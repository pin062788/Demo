package com.ocellus.platform.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplicationContextHolder implements ApplicationContextAware {
	private static Log logger = LogFactory.getLog(ApplicationContextHolder.class);
	private static Map<String, ApplicationContext> applicationContextCache = new HashMap<String, ApplicationContext>();

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		logger.info("===Context name : " + applicationContext.getDisplayName());
		applicationContextCache.put(applicationContext.getDisplayName(), applicationContext);
	}

	public static Object getBean(String beanName) {
		for (Map.Entry<String, ApplicationContext> appEntry : applicationContextCache.entrySet()) {
			ApplicationContext app = appEntry.getValue();
			if (app.getBean(beanName) != null) {
				return app.getBean(beanName);
			}
		}
		return null;
	}

	public static <T> T getBean(Class<T> clazz) throws BeansException {
		for (Map.Entry<String, ApplicationContext> appEntry : applicationContextCache.entrySet()) {
			ApplicationContext app = appEntry.getValue();
			if (app.getBean(clazz) != null) {
				return app.getBean(clazz);
			}
		}
		return null;
	}

	/**
	 * Get message from messageSource bean configured at applicationContext.xml
	 * 
	 * @param code
	 * @return
	 */
	public static String getMessage(String code) {
		for (Map.Entry<String, ApplicationContext> appEntry : applicationContextCache.entrySet()) {
			ApplicationContext app = appEntry.getValue();
			return app.getMessage(code, null, Locale.getDefault());
		}
		return null;
	}

	/**
	 * Get message from messageSource bean configured at applicationContext.xml
	 * 
	 * @param code
	 * @param args
	 * @return
	 */
	public static String getMessage(String code, Object[] args) {
		for (Map.Entry<String, ApplicationContext> appEntry : applicationContextCache.entrySet()) {
			ApplicationContext app = appEntry.getValue();
			return app.getMessage(code, args, Locale.getDefault());
		}
		return null;
	}

}

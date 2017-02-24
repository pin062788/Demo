package com.ocellus.platform.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContextHolder implements ApplicationContextAware {
    private static Log logger = LogFactory.getLog(ApplicationContextHolder.class);
    private static Map<String, ApplicationContext> applicationContextCache = new HashMap<String, ApplicationContext>();

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        logger.info("===Context name : " + applicationContext.getDisplayName());
        applicationContextCache.put(applicationContext.getDisplayName(), applicationContext);

    }

    public static ApplicationContext getApplicationContext(AppCtx contextKey) {
        if (applicationContextCache.get(contextKey.getContextName()) == null)
            return applicationContextCache.values().iterator().next();

        return applicationContextCache.get(contextKey.getContextName());
    }


    public enum AppCtx {
        ROOT_CONTEXT("Root WebApplicationContext"),
        WEB_CONTEXT("WebApplicationContext for namespace 'tilesAction-servlet'");
        private String contextName;

        AppCtx(String name) {
            this.contextName = name;
        }

        public String getContextName() {
            return this.contextName;
        }
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


}

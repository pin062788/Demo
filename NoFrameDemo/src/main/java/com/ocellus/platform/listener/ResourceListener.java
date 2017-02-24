package com.ocellus.platform.listener;

import com.ocellus.platform.service.ListenerService;
import com.ocellus.platform.utils.ApplicationContextHolder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ResourceListener implements ServletContextListener {

    private ListenerService listenerService = null;

    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().log("======================ResourceListener 服务启动ing===========");
        long start = System.currentTimeMillis();
        listenerService = ApplicationContextHolder.getBean(ListenerService.class);
        String rootPath = ResourceListener.class.getClassLoader().getResource("resourceConfig").getPath();
        //listenerService.parseResourceFile(rootPath);
        long end = System.currentTimeMillis();
        event.getServletContext().log("=======================ResourceListener 服务耗时间:" + (end - start) + " ms ===================");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=====销毁成功========");

    }
}

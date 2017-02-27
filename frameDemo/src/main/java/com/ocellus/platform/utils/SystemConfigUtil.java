package com.ocellus.platform.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.URLDecoder;
import java.util.Properties;

public class SystemConfigUtil {
    private static Logger logger = Logger.getLogger(SystemConfigUtil.class);
    private static final String filePath = "conf/config.properties";
    private static Properties sysConfig;
    private static long lastUpdate = 0L;

    public static final String UPLOAD_DEFAULT_PATH="upload.default.path";

    public static String getProperty(String key) {
        init();
        return sysConfig.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        init();
        return sysConfig.getProperty(key, defaultValue);
    }

    public static void setProperty(String key, String value) {
        init();
        sysConfig.setProperty(key, value);
    }

    public static boolean getBoolean(String key) {
        return "true".equalsIgnoreCase(getProperty(key));
    }

    private static void init() {
        if (sysConfig == null) {
            sysConfig = new Properties();
        }
        try {
            String configPath = URLDecoder.decode(SystemConfigUtil.class.getClassLoader().getResource(filePath).getPath(), "UTF-8");
            File configFile = new File(configPath);
            if (configFile.exists()) {
                if (configFile.lastModified() != lastUpdate) {
                    lastUpdate = configFile.lastModified();
                    sysConfig.load(SystemConfigUtil.class.getClassLoader().getResourceAsStream(filePath));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


}

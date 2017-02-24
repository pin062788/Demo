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
    public static final String UPLOAD_CSV_PATH="upload.csv.path";
    public static final String FILE_DATA_PATH="file.data.path";
    public static final String FILE_RSCSV_PATH="file.rscsv.path";
    public static final String FILE_RSCRIPT_PATH="file.rscript.path";
    public static final String FILE_R_MODELSCRIPT_PATH="file.r.modelscript.path";
    public static final String FILE_ANALYZECSV_PATH="file.analyzecsv.path";
    public static final String FILE_RLIB_PATH="file.rlib.path";
    public static final String FILE_GROOVY_PATH="file.groovy.path";

    public static final String JRI_USED="JRI.used";

    public static final String JRI_LIBPATHS="JRI.libPaths";
    public static final String JRI_SOURCER_PATH="JRI.sourceR.path";
    public static final String GROOVY_NAME="groovy.name";

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

package com.ocellus.platform.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.URLDecoder;
import java.util.Properties;

public class TipMsgUtil {
    private static Logger logger = Logger.getLogger(TipMsgUtil.class);
    private static final String filePath = "conf/message.properties";
    private static Properties sysConfig;
    private static long lastUpdate = 0L;
    // 附加部分 start
    private static final String SAVE_SUCCESSED = "save.successed";
    private static final String SAVE_FAILED = "save.failed";
    private static final String SAVE_EXCEPTION = "save.exception";

    private static final String DELETE_SUCCESSED = "delete.successed";
    private static final String DELETE_FAILED = "delete.failed";
    private static final String DELETE_EXCEPTION = "delete.exception";

    private static final String BUTTON_NEW_TEXT = "button.new.text";
    private static final String BUTTON_UPDATE_TEXT = "button.update.text";
    private static final String BUTTON_DELETE_TEXT = "button.delete.text";
    private static final String BUTTON_SEARCH_TEXT = "button.search.text";
    private static final String BUTTON_RESET_TEXT = "button.reset.text";
    private static final String BUTTON_SAVE_TEXT = "button.save.text";
    private static final String BUTTON_CLOSE_TEXT = "button.close.text";


    public static String getSaveSuccessedTip() {
        init();
        return sysConfig.getProperty(SAVE_SUCCESSED,"保存成功!");
    }
    public static String getSaveFailedTip() {

        init();return sysConfig.getProperty(SAVE_FAILED,"保存失败!");
    }
    public static String getSaveExceptionTip() {
        init();
        return sysConfig.getProperty(SAVE_EXCEPTION,"保存异常!");
    }

    public static String getDeleteSuccessedTip() {
        init();
        return sysConfig.getProperty(DELETE_SUCCESSED,"删除成功!");
    }
    public static String getDeleteFailedTip() {
        init();
        return sysConfig.getProperty(DELETE_FAILED,"删除失败!");
    }
    public static String getDeleteExceptionTip() {
        init();
        return sysConfig.getProperty(DELETE_EXCEPTION,"删除异常!");
    }

    public static String getButtonNewText(){
        init();
        return sysConfig.getProperty(BUTTON_NEW_TEXT,"新增");
    }
    public static String getButtonUpdateText(){
        init();
        return sysConfig.getProperty(BUTTON_UPDATE_TEXT,"更新");
    }
    public static String getButtonDeleteText(){
        init();
        return sysConfig.getProperty(BUTTON_DELETE_TEXT,"删除");
    }
    public static String getButtonSearchText(){
        init();
        return sysConfig.getProperty(BUTTON_SEARCH_TEXT,"查询");
    }
    public static String getButtonResetText(){
        init();
        return sysConfig.getProperty(BUTTON_RESET_TEXT,"查询");
    }
    public static String getButtonSaveText(){
        init();
        return sysConfig.getProperty(BUTTON_SAVE_TEXT,"保存");
    }
    public static String getButtonCloseText(){
        init();
        return sysConfig.getProperty(BUTTON_CLOSE_TEXT,"关闭");
    }
    // 附加部分 end

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
            String configPath = URLDecoder.decode(TipMsgUtil.class.getClassLoader().getResource(filePath).getPath(), "UTF-8");
            File configFile = new File(configPath);
            if (configFile.exists()) {
                if (configFile.lastModified() != lastUpdate) {
                    lastUpdate = configFile.lastModified();
                    sysConfig.load(TipMsgUtil.class.getClassLoader().getResourceAsStream(filePath));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


}

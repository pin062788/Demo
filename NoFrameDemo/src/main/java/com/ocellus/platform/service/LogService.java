package com.ocellus.platform.service;

import com.ocellus.platform.dao.LogDAO;
import com.ocellus.platform.model.LogFile;
import com.ocellus.platform.model.SysLog;
import com.ocellus.platform.model.UserLog;
import com.ocellus.platform.utils.SystemConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LogService extends AbstractService<UserLog, String> {


    private LogDAO logDAO;


    public List<UserLog> searchUserLog(Map params) {
        return logDAO.searchUserLog(params);
    }

    public List<SysLog> searchSysLog(Map params) {
        return logDAO.searchSysLog(params);
    }

    public void insertUserLog(UserLog userLog) {

        super.insert(userLog);
    }

    public List<UserLog> searchInterfaceLog(Map params) {
        return logDAO.searchInterfaceLog(params);
    }

    public List<LogFile> getLogFileList() {
        String logDirPath = SystemConfigUtil.getProperty("log.dir.path");
        File uploadDir = new File(logDirPath);
        return getDirFiles(uploadDir);
    }

    public List<LogFile> getDirFiles(File dir) {
        List<LogFile> list = new ArrayList<LogFile>();
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        LogFile log = new LogFile();
                        log.setFileName(file.getName());
                        log.setFilePath(file.getPath());
                        list.add(log);
                    } else {
                        list.addAll(getDirFiles(file));
                    }
                }
            }
        }
        return list;
    }

    @Autowired
    public void setLogDAO(LogDAO logDAO) {
        super.setDao(logDAO);
        this.logDAO = logDAO;
    }
}

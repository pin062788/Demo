package com.ocellus.platform.dao;

import com.ocellus.platform.model.SysLog;
import com.ocellus.platform.model.UserLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface LogDAO extends BaseDAO<UserLog, String> {
    public List<UserLog> searchUserLog(Map params);

    public List<UserLog> searchInterfaceLog(Map params);

    public List<SysLog> searchSysLog(Map params);

    public void insertUserLog(UserLog userLog);
}

package com.ocellus.platform.service;

import com.ocellus.platform.dao.EmployeeDAO;
import com.ocellus.platform.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 人员信息：Service
 */
@Service("employeeService")
public class EmployeeService extends AbstractService<Employee, String> {
    @SuppressWarnings("unused")
    private EmployeeDAO employeeDAO;

    @Autowired
    public void setDao(EmployeeDAO dao) {

        super.setDao(dao);
        this.employeeDAO = dao;
    }

    public void deleteByIds(Map<String, String> map) {

        if (null != map.get("ids") && !"".equals(map.get("ids"))) {
            String[] ids = map.get("ids").toString().split(",");
            for (String id : ids) {
                this.delete(id);
            }
        }

    }

}

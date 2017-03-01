package com.ocellus.business.service;

import com.ocellus.business.dao.TestDAO;
import com.ocellus.business.model.Test;
import com.ocellus.platform.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bi.jialong on 2017/2/28 0028.
 */
@Service
public class TestService extends AbstractService<Test,String> {
    private TestDAO baseDao;

    @Autowired
    public void setTestDAO(TestDAO baseDao){
        this.baseDao = baseDao;
        super.setDao(baseDao);
    }

    @Override
    public Test getBaseBean() {
        return new Test();
    }
}

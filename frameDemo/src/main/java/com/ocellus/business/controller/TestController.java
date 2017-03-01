package com.ocellus.business.controller;

import com.ocellus.business.model.Test;
import com.ocellus.business.service.TestService;
import com.ocellus.platform.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bi.jialong on 2017/2/28 0028.
 */
@Controller
@RequestMapping("test")
public class TestController extends BaseController<Test,String> {
    public static final String MODEL_NAME = "test";
    private TestService baseService;
    @Autowired
    public void setBaseService(TestService baseService) {
        this.baseService = baseService;
        super.setBaseService(baseService);
        super.setModelName(MODEL_NAME);
    }

}

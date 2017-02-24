package com.ocellus.platform.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    @RequestMapping("/show")
    @RequiresPermissions("monitor:view")
    public String show() {
        return "/monitoring/show";
    }
}

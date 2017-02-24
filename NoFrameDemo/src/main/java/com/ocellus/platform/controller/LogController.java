package com.ocellus.platform.controller;

import com.ocellus.platform.model.LogFile;
import com.ocellus.platform.model.PageRequest;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.service.LogService;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/log")
public class LogController extends BaseController {
    @Autowired
    private LogService logService;

    @RequestMapping("/showLogFileList.do")
    public String showLogFile() {
        return "log/logFileList";
    }

    @RequestMapping("/getLogFileList.do")
    @ResponseBody
    public PageResponse getLogFileList(HttpServletRequest request) {
        getParamMapWithPage(request);
        PageRequest req = PageRequest.get();
        List<LogFile> list = new ArrayList<LogFile>();
        List<LogFile> all = logService.getLogFileList();
        Integer startIndex = (req.getPage() - 1) * req.getRows();
        Integer endIndex = startIndex + req.getRows();
        if (endIndex > all.size())
            endIndex = all.size();
        for (int i = startIndex; i < endIndex; i++) {
            list.add(all.get(i));
        }
        int total = all.size();
        PageResponse resp = PageResponse.get();
        resp.setRecords(total);
        resp.setPage(req.getPage());
        int numOfPages = 0;
        if (total > 0) {
            numOfPages = total % req.getRows() == 0 ? total / req.getRows() : total / req.getRows() + 1;
        }
        resp.setTotal(numOfPages);
        resp.setRows(list);
        PageRequest.remove();
        return resp;
    }

    @RequestMapping("/downloadLogFile.do")
    @ResponseBody
    public void downloadLogFile(HttpServletRequest request, HttpServletResponse res, String filePath, String fileName) throws IOException, Exception {
        OutputStream os = res.getOutputStream();
        try {
            res.reset();
            res.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            res.setContentType("application/octet-stream; charset=utf-8");
            File file = new File(filePath, fileName);
            if (file.exists() && file.isFile()) {
                os.write(FileUtils.readFileToByteArray(file));
                os.flush();
            } else {
                throw new Exception("文件不存在");
            }
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public String sysLogShow() {
        return "log/sysLog";
    }

    @RequestMapping("/userLogShow.do")
    @RequiresPermissions("userlog:view")
    public String userLogShow() {
        return "log/userLog";
    }

    @RequestMapping("/interfaceLogShow.do")
    @RequiresPermissions("userlog:view")
    public String interfaceLogShow() {
        return "log/interfaceLog";
    }

    @RequestMapping("/sysLogList.do")
    @ResponseBody

    public PageResponse sysLogList(HttpServletRequest request) {
        Map map = getParamMapWithPage(request);
        return getPageResponse(logService.searchSysLog(map));
    }

    @RequestMapping("/userLogList.do")
    @ResponseBody
    @RequiresPermissions("userlog:view")
    public PageResponse userLogList(HttpServletRequest request) {
        Map map = getParamMapWithPage(request);
        return getPageResponse(logService.searchUserLog(map));
    }

    @RequestMapping("/interfaceLogList.do")
    @ResponseBody
    public PageResponse interfaceLogList(HttpServletRequest request) {
        Map map = getParamMapWithPage(request);
        return getPageResponse(logService.searchInterfaceLog(map));
    }


}

package com.ocellus.platform.utils;

import com.ocellus.platform.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

public class WebUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            return attrs.getRequest();
        }
        return null;
    }

    /**
     * 获取登陆用户的 真实姓名  no登陆账号
     *
     * @return
     */
    public static String getLoginUserName() {
        User user = getLoginUser();
        if (user != null && !StringUtil.isEmpty(user.getRelatedName())) {
            return user.getRelatedName();
        } else {
            try {
                Subject currentUser = SecurityUtils.getSubject();
                return currentUser.getPrincipal() + "";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String getCompanyCode() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            if (request != null) {
                HttpSession session = request.getSession();
                return (String) session.getAttribute(Constants.SESSION_ORG_CODE);
            }
        }
        return null;
    }

    public static User getLoginUser() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            if (request != null) {
                HttpSession session = request.getSession();
                return (User) session.getAttribute(Constants.SESSION_USER_KEY);
            }
        }
        return null;
    }

    public static void download(HttpServletResponse response, String filePath, boolean delete) {
        File file = new File(filePath);
        InputStream fis = null;
        OutputStream toClient = null;

        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(file.getName().getBytes("utf-8"), "utf-8"));
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            fis = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            toClient.write(buffer);
            toClient.flush();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (toClient != null)
                    toClient.close();
                if (delete) {
                    File delfile = new File(filePath);
                    delfile.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

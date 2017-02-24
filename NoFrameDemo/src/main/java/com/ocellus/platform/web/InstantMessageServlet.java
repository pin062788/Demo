package com.ocellus.platform.web;

import com.ocellus.platform.model.InstantMessage;
import com.ocellus.platform.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class Customer
 */
public class InstantMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(InstantMessageServlet.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InstantMessageServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
//		response.setHeader("Cache-Control", "no-cache");
        request.setCharacterEncoding("utf-8");
        String content = request.getParameter("content");
        String readFlag = request.getParameter("msgFlag");
        PrintWriter out = response.getWriter();
        Object us = request.getSession().getAttribute("userName");
        String toUser = request.getParameter("toUser");
        String usStyle = "";
        if (toUser != null && !"".equals(toUser) && us != null && !"".equals(us) && toUser.indexOf(",") == -1) {//单人
            if (InstantMessage.getListUser() != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (toUser.equals(us) && (readFlag == null || "".equals(readFlag))) {
                    out.write("不能给自己发送消息！");
                    out.close();
                    return;
                }
                usStyle = "<font color='red'>" + us + "</font>";
                if (content != null && !"".equals(content)) {//发送消息
                    content = java.net.URLDecoder.decode(request.getParameter("content"), "UTF-8");
                    List<User> list = null;
                    User user = null;
                    boolean flag = false;
                    for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
                        map = InstantMessage.getListUser().get(i);
                        if (map.get("userName").equals(us)) {//查找当前用户的消息列表
                            list = (List<User>) map.get("userMsg");
                            flag = false;
                            if (null != list && list.size() > 0) {
                                for (int j = 0; j < list.size(); j++) {//判断当前是否保存了发送人消息列表
                                    user = list.get(j);
                                    if (toUser.equals(user.getToUser())) {
                                        user.getSendMsg().add(usStyle + " 对 " + toUser + " ： " + content);
                                        flag = true;
                                        //break;
                                    }
                                }
                            }
                            if (!flag) {//如果当前接收用户不存在就增加
                                User userTmp = new User();
                                //userTmp.setMsg(new ArrayList<String>());
                                userTmp.setToUser(toUser);//接收人
                                userTmp.setUserName(us.toString());//发送人
                                userTmp.getSendMsg().add(usStyle + " 对 " + toUser + " ： " + content);
                                list.add(userTmp);
                            }
                        }

                    }
                    out.write(usStyle + " 对 " + toUser + " ： " + content);
                    logger.debug(usStyle + " 对 " + toUser + " ： " + content);

                }
                if (readFlag != null && !"".equals(readFlag)) {//读取消息
                    if (toUser.equals(us)) {
                        out.write("");
                        out.close();
                        return;
                    }
                    List<User> list = null;
                    User user = null;
                    String txt = "";
                    for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
                        map = InstantMessage.getListUser().get(i);
                        if (toUser.equals(map.get("userName"))) {
                            list = (List<User>) map.get("userMsg");
                            for (int j = 0; j < list.size(); j++) {
                                user = list.get(j);
                                if (us.equals(user.getToUser())) {
                                    for (int c = 0; c < user.getSendMsg().size(); c++) {
                                        txt += user.getSendMsg().get(c);
                                        if (c != user.getSendMsg().size() - 1)
                                            txt += "\n";
                                    }
                                    user.getSendMsg().clear();
                                    break;
                                }
                            }
                        }
                    }
                    out.write(txt);
                }
            }
        } else if (toUser != null && !"".equals(toUser) && us != null && !"".equals(us) && toUser.indexOf(",") != -1) {//多用户发送------------------------------
            if (InstantMessage.getListUser() != null) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (readFlag == null || "".equals(readFlag))
                    if (toUser.indexOf(",") != -1) {
                        String[] userArr = toUser.split(",");
                        for (int i = 0; i < userArr.length; i++) {
                            if (userArr[i].equals(us)) {
                                out.write("不能给自己发送消息！");
                                out.close();
                                return;
                            }
                        }
                    }
                usStyle = "<font color='red'>" + us + "</font>";
                if (content != null && !"".equals(content)) {//发送消息
                    content = java.net.URLDecoder.decode(request.getParameter("content"), "UTF-8");
                    List<User> list = null;
                    User user = null;
                    boolean flag = false;
                    String[] userArr = null;
                    String toUserTmp = "";
                    if (toUser.indexOf(",") != -1) {
                        userArr = toUser.split(",");
                    }
                    for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
                        map = InstantMessage.getListUser().get(i);
                        if (map.get("userName").equals(us)) {//查找当前用户的消息列表
                            list = (List<User>) map.get("userMsg");
                            if (userArr != null && userArr.length > 0) {//判断是否为多人
                                for (int r = 0; r < userArr.length; r++) {
                                    flag = false;
                                    toUserTmp = userArr[r];
                                    if (null != list && list.size() > 0) {
                                        for (int j = 0; j < list.size(); j++) {//判断当前是否保存了发送人消息列表
                                            user = list.get(j);
                                            if (toUserTmp.equals(user.getToUser())) {
                                                user.getSendMsg().add(usStyle + " 对 " + toUserTmp + " ： " + content);
                                                flag = true;
                                            }
                                        }
                                    }
                                    if (!flag) {//如果当前接收用户不存在就增加
                                        User userTmp = new User();
                                        userTmp.setToUser(toUserTmp);//接收人
                                        userTmp.setUserName(us.toString());//发送人
                                        userTmp.getSendMsg().add(usStyle + " 对 " + toUserTmp + " ： " + content);
                                        list.add(userTmp);
                                    }
                                }
                            }
                        }
                    }
                    out.write(usStyle + " 对 " + toUser + " ： " + content);
                    logger.debug(usStyle + " 对 " + toUser + " ： " + content);
                }
                if (readFlag != null && !"".equals(readFlag)) {//读取消息

                    String[] userArr = null;
                    String toUserTmp = "";
                    if (toUser.indexOf(",") != -1) {
                        userArr = toUser.split(",");
                    }

                    List<User> list = null;
                    User user = null;
                    String txt = "";
                    for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
                        map = InstantMessage.getListUser().get(i);
                        if (userArr != null && userArr.length > 0) {
                            for (int r = 0; r < userArr.length; r++) {
                                toUserTmp = userArr[r];
                                if (toUserTmp.equals(map.get("userName"))) {
                                    list = (List<User>) map.get("userMsg");
                                    begin:
                                    for (int j = 0; j < list.size(); j++) {
                                        user = list.get(j);
                                        if (us.equals(user.getToUser())) {
                                            for (int c = 0; c < user.getSendMsg().size(); c++) {
                                                txt += user.getSendMsg().get(c) + "\n";
                                            }
                                            user.getSendMsg().clear();
                                            break begin;
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if (txt.lastIndexOf("\n") != -1) {
                        txt = txt.substring(0, txt.lastIndexOf("\n"));
                        //System.out.println("txt存在换行符:"+txt);
                    }
                    out.write(txt);
                }
            }
        } else {
            out.write("");
        }
        out.close();
    }
}

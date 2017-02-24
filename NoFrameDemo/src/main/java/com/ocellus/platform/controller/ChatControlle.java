package com.ocellus.platform.controller;

import com.ocellus.platform.model.InstantMessage;
import com.ocellus.platform.model.User;
import com.ocellus.platform.web.view.AjaxView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ChatControlle extends BaseController {
    @ResponseBody
    @RequestMapping("/chat/getUserList")
    public List<Map<String, Object>> getUserList(HttpServletRequest request) {
        Map<String, String> param = getParamMap(request);
        String servMsgFlag = param.get("servMsgFlag");
        if (null != servMsgFlag && !"".equals(servMsgFlag)) {//读取当前用户列表
            if (InstantMessage.getMap() != null) {
                Iterator iter = InstantMessage.getMap().keySet().iterator();
                List<Map<String, Object>> list = new ArrayList();
                Map map = new HashMap();
                while (iter.hasNext()) {
                    User user = InstantMessage.getMap().get(iter.next());
                    if (user.getUserName().split("--")[0].equals(request.getSession().getAttribute("userName")))
                        continue;
                    map = new HashMap();
                    map.put("userName", user.getUserName());
                    if (user.getToUser().equals(request.getSession().getAttribute("userName")))
                        map.put("msgList", user.getMsg());
                    else
                        map.put("msgList", new ArrayList());
                    map.put("toUser", user.getToUser());
                    list.add(map);
                }
                return list;
            }
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("/chat/getUserLists")
    public List<Map<String, Object>> getUserLists(HttpServletRequest request) {
        Map<String, String> param = getParamMap(request);
        String servMsgFlag = param.get("servMsgFlag");
        if (null != servMsgFlag && !"".equals(servMsgFlag)) {//读取当前用户列表
            if (InstantMessage.getListUser() != null) {
                return InstantMessage.getListUser();
            }
        }
        return null;
    }

    /**
     * 聊天首页
     *
     * @param request
     * @return
     */
    @RequestMapping("/chat/toInstantMessaging")
    public ModelAndView toInstantMessagingIndex(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/chat/index");
        return mv;
    }

    /**
     * 聊天首页
     *
     * @param request
     * @return
     */
    @RequestMapping("/chat/openInstantMessaging")
    public ModelAndView toInstantMessaging(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/chat/serverAsk");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/chat/getMessageCount")
    public AjaxView getMessageCount(HttpServletRequest request) {
        AjaxView rtn = new AjaxView();
        String userName = (String) request.getSession().getAttribute("userName");
        rtn.setData(InstantMessage.getMessageCount(userName));
        return rtn;
    }
}

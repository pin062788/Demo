package com.ocellus.platform.controller;

import com.ocellus.platform.model.InstantMessage;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.Resource;
import com.ocellus.platform.model.User;
import com.ocellus.platform.service.ResourceService;
import com.ocellus.platform.service.UserService;
import com.ocellus.platform.utils.WebUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main")
public class HomeController extends BaseController {
    private static Logger logger = Logger.getLogger(HomeController.class);
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public ModelAndView showIndex(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("main");
        User user = userService.getLoginUser();
        request.getSession().setAttribute("userName", user.getUserName());
        setUserMsg(user.getUserName());

        Map params = new HashMap();
        params.put("userId", WebUtil.getLoginUser().getUserId());
        List<Resource> shortcutMenu = resourceService.selectShortcutMenu(params);
        mv.addObject("shortcutMenu", shortcutMenu);
        return mv;
    }

    /**
     * 普通方式登录调用的方法，cas模式下不执行次方法，请不要在该方法中添加任何其他代码。
     */
    @RequestMapping(value = "/loginAuth.do")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            AuthenticationToken token = new UsernamePasswordToken(userName, password);
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(token);
            mv.setViewName("redirect:/main/index.do");
            return mv;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            request.setAttribute("error", "用户名或密码错误，请重新输入！");
            mv.setViewName("../../login");
            return mv;
        }
    }

    @RequestMapping(value = "/logout.do")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> mapTmp = new HashMap<String, Object>();
        for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
            mapTmp = InstantMessage.getListUser().get(i);
            if (mapTmp.get("userName").equals(request.getSession().getAttribute("userName"))) {
                InstantMessage.getListUser().remove(i);
                break;
            }
        }
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "../../login";
    }

    @ResponseBody
    @RequestMapping("/getMenu")
    public List<Resource> getMenu(HttpSession session) {
        return resourceService.getMenu(userService.getLoginUser());
        //return resourceService.getChildren("0");
    }

    private synchronized void setUserMsg(String userName) {//添加用户列表
        Map<String, Object> mapTmp = new HashMap<String, Object>();
        boolean flag = false;
        for (int i = 0; i < InstantMessage.getListUser().size(); i++) {
            mapTmp = InstantMessage.getListUser().get(i);
            if (mapTmp.get("userName").equals(userName)) {
                flag = true;
            }
/*			if(mapTmp.containsValue(userName)){
                flag = true;
			}
*/
        }
        if (!flag) {
            List<User> list = new ArrayList<User>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", userName);
            map.put("userMsg", list);
            InstantMessage.getListUser().add(map);
        }
    }

    @RequestMapping("/showSearchList")
    public ModelAndView showSearchList(@RequestParam("criterion") String criterion) {
        ModelAndView mv = new ModelAndView("/main/searchList");
        mv.addObject("criterion", criterion);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/search")
    public PageResponse search(HttpServletRequest request) {
        //search resource
        Map<String, String> params = getParamMapWithPage(request);
        params.put("resourceName", request.getParameter("criterion"));
        List<Resource> res = resourceService.search(params);

        return getPageResponse(res);
    }

    @ResponseBody
    @RequestMapping("/searchLeftTree")
    public List<Resource> searchLeftTree(HttpServletRequest request) {
        //search resource
        Map<String, String> params = getParamMapWithPage(request);
        params.put("resourceName", params.get("criterion"));
        List<Resource> res = resourceService.search(params);

        return res;
    }

    /**
     * 获取当前登录账号信息
     *
     * @return
     */
    @RequestMapping("/getUserLogin")
    @ResponseBody
    public String getUserLogin() {
        return WebUtil.getLoginUserName();
    }

    @RequestMapping("/indexPage")
    public ModelAndView indexPage() {
        ModelAndView mv = new ModelAndView("/main/indexMain");
        return mv;
    }

    @RequestMapping("/mainPage")
    public ModelAndView mainPage() {
        ModelAndView mv = new ModelAndView("/main/userIndex");
        return mv;
    }

}

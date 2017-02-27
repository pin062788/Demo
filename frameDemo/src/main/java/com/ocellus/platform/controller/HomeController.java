package com.ocellus.platform.controller;

import com.ocellus.platform.model.InstantMessage;
import com.ocellus.platform.model.Resource;
import com.ocellus.platform.model.User;
import com.ocellus.platform.service.ResourceService;
import com.ocellus.platform.service.UserService;
import com.ocellus.platform.utils.StringUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        ModelAndView mv = new ModelAndView("home");
        Map<String, String> requestMap = getParamMap(request);
        User user = userService.getLoginUser();
        String theme = "start";//"start";
        /*if(User.RELATED_TYPE_EMPLOYEE.equals(user.getRelatedType())){
            theme = "default";
        }else if(User.RELATED_TYPE_SUPPLIER.equals(user.getRelatedType())){
            theme = "black-tie";
        }*/
        request.getSession().setAttribute("theme", theme);
        request.getSession().setAttribute("loginUserName", user.getRelatedName());
        mv.addAllObjects(requestMap);
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
            if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(password)) {
                try {
                    AuthenticationToken token = new UsernamePasswordToken(userName, password);
                    Subject currentUser = SecurityUtils.getSubject();
                    currentUser.login(token);
                    mv.setViewName("redirect:/main/index.do");
                    return mv;
                } catch (Exception e) {
                    logger.error("Error: ", e);
                    request.setAttribute("error", "用户名或密码错误，请重新输入!");
                    mv.setViewName("../../login");
                    return mv;
                }
            } else {
                request.setAttribute("error", "用户名或密码错误，请重新输入!");
                mv.setViewName("../../login");
                return mv;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            request.setAttribute("error", "用户名或密码错误，请重新输入!");
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
    }

    @RequestMapping("/mainPage")
    public ModelAndView mainPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("mainPage");
        Map<String, String> requestMap = getParamMap(request);
        mv.addAllObjects(requestMap);
        return mv;
    }

}

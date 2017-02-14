package controller;

import annotation.LogFunction;
import com.alibaba.fastjson.JSON;
import form.SignInForm;
import form.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zya on 2016/9/8.
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/signInCheck.json")
    @ResponseBody
    @LogFunction
    public String signInCheck(HttpServletRequest request, SignInForm signInForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (!signInForm.validate()) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = userService.signInCheck(request, signInForm);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/signUpCheck.json")
    @ResponseBody
    @LogFunction
    public String signUpCheck(HttpServletRequest request, SignUpForm signUpForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        HttpSession session = request.getSession();

        if (!signUpForm.validate(session)) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = userService.signUpCheck(request, signUpForm);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/verifyCodeCheck.json")
    @ResponseBody
    @LogFunction
    public String verifyCodeCheck(HttpServletRequest request, String signUpEmail) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(signUpEmail) || !signUpEmail.contains("@")) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = userService.verifyCodeCheck(request, signUpEmail);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/signOut.json")
    @ResponseBody
    @LogFunction
    public String signOut(HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        request.getSession().setAttribute("loginUser", null);
        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "logoutSuccess");

        return JSON.toJSONString(returnMap);//.getBytes("UTF-8"),"iso8859-1");
    }

}

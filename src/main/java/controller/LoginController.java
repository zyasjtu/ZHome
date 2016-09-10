package controller;

import annotation.LogFunction;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zya on 2016/9/8.
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/loginCheck.json")
    @ResponseBody
    @LogFunction
    public String loginCheck(HttpServletRequest request,
                             @RequestParam String inputName,
                             @RequestParam String inputPassword) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (StringUtils.isEmpty(inputName) || StringUtils.isEmpty(inputPassword)) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = userService.loginCheck(request, inputName, inputPassword);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/logout.json")
    @ResponseBody
    @LogFunction
    public String logout(HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        request.getSession().setAttribute("loginUser", null);
        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "logoutSuccess");
        return JSON.toJSONString(returnMap);
    }

}

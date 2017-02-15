package service;

import dao.UserDao;
import form.SignInForm;
import form.SignUpForm;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.VerifyCodeUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zya on 2016/9/8.
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public Map<String, Object> signInCheck(HttpServletRequest request, SignInForm signInForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            List<User> users = userDao.findUser(signInForm.getSignInEmail(), signInForm.getSignInPassword());
            if (users.size() == 0) {
                returnMap.put("respCode", "1001");
                returnMap.put("respMsg", "inputWrong");
                request.getSession().setAttribute("signInUser", null);
            } else {
                returnMap.put("respCode", "1000");
                returnMap.put("respMsg", "signInSuccess");
                request.getSession().setAttribute("signInUser", users.get(0));
            }
        } catch (Exception e) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "signInFail");
            request.getSession().setAttribute("signInUser", null);
        }
        return returnMap;
    }

    public Map<String, Object> signUpCheck(HttpServletRequest request, SignUpForm signUpForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            User user = new User();
            user.setEmail(signUpForm.getSignUpEmail());
            user.setPassword(signUpForm.getSignUpPassword1());
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userDao.addUser(user);

            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "signUpSuccess");
            request.getSession().setAttribute("signInUser", user);
        } catch (Exception e) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "signUpFail");
            request.getSession().setAttribute("signInUser", null);
        }
        return returnMap;
    }

    public Map<String, Object> changeVerifyCode(HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            String verifyCode = VerifyCodeUtil.generateVerifyCode(request);
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "verifyCodeSuccess");
            request.getSession().setAttribute("verifyCode", verifyCode);
        } catch (Exception e) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "verifyCodeFail");
            request.getSession().setAttribute("verifyCode", null);
        }
        return returnMap;
    }
}

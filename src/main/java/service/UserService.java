package service;

import dao.UserDao;
import form.SignInForm;
import form.SignUpForm;
import model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RSAUtil;
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
    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;

    public Map<String, Object> signInCheck(HttpServletRequest request, SignInForm signInForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            String signInEmail = RSAUtil.decrypt(signInForm.getSignInEmail(), RSAUtil.PRIVATE_KEY);
            String signInPassword = RSAUtil.decrypt(signInForm.getSignInPassword(), RSAUtil.PRIVATE_KEY);

            List<User> users = userDao.findUser(signInEmail, signInPassword);
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
            LOGGER.error("signInFail", e);
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "signInFail");
            request.getSession().setAttribute("signInUser", null);
        }
        return returnMap;
    }

    public Map<String, Object> signUpCheck(HttpServletRequest request, SignUpForm signUpForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            String signUpEmail = RSAUtil.decrypt(signUpForm.getSignUpEmail(), RSAUtil.PRIVATE_KEY);
            String signUpPassword = RSAUtil.decrypt(signUpForm.getSignUpPassword1(), RSAUtil.PRIVATE_KEY);

            User user = new User();
            user.setEmail(signUpEmail);
            user.setPassword(signUpPassword);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            userDao.addUser(user);

            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "signUpSuccess");
            request.getSession().setAttribute("signInUser", user);
        } catch (Exception e) {
            LOGGER.error("signUpFail", e);
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
            LOGGER.error("verifyCodeFail", e);
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "verifyCodeFail");
            request.getSession().setAttribute("verifyCode", null);
        }
        return returnMap;
    }
}

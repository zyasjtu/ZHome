package service;

import dao.UserDao;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public Map<String, Object> loginCheck(HttpServletRequest request, String inputName, String inputPassword) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<User> users = userDao.findUser(inputName, inputPassword);
        if (users.size() == 0) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "loginFail");
            request.getSession().setAttribute("loginUser", null);
        } else {
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "loginSuccess");
            request.getSession().setAttribute("loginUser", users.get(0));
        }

        return returnMap;
    }
}

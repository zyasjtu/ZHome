package service;

import dao.MessageDao;
import model.Message;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zya on 2016/9/9.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    public Map<String, Object> getMessages(Integer pageNo, Integer pageSize) {
        Map<String,Object> returnMap = new HashMap<String, Object>();
        List<Message> messages = messageDao.findMessage(pageNo, pageSize);
        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "getMessageSuccess");
        returnMap.put("messages", messages);

        return returnMap;
    }

    public Map<String,Object> addMessage(HttpServletRequest request, String text, String imageUrl) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        User user = (User) request.getSession().getAttribute("loginUser");

        Message message = new Message();
        message.setCreator(user.getName());
        message.setCreateIp(request.getRemoteAddr());
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        message.setText(text);
        message.setImageUrl(imageUrl);
        messageDao.addMessage(message);

        returnMap.put("respCode", "1000");
        returnMap.put("respMsg", "addMessageSuccess");

        return returnMap;
    }
}

package service;

import dao.TopicDao;
import form.TopicForm;
import model.Topic;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by z673413 on 2017/2/15.
 */
@Service
public class TopicService {
    @Autowired
    private TopicDao topicDao;

    public Map<String, Object> addTopic(HttpServletRequest request, TopicForm topicForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        try {
            User user = (User) request.getSession().getAttribute("signInUser");
            String email = user.getEmail();
            Topic topic = new Topic();
            topic.setSubject(topicForm.getSubject());
            topic.setDetail(topicForm.getDetail());
            topic.setAuthor(email.substring(0, email.indexOf("@")));
            topic.setAuthorWechat(topicForm.getAuthorWecha());
            topic.setAuthorQQ(topicForm.getAuthorQQ());
            topic.setAuthorEmail(topicForm.getAuthorEmail());
            topic.setAuthorMobile(topicForm.getAuthorMobile());
            topic.setAuthorIp(request.getRemoteAddr());
            topic.setAuthorType(topicForm.getAuthorType());
            topic.setCreateTime(new Date());
            topic.setUpdateTime(new Date());
            topicDao.addTopic(topic);

            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "addTopicSuccess");
        } catch (Exception e) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "addTopicFail");
        }

        return returnMap;
    }

    public Map<String, Object> findAllTopic() {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        try {
            List<Topic> topics = topicDao.findAllTopic();
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "findTopicSuccess");
            returnMap.put("topics", topics);
        } catch (Exception e) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "findTopicFail");
        }

        return returnMap;
    }
}

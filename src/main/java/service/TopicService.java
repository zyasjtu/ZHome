package service;

import dao.TopicDao;
import form.PublishForm;
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

    public Map<String, Object> addTopic(HttpServletRequest request, PublishForm publishForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        try {
            User user = (User) request.getSession().getAttribute("signInUser");
            String email = user.getEmail();
            Topic topic = new Topic();
            topic.setSubject(publishForm.getPublishSubject());
            topic.setDetail(publishForm.getPublishDetail());
            topic.setAuthor(email.substring(0, email.indexOf("@")));
            topic.setAuthorWechat(publishForm.getPublishWechat());
            topic.setAuthorQQ(publishForm.getPublishQQ());
            topic.setAuthorEmail(publishForm.getPublishEmail());
            topic.setAuthorMobile(publishForm.getPublishMobile());
            topic.setAuthorIp(request.getRemoteAddr());
            topic.setAuthorType(publishForm.getPublishType());
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
            List<Topic> tasks = topicDao.findTopic(1);
            List<Topic> technologies = topicDao.findTopic(2);
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "findTopicSuccess");
            returnMap.put("tasks", tasks);
            returnMap.put("technologies", technologies);
        } catch (Exception e) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "findTopicFail");
        }

        return returnMap;
    }
}

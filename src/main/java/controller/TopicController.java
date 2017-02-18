package controller;

import com.alibaba.fastjson.JSON;
import form.PublishForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.TopicService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by z673413 on 2017/2/15.
 */
@Controller
public class TopicController {
    @Autowired
    private TopicService topicService;

    @RequestMapping("/addTopic.json")
    @ResponseBody
    public String addTopic(HttpServletRequest request, PublishForm publishForm) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (!publishForm.validate(request)) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = topicService.addTopic(request, publishForm);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/findAllTopic.json")
    @ResponseBody
    public String findAllTopic() {
        Map<String, Object> returnMap = topicService.findAllTopic();
        return JSON.toJSONStringWithDateFormat(returnMap, "yyyy-MM-dd HH:mm:ss.SSS");
    }
}

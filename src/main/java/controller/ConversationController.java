package controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.ConversationService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zya on 2016/9/9.
 */
@Controller
public class ConversationController {
    @Autowired
    private ConversationService conversationService;

    @RequestMapping("/getConversations.json")
    @ResponseBody
    public String getConversations(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (pageNo < 1 || pageSize < 1) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = conversationService.getConversations(pageNo, pageSize);
        }

        return JSON.toJSONStringWithDateFormat(returnMap, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    @RequestMapping("/addMessage.json")
    @ResponseBody
    public String addMessage(HttpServletRequest request, @RequestParam String text, String imageUrl) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(text)) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = conversationService.addMessage(request, text, imageUrl);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/deleteMessage.json")
    @ResponseBody
    public String deleteMessage(@RequestParam Long messageId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (null == messageId) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = conversationService.deleteMessage(messageId);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/addComment.json")
    @ResponseBody
    public String addComment(HttpServletRequest request, @RequestParam String text,
                             @RequestParam String replyTo, @RequestParam Long messageId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(replyTo) || null == messageId) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = conversationService.addComment(request, text, replyTo, messageId);
        }

        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/deleteComment.json")
    @ResponseBody
    public String deleteComment(@RequestParam Long commentId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (null == commentId) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = conversationService.deleteComment(commentId);
        }

        return JSON.toJSONString(returnMap);
    }
}

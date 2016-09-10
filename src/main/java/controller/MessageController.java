package controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.MessageService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zya on 2016/9/9.
 */
@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping("/getAllMessages.json")
    @ResponseBody
    public String getAllMessages() {
        Map<String, Object> returnMap = messageService.getAllMessages();
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
            returnMap = messageService.addMessage(request, text, imageUrl);
        }

        return JSON.toJSONString(returnMap);
    }
}

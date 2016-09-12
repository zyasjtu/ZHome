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

    /**
     * 分页查询消息
     * <p>
     * 接口：
     * /getMessages.json
     * 入参：
     * pageNo                   |Integer|N|页码
     * pageSize                 |Integer|N|每页记录数量
     * 出参：
     * respCode                 |String|N|返回码
     * respMsg                  |String|N|返回消息
     * messages                 |List|O|查询结果
     * <p>
     * 样例：
     * #1
     * {
     * "respCode":"1000",
     * "respMsg":"getMessagesSuccess"
     * "messages": [
     * {
     * "createIp": "99.48.24.36",
     * "createTime": "2016-09-09 15:29:24.000",
     * "creator": "Colin",
     * "id": 1,
     * "imageUrl": "image.jpg",
     * "text": "This is a message",
     * "updateTime": "2016-09-09 15:29:24.000"
     * },
     * {
     * "createIp": "127.0.0.1",
     * "createTime": "2016-09-09 17:19:36.000",
     * "creator": "Colin",
     * "id": 5,
     * "text": "This is another message",
     * "updateTime": "2016-09-09 17:19:36.000"
     * }
     * ]
     * }
     * #2
     * {
     * "respCode": "1001",
     * "respMsg": "invalidParameter"
     * }
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/getMessages.json")
    @ResponseBody
    public String getMessages(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (pageNo < 1 || pageSize < 1) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = messageService.getMessages(pageNo, pageSize);
        }
        return JSON.toJSONStringWithDateFormat(returnMap, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 添加消息
     * <p>
     * 接口：
     * /addMessage.json
     * 入参：
     * text                     |String|N|消息文字内容
     * imgUrl                   |String|O|消息图片链接
     * 出参：
     * respCode                 |String|N|返回码
     * respMsg                  |String|N|返回消息
     * <p>
     * 样例：
     * #1
     * {
     * "respCode":"1000",
     * "respMsg":"addMessageSuccess"
     * }
     * #2
     * {
     * "respCode":"1001",
     * "respMsg":"invalidParameter"
     * }
     *
     * @param text
     * @param imageUrl
     * @return
     */
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

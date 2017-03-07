package controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import service.HttpClientService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Colin on 2017/3/7.
 */

@Controller
public class HttpClientController {
    @Autowired
    HttpClientService httpClientService;

    @RequestMapping("/from.json")
    @ResponseBody
    public String from() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("param1", "1");
        params.put("param2", "2");
        Map<String, Object> returnMap = httpClientService.doPost("http://127.0.0.1/to.json", params);
        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/to.json")
    @ResponseBody
    public String to(@RequestParam String param1, @RequestParam Integer param2) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("param1", param1);
        returnMap.put("param2", param2);
        return JSON.toJSONString(returnMap);
    }
}

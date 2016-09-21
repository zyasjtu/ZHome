package controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import service.FileService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zya on 2016/9/20.
 */
@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping("/uploadFile.json")
    @ResponseBody
    public String uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (null == file) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = fileService.uploadFile(request, file);
        }

        return JSON.toJSONString(returnMap);
    }


    @RequestMapping("/uploadFiles.json")
    @ResponseBody
    public String uploadFiles(HttpServletRequest request, @RequestParam("files") MultipartFile[] files) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (null == files && 0 == files.length) {
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "invalidParameter");
        } else {
            returnMap = fileService.uploadFiles(request, files);
        }

        return JSON.toJSONString(returnMap);
    }


}

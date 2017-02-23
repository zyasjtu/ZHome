package controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import service.FileService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public String uploadFile(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("file") MultipartFile file) {
        Map<String, Object> returnMap = fileService.uploadFile(request, response, file);
        return JSON.toJSONString(returnMap);
    }


    @RequestMapping("/uploadFiles.json")
    @ResponseBody
    public String uploadFiles(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("files") MultipartFile[] files) {
        Map<String, Object> returnMap = fileService.uploadFiles(request, response, files);
        return JSON.toJSONString(returnMap);
    }

    @RequestMapping("/uploadImage.json")
    @ResponseBody
    public String uploadImage(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("file") MultipartFile file) {
        Map<String, Object> returnMap = fileService.uploadImage(request, response, file);
        return JSON.toJSONString(returnMap);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request) {
        if (ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException) {
        }
        return "error.html";
    }
}

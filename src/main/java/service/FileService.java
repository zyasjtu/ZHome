package service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zya on 2016/9/20.
 */
@Service
public class FileService {
    public Map<String, Object> uploadFile(HttpServletRequest request, MultipartFile file) {
        Map<String, Object> returnMap = saveFile(request, file);

        return returnMap;
    }

    public Map<String, Object> uploadFiles(HttpServletRequest request, MultipartFile[] files) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        for (MultipartFile file : files) {
            returnMap = saveFile(request, file);
            if (!"1000".equals(returnMap.get("respCode"))) {
                break;
            }
        }

        return returnMap;
    }

    private Map<String, Object> saveFile(HttpServletRequest request, MultipartFile file) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (!file.isEmpty()) {
            try {
                String filePath = request.getSession().getServletContext().getRealPath("/")
                        + "upload/" + file.getOriginalFilename();
                file.transferTo(new File(filePath));
                returnMap.put("respCode", "1000");
                returnMap.put("respMsg", "uploadSuccess");
            } catch (Exception e) {
                e.printStackTrace();
                returnMap.put("respCode", "1001");
                returnMap.put("respMsg", "uploadFail");
            }
        } else {
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "uploadSuccess");
        }

        return returnMap;
    }
}

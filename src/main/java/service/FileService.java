package service;

import api.FaceApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public Map<String, Object> uploadImage(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        Map<String, Object> saveFileReturnMap = saveFile(request, file);
        if (!"1000".equals(saveFileReturnMap.get("respCode"))) {
            return saveFileReturnMap;
        } else {
            String imagePath = request.getSession().getServletContext().getRealPath("/")
                    + "upload/" + file.getOriginalFilename();
            File image = new File(imagePath);
            byte[] buff = FaceApi.getBytesFromFile(image);

            String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
            HashMap<String, String> map = new HashMap<String, String>();
            HashMap<String, byte[]> byteMap = new HashMap<String, byte[]>();
            map.put("api_key", "key");
            map.put("api_secret", "secret");
            byteMap.put("image_file", buff);

            Map<String, Object> returnMap = new HashMap<String, Object>();
            try {
                byte[] bacd = FaceApi.post(url, map, byteMap);
                String str = new String(bacd);
                System.out.println(str);
                Map<String, Object> faceApiReturnMap = JSON.parseObject(str, Map.class);
                if (StringUtils.isEmpty(faceApiReturnMap.get("error_message"))) {
                    returnMap.put("respCode", "1000");
                    returnMap.put("respMsg", "success");
                    returnMap.put("faces", faceApiReturnMap.get("faces"));

                    JSONArray faces = (JSONArray) faceApiReturnMap.get("faces");
                    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                    Mat img = Highgui.imread(imagePath);
                    for (int i = 0; i < faces.size(); i++) {
                        JSONObject face = faces.getJSONObject(i);
                        Double top = face.getJSONObject("face_rectangle").getDouble("top");
                        Double left = face.getJSONObject("face_rectangle").getDouble("left");
                        Double width = face.getJSONObject("face_rectangle").getDouble("width");
                        Double height = face.getJSONObject("face_rectangle").getDouble("height");
                        Core.rectangle(img, new Point(left, top), new Point(left + width, top + height), new Scalar(0, 0, 255), 2);
                    }
                    Highgui.imwrite(imagePath, img);
                    response.sendRedirect("upload/" + file.getOriginalFilename());
                } else {
                    returnMap.put("respCode", "1001");
                    returnMap.put("respMsg", faceApiReturnMap.get("error_message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return returnMap;
        }
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

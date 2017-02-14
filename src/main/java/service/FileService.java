package service;

import api.FaceApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jni.FaceEngine;
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
    public Map<String, Object> uploadFile(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        Map<String, Object> saveFileReturnMap = saveFile(request, file);
        if (!"1000".equals(saveFileReturnMap.get("respCode"))) {
            return saveFileReturnMap;
        } else {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            try {
                String filePath = request.getSession().getServletContext().getRealPath("/")
                        + "upload/" + file.getOriginalFilename();
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                System.loadLibrary("FaceEngine");
                Mat img = Highgui.imread(filePath);
                int[] facePoints = FaceEngine.detect(img.getNativeObjAddr(),
                        request.getSession().getServletContext().getRealPath("/"));
                for (int i = 0; i < facePoints.length; i += 4) {
                    Point pointTL = new Point((double) facePoints[i], (double) facePoints[i + 1]);
                    Point pointBR = new Point((double) (facePoints[i] + facePoints[i + 2]),
                            (double) (facePoints[i + 1] + facePoints[i + 3]));
                    Core.rectangle(img, pointTL, pointBR, new Scalar(0, 0, 255), 2);
                }
                Highgui.imwrite(filePath, img);
                response.sendRedirect("upload/" + file.getOriginalFilename());
                returnMap.put("respCode", "1000");
                returnMap.put("respMsg", "success");
            } catch (Exception e) {
                e.printStackTrace();
                returnMap.put("respCode", "1001");
                returnMap.put("respMsg", "detect face fail");
            }
            return returnMap;
        }
    }

    public Map<String, Object> uploadFiles(HttpServletRequest request, HttpServletResponse response, MultipartFile[] files) {
        Map<String, Object> saveFileReturnMap = new HashMap<String, Object>();
        for (MultipartFile file : files) {
            saveFileReturnMap = saveFile(request, file);
            if (!"1000".equals(saveFileReturnMap.get("respCode"))) {
                return saveFileReturnMap;
            }
        }

        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            String filePath1 = request.getSession().getServletContext().getRealPath("/")
                    + "upload/" + files[0].getOriginalFilename();
            String filePath2 = request.getSession().getServletContext().getRealPath("/")
                    + "upload/" + files[1].getOriginalFilename();
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("FaceEngine");
            Mat img1 = Highgui.imread(filePath1);
            Mat img2 = Highgui.imread(filePath2);
            double score = FaceEngine.identification(img1.getNativeObjAddr(), img2.getNativeObjAddr(),
                    request.getSession().getServletContext().getRealPath("/"));
            returnMap.put("score", score);
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "success");
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "identify face fail");
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

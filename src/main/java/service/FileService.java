package service;

import api.FaceApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jni.FaceEngine;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static org.opencv.core.CvType.CV_8UC3;

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
                String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
                String fileName = file.getOriginalFilename();
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                System.loadLibrary("FaceEngine");
                Mat img = Highgui.imread(filePath + fileName);
                if (img.empty()) {
                    returnMap.put("respCode", "1001");
                    returnMap.put("respMsg", "imageIsEmpty");
                    return returnMap;
                }
                int[] facePoints = FaceEngine.detect(img.getNativeObjAddr(),
                        request.getSession().getServletContext().getRealPath("/"));
                for (int i = 0; i < facePoints.length; i += 4) {
                    Point pointTL = new Point((double) facePoints[i], (double) facePoints[i + 1]);
                    Point pointBR = new Point((double) (facePoints[i] + facePoints[i + 2]),
                            (double) (facePoints[i + 1] + facePoints[i + 3]));
                    Core.rectangle(img, pointTL, pointBR, new Scalar(0, 0, 255), 2);
                }
                img = resizeImageToSquare(img);
                Highgui.imwrite(filePath + request.getSession().getId() + fileName, img);
                response.sendRedirect("upload/" + request.getSession().getId() + fileName);
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
            String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
            String fileName1 = files[0].getOriginalFilename();
            String fileName2 = files[1].getOriginalFilename();
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("FaceEngine");
            Mat img1 = Highgui.imread(filePath + fileName1);
            Mat img2 = Highgui.imread(filePath + fileName2);
            if (img1.empty() || img2.empty()) {
                returnMap.put("respCode", "1001");
                returnMap.put("respMsg", "imageIsEmpty");
                return returnMap;
            }
            double facePoints[] = FaceEngine.identification(img1.getNativeObjAddr(), img2.getNativeObjAddr(),
                    request.getSession().getServletContext().getRealPath("/"));

            if (facePoints.length == 29) {
                for (int i = 0; i < 5; i++) {
                    Point point = new Point(facePoints[2 * i], facePoints[2 * i + 1]);
                    Core.circle(img1, point, 2, new Scalar(0, 255, 0), 2);
                }
                for (int i = 0; i < 5; i++) {
                    Point point = new Point(facePoints[2 * i + 10], facePoints[2 * i + 11]);
                    Core.circle(img2, point, 2, new Scalar(0, 255, 0), 2);
                }
                Point pointTL1 = new Point(facePoints[20], facePoints[21]);
                Point pointBR1 = new Point((facePoints[20] + facePoints[22]), (facePoints[21] + facePoints[23]));
                Core.rectangle(img1, pointTL1, pointBR1, new Scalar(0, 0, 255), 2);
                Point pointTL2 = new Point(facePoints[24], facePoints[25]);
                Point pointBR2 = new Point((facePoints[24] + facePoints[26]), (facePoints[25] + facePoints[27]));
                Core.rectangle(img2, pointTL2, pointBR2, new Scalar(0, 0, 255), 2);

                Mat img = mergeImages(img1, img2, facePoints[28]);
                Highgui.imwrite(filePath + request.getSession().getId() + fileName1, img);
                response.sendRedirect("upload/" + request.getSession().getId() + fileName1);
                returnMap.put("score", facePoints[28]);
                returnMap.put("respCode", "1000");
                returnMap.put("respMsg", "success");
            } else {
                returnMap.put("score", facePoints[0]);
                returnMap.put("respCode", "1000");
                returnMap.put("respMsg", "noFaceDetected");
            }
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

    private Mat resizeImageToSquare(Mat img) {
        int width = img.width();
        int height = img.height();
        Rect roi = new Rect(0, 0, width, height);

        if (width == height) {
            Mat dst = new Mat(width, height, CV_8UC3, new Scalar(255, 255, 255));
            img.copyTo(dst);
            return dst;
        } else if (width > height) {
            Mat dst = new Mat(width, width, CV_8UC3, new Scalar(255, 255, 255));
            img.copyTo(dst.submat(roi));
            return dst;
        } else {
            Mat dst = new Mat(height, height, CV_8UC3, new Scalar(255, 255, 255));
            img.copyTo(dst.submat(roi));
            return dst;
        }
    }

    private Mat mergeImages(Mat src1, Mat src2, double score) {
        int width1 = src1.width();
        int height1 = src1.height();
        int width2 = src2.width();
        int height2 = src2.height();

        Mat dst = new Mat(height1 > height2 ? height1 : height2, width1 + width2, CV_8UC3, new Scalar(255, 255, 255));
        Rect roi = new Rect(0, 0, width1, height1);
        src1.copyTo(dst.submat(roi));
        roi = new Rect(width1, 0, width2, height2);
        src2.copyTo(dst.submat(roi));

        DecimalFormat df = new DecimalFormat("######0.00");
        String text = String.valueOf(df.format(score * 100)) + "%";
        Core.putText(dst, text, new Point(width1, dst.height()), 0, 1.5, new Scalar(0, 0, 255), 2);

        return dst;
    }
}

package service;

import jni.FaceEngine;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.springframework.stereotype.Service;
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
        String path = request.getSession().getServletContext().getRealPath("/") + "upload/" +
                request.getSession().getId() + file.getOriginalFilename();

        Map<String, Object> saveFileReturnMap = saveFile(path, file);
        if (!"1000".equals(saveFileReturnMap.get("respCode"))) {
            return saveFileReturnMap;
        } else {
            Map<String, Object> returnMap = new HashMap<String, Object>();
            try {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                System.loadLibrary("FaceEngine");
                Mat img = Highgui.imread(path);
                if (img.empty()) {
                    returnMap.put("respCode", "1001");
                    returnMap.put("respMsg", "imageIsEmpty");
                    return returnMap;
                }
                int[] facePoints = FaceEngine.detect(img.getNativeObjAddr(), "");
                for (int i = 0; i < facePoints.length; i += 4) {
                    Point pointTL = new Point((double) facePoints[i], (double) facePoints[i + 1]);
                    Point pointBR = new Point((double) (facePoints[i] + facePoints[i + 2]),
                            (double) (facePoints[i + 1] + facePoints[i + 3]));
                    Core.rectangle(img, pointTL, pointBR, new Scalar(0, 0, 255), 2);
                }
                img = resizeImageToSquare(img);
                Highgui.imwrite(path, img);
                response.sendRedirect("upload/" + request.getSession().getId() + file.getOriginalFilename());
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
        String path = request.getSession().getServletContext().getRealPath("/") + "upload/" + request.getSession().getId();
        Map<String, Object> saveFileReturnMap = new HashMap<String, Object>();
        for (MultipartFile file : files) {
            saveFileReturnMap = saveFile(path + file.getOriginalFilename(), file);
            if (!"1000".equals(saveFileReturnMap.get("respCode"))) {
                return saveFileReturnMap;
            }
        }

        Map<String, Object> returnMap = new HashMap<String, Object>();
        try {
            String path1 = path + files[0].getOriginalFilename();
            String path2 = path + files[1].getOriginalFilename();

            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("FaceEngine");
            Mat img1 = Highgui.imread(path1);
            Mat img2 = Highgui.imread(path2);
            if (img1.empty() || img2.empty()) {
                returnMap.put("respCode", "1001");
                returnMap.put("respMsg", "imageIsEmpty");
                return returnMap;
            }
            double facePoints[] = FaceEngine.identification(img1.getNativeObjAddr(), img2.getNativeObjAddr(), "");

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
                DecimalFormat df = new DecimalFormat("######0.0");
                String text = String.valueOf(df.format(facePoints[28] * 100)) + "%";
                Core.putText(img1, text, new Point(pointTL1.x, pointBR1.y - 3), 0, 1, new Scalar(0, 0, 255), 2);
                Core.putText(img2, text, new Point(pointTL2.x, pointBR2.y - 3), 0, 1, new Scalar(0, 0, 255), 2);

                Mat img = mergeImages(img1, img2);
                Highgui.imwrite(path1, img);
                response.sendRedirect("upload/" + request.getSession().getId() + files[0].getOriginalFilename());
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

    private Map<String, Object> saveFile(String path, MultipartFile file) {
        Map<String, Object> returnMap = new HashMap<String, Object>();

        if (!file.isEmpty()) {
            try {
                file.transferTo(new File(path));
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

    private Mat mergeImages(Mat src1, Mat src2) {
        int width1 = src1.width();
        int height1 = src1.height();
        int width2 = src2.width();
        int height2 = src2.height();

        Mat dst = new Mat(height1 > height2 ? height1 : height2, width1 + width2, CV_8UC3, new Scalar(255, 255, 255));
        Rect roi = new Rect(0, 0, width1, height1);
        src1.copyTo(dst.submat(roi));
        roi = new Rect(width1, 0, width2, height2);
        src2.copyTo(dst.submat(roi));

        return dst;
    }
}

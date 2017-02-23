package service;

import jni.FaceEngine;
import jni.VehicleEngine;
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
import java.io.IOException;
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
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String bathPath = request.getSession().getServletContext().getRealPath("/");
        String fileName = request.getSession().getId() + file.getOriginalFilename();
        String fullPath = bathPath + "upload/" + fileName;
        try {
            saveFile(fullPath, file);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("FaceEngine");
            Mat img = Highgui.imread(fullPath);
            if (img.empty()) {
                img = Highgui.imread(bathPath + "static/faceDetect.jpg");
                fileName += "faceDetect.jpg";
                fullPath += "faceDetect.jpg";
            }
            int[] facePoints = FaceEngine.detect(img.getNativeObjAddr(), "");
            for (int i = 0; i < facePoints.length; i += 4) {
                Point pointTL = new Point((double) facePoints[i], (double) facePoints[i + 1]);
                Point pointBR = new Point((double) (facePoints[i] + facePoints[i + 2]),
                        (double) (facePoints[i + 1] + facePoints[i + 3]));
                Core.rectangle(img, pointTL, pointBR, new Scalar(0, 0, 255), 2);
            }
            //img = resizeImageToSquare(img);
            Highgui.imwrite(fullPath, img);
            response.sendRedirect("upload/" + fileName);
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "success");
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "detectFaceFail");
        }
        return returnMap;
    }

    public Map<String, Object> uploadFiles(HttpServletRequest request, HttpServletResponse response, MultipartFile[] files) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String bathPath = request.getSession().getServletContext().getRealPath("/");
        String fileName1 = request.getSession().getId() + (files.length == 2 ? files[0].getOriginalFilename() : "");
        String fileName2 = request.getSession().getId() + (files.length == 2 ? files[1].getOriginalFilename() : "");
        String fullPath1 = bathPath + "upload/" + fileName1;
        String fullPath2 = bathPath + "upload/" + fileName2;
        try {
            if (files.length == 2) {
                saveFile(fullPath1, files[0]);
                saveFile(fullPath2, files[1]);
            }
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("FaceEngine");
            Mat img1 = Highgui.imread(fullPath1);
            Mat img2 = Highgui.imread(fullPath2);
            if (img1.empty() || img2.empty()) {
                img1 = Highgui.imread(bathPath + "static/faceVerify1.jpg");
                fileName1 += "faceVerify1.jpg";
                fullPath1 += "faceVerify1.jpg";
                img2 = Highgui.imread(bathPath + "static/faceVerify2.jpg");
                fileName2 += "faceVerify2.jpg";
                fullPath2 += "faceVerify2.jpg";
            }
            double[] facePoints = FaceEngine.identification(img1.getNativeObjAddr(), img2.getNativeObjAddr(), "");

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
                Highgui.imwrite(fullPath1, img);
                response.sendRedirect("upload/" + fileName1);
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
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String bathPath = request.getSession().getServletContext().getRealPath("/");
        String fileName = request.getSession().getId() + file.getOriginalFilename();
        String fullPath = bathPath + "upload/" + fileName;

        try {
            saveFile(fullPath, file);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("VehicleEngine");
            Mat img = Highgui.imread(fullPath);
            if (img.empty()) {
                img = Highgui.imread(bathPath + "static/plateDetect.jpg");
                fileName += "plateDetect.jpg";
                fullPath += "plateDetect.jpg";
            }
            double[] vehiclePoints = VehicleEngine.detectPlate(img.getNativeObjAddr());
            for (int i = 0; i < vehiclePoints.length; i += 4) {
                Point pointTL = new Point(vehiclePoints[i], vehiclePoints[i + 1]);
                Point pointBR = new Point(vehiclePoints[i] + vehiclePoints[i + 2], vehiclePoints[i + 1] + vehiclePoints[i + 3]);
                Core.rectangle(img, pointTL, pointBR, new Scalar(0, 0, 255), 2);
            }
            //img = resizeImageToSquare(img);
            Highgui.imwrite(fullPath, img);
            response.sendRedirect("upload/" + fileName);
            returnMap.put("respCode", "1000");
            returnMap.put("respMsg", "success");
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("respCode", "1001");
            returnMap.put("respMsg", "detectVehicle/PlateFail");
        }
        return returnMap;
    }

    private void saveFile(String path, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            file.transferTo(new File(path));
        }
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

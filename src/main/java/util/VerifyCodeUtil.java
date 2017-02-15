package util;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Colin on 2017/2/10.
 */

public class VerifyCodeUtil {
    public static String generateVerifyCode(HttpServletRequest request) throws IOException {
        int random = (int) (Math.random() * 10000);
        String verifyCode = StringUtils.leftPad(String.valueOf(random), 4, "0");
        //  表示一个图像，它具有合成整数像素的 8 位 RGB 颜色分量。
        BufferedImage img = new BufferedImage(30, 15, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        //使用此图形上下文的当前字体和颜色绘制由指定 string 给定的文本。最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处。
        g.drawString(verifyCode, 1, 12);
        //类似于流中的close()带动flush()---把数据刷到img对象当中
        g.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象。
        //使用支持给定格式的任意 ImageWriter 将一个图像写入 File。

        ImageIO.write(img, "JPG", new File(request.getSession().getServletContext().getRealPath("/") + "static/verifyCode.jpg"));

        return verifyCode;
    }

//    public static void main(String argv[]) {
//        try {
//            generateVerifyCode(new MockHttpServletRequest());
//        } catch (Exception e) {
//
//        }
//    }
}
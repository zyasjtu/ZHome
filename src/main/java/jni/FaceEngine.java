package jni;

/**
 * Created by Colin on 2017/2/9.
 */
public class FaceEngine {
    public static native int[] detect(long srcAddr, String modelPath);

    public static native double[] identification(long srcAddr1, long srcAddr2, String modelPath);
}

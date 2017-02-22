package jni;

/**
 * Created by Colin on 2017/2/22.
 */
public class VehicleEngine {
    public static native double[] detectVehicle(long srcAddr);

    public static native double[] detectPlate(long srcAddr);
}

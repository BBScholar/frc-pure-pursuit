package org.frc2018;

public class Utils {
    /**
     * 
     * @param ips inches per second
     * @return
     */
    public static double inchesPerSecondToEncoderTicksPer100Ms(double ips) {
        return inchesToEncoderTicks(ips) / 10.0;
    }

    /**
     * 
     * @param ticks_per_100ms
     * @return
     */
    public static double encoderTicksPer100MsToInchesPerSecond(int ticks_per_100ms){
        return encoderTicksToInches(ticks_per_100ms) * 10.0;
    }

    /**
     * 
     * @param inches
     * @return
     */
    public static int inchesToEncoderTicks(double inches) {
        return (int) ((Constants.ENCODERS_TICKS_PER_ROTATION / Constants.WHEEL_CIR) * inches);
    }

    /**
     * 
     * @param ticks
     * @return
     */
    public static double encoderTicksToInches(int ticks) {
        return (Constants.WHEEL_CIR / Constants.ENCODERS_TICKS_PER_ROTATION) * ticks;
    }

    /**
     * 
     * @param ticks
     * @return
     */
    public static double talonAngleToDegrees(double ticks) {
        return (360.0 / Constants.TURN_UNITS_PER_ROTATION) * ticks;
    }

    /**
     * 
     * @param angle
     * @return
     */
    public static int degreesToTalonAngle(double angle) {
        return (int) ((Constants.TURN_UNITS_PER_ROTATION / 360.0) * angle);
    }
}
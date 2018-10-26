package org.frc2018;

public class Constants {


    public static final int LEFT_MASTER_PORT = 2;
    public static final int LEFT_SLAVE_PORT = 1;
    public static final int RIGHT_MASTER_PORT = 8;
    public static final int RIGHT_SLAVE_PORT = 9;

    public static final int ARM_PORT = 5;

    public static final int GYRO_PORT = 10;

    public static final double MAX_SETPOINT = 120.0; // inches per second

    public static final double WHEEL_DIAMETER = 6.0;
    public static final int ENCODERS_TICKS_PER_ROTATION = 1024;
    public static final double WHEEL_CIR = WHEEL_DIAMETER * Math.PI;
    public static final int TALON_UPDATE_PERIOD_MS = 5;
    public static final double TRACK_WIDTH = 20.0; // inches

    public static final double OPEN_LOOP_ARM_MULT = 0.4;
    
    public static final double EPSILON = 1E-10;

    // pid constants
    public static final double CLOSED_LOOP_RAMP = 0.0;

    public static final double VEL_kP = 3.5;
    public static final double VEL_kI = 0.0;
    public static final double VEL_kD = 0.5;
    public static final double VEL_kF = 0.15;
    public static final int    VEL_IZONE = 10;


    public static final double POS_kP = 1.0;
    public static final double POS_kI = 0.0;
    public static final double POS_kD = 0.6;
    public static final double POS_kF = 0.0;
    public static final int    POS_IZONE = 10;
    public static final int    POS_MAX_VELO = 10000; // encoder ticks per 100ms
    public static final int    POS_MAX_ACCEL = 1000; // encoder ticks per 100ms

    public static final double LOOK_AHEAD_DISTANCE = 24.0; // inches
    public static final double STOP_STEERING_DISTANCE = 6.0; // inches

    public static final double ARM_kP = 0.1;
    public static final double ARM_kI = 0.0;
    public static final double ARM_kD = 0.01;
    public static final double ARM_kF = 0.0;

}
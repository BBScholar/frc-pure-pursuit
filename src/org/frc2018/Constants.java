package org.frc2018;

public class Constants {


    public static final int LEFT_MASTER_PORT = 2;
    public static final int LEFT_SLAVE_PORT = 1;
    public static final int RIGHT_MASTER_PORT = 8;
    public static final int RIGHT_SLAVE_PORT = 9;

    public static final int ARM_PORT = 5;

    public static final int GYRO_PORT = 10;

    public static final double MAX_SETPOINT = 5 * 10.0; // 12 ft per second

    public static final double WHEEL_DIAMETER = 6.0;
    public static final int ENCODERS_TICKS_PER_ROTATION = 1028;
    public static final double WHEEL_CIR = WHEEL_DIAMETER * Math.PI;
    public static final int TALON_UPDATE_PERIOD_MS = 5;

    // pid constants
    public static final double CLOSED_LOOP_RAMP = 1.0;

    public static final double VEL_kP = 5.0;
    public static final double VEL_kI = 0.00;
    public static final double VEL_kD = 0.01;
    public static final double VEL_kF = 0.0;
    public static final int    VEL_IZONE = 10;


    public static final double POS_kP = 1.0;
    public static final double POS_kI = 0.0;
    public static final double POS_kD = 0.05;
    public static final double POS_kF = 0.0;
    public static final int    POS_IZONE = 10;
    public static final int    POS_MAX_VELO = 10000; // encoder ticks per 100ms
    public static final int    POS_MAX_ACCEL = 1000; // encoder ticks per 100ms

     // Wheels
     public static double kTrackWidthInches = 22.0;
     public static double kTrackScrubFactor = 0.95;

     public static double kMinLookAhead = 12.0; // inches
     public static double kMinLookAheadSpeed = 9.0; // inches per second
     public static double kMaxLookAhead = 24.0; // inches
     public static double kMaxLookAheadSpeed = 120.0; // inches per second
     public static double kDeltaLookAhead = kMaxLookAhead - kMinLookAhead;
     public static double kDeltaLookAheadSpeed = kMaxLookAheadSpeed - kMinLookAheadSpeed;
 
     public static double kInertiaSteeringGain = 0.0; // angular velocity command is multiplied by this gain *
                                                      // our speed
                                                      // in inches per sec
     public static double kSegmentCompletionTolerance = 0.1; // inches
     public static double kPathFollowingMaxAccel = 120.0;    // inches per second^2
     public static double kPathFollowingMaxVel = 120.0;      // inches per second
     public static double kPathFollowingProfileKp = 5.00;
     public static double kPathFollowingProfileKi = 0.03;
     public static double kPathFollowingProfileKv = 0.02;
     public static double kPathFollowingProfileKffv = 1.0;
     public static double kPathFollowingProfileKffa = 0.05;
     public static double kPathFollowingGoalPosTolerance = 0.75;
     public static double kPathFollowingGoalVelTolerance = 12.0;
     public static double kPathStopSteeringDistance = 9.0;

    

}
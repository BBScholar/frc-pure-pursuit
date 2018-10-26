package org.frc2018;

public class Constants { // temp, in place of dashboard

    public static final double      UPDATE_PERIOD               = 0.005; // in seconds

    public static final int         DRIVER_PORT                 = 0;
    public static final int         CODRIVER_PORT               = 1;
    public static final double      XBOX_TURN_MULTIPLIER        = 0.4;

    public static final int         LEFT_MASTER_PORT            = 2;
    public static final int         LEFT_SLAVE_PORT             = 1;
    public static final int         RIGHT_MASTER_PORT           = 8;
    public static final int         RIGHT_SLAVE_PORT            = 9;

    public static final int         ARM_PORT                    = 5;
    public static final int         LEFT_INTAKE_PORT            = 4;
    public static final int         RIGHT_INTAKE_PORT           = 6;

    public static final int         GYRO_PORT                   = 10;
    public static final double      PIGEON_UNITS_PER_ROTATION   = 8192; // raw pigeon units
    public static final double      TURN_UNITS_PER_ROTATION     = 3600; // for accuracy of ±0.1º

    public static final double      WHEEL_DIAMETER              = 6.0; // inches
    public static final int         ENCODERS_TICKS_PER_ROTATION = 1024;
    public static final double      WHEEL_CIR                   = WHEEL_DIAMETER * Math.PI;
    public static final double      TRACK_WIDTH                 = 20.0; // inches

    public static final double      XBOX_DEADBAND               = 0.1;

    public static final double      MAX_ARM_SPEED               = 0.7;
    public static final boolean     INVERT_ARM                  = false;
    public static final boolean     INVERT_INTAKE               = false;
    public static final double      INTAKE_SPEED                = -0.7;
    public static final double      INTAKE_HOLD_SPEED           = -0.3;
    public static final double      INTAKE_SPIT_SPEED           = 1.0;
    public static final double      INTAKE_DROP_SPEED           = 0.3;

    public static final int         TALON_UPDATE_PERIOD_MS      = 10;
    public static final int         TALON_PIDF_LOOP_PERIOD_MS   = 1;
    public static final boolean     INVERT_ANGLE_AUX_PIDF       = false;
    public static final boolean     INVERT_TURN_PIDF            = false;
    public static final boolean     INVERT_FIXED_AUX_PIDF       = false;

    public static final double      MAX_VELOCITY_SETPOINT       = 120.0; // inches per second
    
    public static final double      EPSILON                     = 1E-10;

    // pid constants
    public static final double      CLOSED_LOOP_RAMP            = 0.0;

    public static final double      VEL_kP                      = 3.5;
    public static final double      VEL_kI                      = 0.0;
    public static final double      VEL_kD                      = 0.5;
    public static final double      VEL_kF                      = 0.15;
    public static final int         VEL_IZONE                   = 10;
    public static final double      VEL_MAX_OUTPUT              = 1.0;


    public static final double      POS_kP                      = 1.0;
    public static final double      POS_kI                      = 0.0;
    public static final double      POS_kD                      = 0.6;
    public static final double      POS_kF                      = 0.0;
    public static final int         POS_IZONE                   = 10;
    public static final double      POS_MAX_OUTPUT              = 0.5;
    public static final double      ANGLE_kP                    = 0;
    public static final double      ANGLE_kI                    = 0;
    public static final double      ANGLE_kD                    = 0;
    public static final double      ANGLE_kF                    = 0;
    public static final int         ANGLE_IZONE                 = 10;
    public static final double      ANGLE_MAX_OUTPUT            = 1.0;

    public static final double      TURN_kP                     = 1.0;
    public static final double      TURN_kI                     = 0.0;
    public static final double      TURN_kD                     = 0.6;
    public static final double      TURN_kF                     = 0.0;
    public static final int         TURN_IZONE                  = 10;
    public static final double      TURN_MAX_OUTPUT             = 1.0;
    public static final double      FIXED_kP                    = 0.0;
    public static final double      FIXED_kI                    = 0.0;
    public static final double      FIXED_kD                    = 0.0;
    public static final double      FIXED_kF                    = 0.0;
    public static final int         FIXED_IZONE                 = 10;
    public static final double      FIXED_MAX_OUTPUT            = 0.5;


    public static final double LOOK_AHEAD_DISTANCE              = 24.0; // inches

}
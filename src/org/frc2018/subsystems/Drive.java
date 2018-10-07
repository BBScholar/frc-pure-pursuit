package org.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import org.frc2018.Constants;

public class Drive implements Subsystem {

    private static Drive m_instance = new Drive();

    /**
     * 
     * @return
     */
    public static Drive getInstance() {
        return m_instance;
    }

    /**
     * 
     */
    public enum DriveMode {
        OPEN_LOOP,
        TURN_TO_HEADING,
        DRIVE_STRAIGHT,
        VELOCITY_SETPOINT,
        FOLLOW_PATH,
    }

    /**
     * 
     * @param mode
     * @return
     */
    protected static boolean usesVelocityControl(DriveMode mode) {
        switch(mode) {
            case FOLLOW_PATH:
            case VELOCITY_SETPOINT:
                return true;
            default:
                return false;

        }
    }

    /**
     * 
     * @param mode
     * @return
     */
    protected static boolean usesPositionControl(DriveMode mode) {
        switch(mode) {
            case FOLLOW_PATH:
            case VELOCITY_SETPOINT:
            case OPEN_LOOP:
                return false;
            default:
                return true;

        }
    }

    private TalonSRX m_left_master, m_left_slave;
    private TalonSRX m_right_master, m_right_slave;

    private PigeonIMU m_gyro;

    private DriveMode m_mode;

    private final int POSITION_CONTROL_SLOT = 0;
    private final int VELOCITY_CONTROL_SLOT = 1;

    private boolean mIsBrakeMode = false;
    private boolean mIsOnTarget = false;
    private boolean mIsApproaching = false;

    private Drive() {
        m_left_master = new TalonSRX(Constants.LEFT_MASTER_PORT);
        m_left_slave = new TalonSRX(Constants.LEFT_SLAVE_PORT);

        m_right_master = new TalonSRX(Constants.RIGHT_MASTER_PORT);
        m_right_slave = new TalonSRX(Constants.RIGHT_SLAVE_PORT);

        m_left_master.setSensorPhase(false);

        m_left_slave.follow(m_left_master);
        m_right_slave.follow(m_right_master);

        m_right_master.setInverted(true);
        m_right_slave.setInverted(true);

        m_left_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

        m_left_master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature,
            Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature,
            Constants.TALON_UPDATE_PERIOD_MS, 0);

        m_gyro = new PigeonIMU(Constants.GYRO_PORT);

        m_mode = DriveMode.OPEN_LOOP;

        mIsBrakeMode = true;
        setBrakeMode(false);

        reloadGains();
        setOpenLoop(0, 0);
    }

    @Override
    public void update() {
        
        switch(m_mode) {
            case OPEN_LOOP:
                return;
            case VELOCITY_SETPOINT:
                return;
            case FOLLOW_PATH:
                // something here later
                return;
            case TURN_TO_HEADING:
                // something here
                return;
            case DRIVE_STRAIGHT:
            
                return;
        };
    }

    // Brake Mode stuff

    /**
     * Sets talon brake mode.
     * @param on true if want brake mode to be on, false if want brake mode to be off.
     */
    public void setBrakeMode(boolean on) {
        if(mIsBrakeMode == on)
            return;

        if(on) {
            m_left_master.setNeutralMode(NeutralMode.Brake);
            m_left_slave.setNeutralMode(NeutralMode.Brake);
            m_right_master.setNeutralMode(NeutralMode.Brake);
            m_right_slave.setNeutralMode(NeutralMode.Brake);
        } else {
            m_left_master.setNeutralMode(NeutralMode.Coast);
            m_left_slave.setNeutralMode(NeutralMode.Coast);
            m_right_master.setNeutralMode(NeutralMode.Coast);
            m_right_slave.setNeutralMode(NeutralMode.Coast);
        }
        mIsBrakeMode = on;
    }

    /**
     * 
     * @return boolean representing brake mode status of drivetrain. (True = on, False = off)
     */
    public boolean isBrakeMode() {
        return mIsBrakeMode;
    }

    // Open Loop Stuff

    /**
     * 
     * @param left
     * @param right
     */
    public void setOpenLoop(double left, double right) {
        if(m_mode != DriveMode.OPEN_LOOP) {
            m_left_master.configNominalOutputForward(0.0, 0);
            m_left_master.configNominalOutputReverse(0.0, 0);
            m_right_master.configNominalOutputForward(0.0, 0);
            m_right_master.configNominalOutputReverse(0.0, 0);
            setBrakeMode(false);  
            m_mode = DriveMode.OPEN_LOOP;  
        }
        m_left_master.set(ControlMode.PercentOutput, left);
        m_right_master.set(ControlMode.PercentOutput, right);

    }

    // velocity control stuff

    /**
     * 
     * @param left_inches_per_sec
     * @param right_inches_per_sec
     */
    public void setVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
        configureTalonsForSpeedControl();
        m_mode = DriveMode.VELOCITY_SETPOINT;
        updateVelocitySetpoint(left_inches_per_sec, right_inches_per_sec);
    }

    /**
     * 
     */
    private void configureTalonsForSpeedControl() {
        if(!usesVelocityControl(m_mode)) {
            setBrakeMode(true);
            m_left_master.selectProfileSlot(VELOCITY_CONTROL_SLOT, 0);
            m_right_master.selectProfileSlot(VELOCITY_CONTROL_SLOT, 0);
        }
    }

    /**
     * 
     * @param left_inches_per_sec
     * @param right_inches_per_sec
     */
    private void updateVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
        if(usesVelocityControl(m_mode)) {
            final double max_desired = Math.max(Math.abs(left_inches_per_sec), Math.abs(right_inches_per_sec));
            final double scale = max_desired > Constants.MAX_SETPOINT
                    ? Constants.MAX_SETPOINT / max_desired : 1.0;
            m_left_master.set(ControlMode.Velocity, scale * inchesPerSecondToEncoderTicksPer100Ms(left_inches_per_sec));
            m_right_master.set(ControlMode.Velocity, scale * inchesPerSecondToEncoderTicksPer100Ms(right_inches_per_sec));
        } else {
            System.out.println("Hit a bad velocity control state");
            m_left_master.set(ControlMode.Velocity, 0);
            m_right_master.set(ControlMode.Velocity, 0);
        }
    }

    // position control stuff

    /**
     * 
     * @param left_inches
     * @param right_inches
     */
    public void setPositionSetpoint(double left_inches, double right_inches) {
        configureTalonsForPositionControl();
        updatePositionSetpoint(left_inches, right_inches);
    }

    /**
     * 
     */
    private void configureTalonsForPositionControl() {
        if(!usesPositionControl(m_mode)) {
            setBrakeMode(true);
            m_left_master.selectProfileSlot(POSITION_CONTROL_SLOT, 0);
            m_right_master.selectProfileSlot(POSITION_CONTROL_SLOT, 0);
        }
    }

    /**
     * 
     * @param left_position_inches
     * @param right_position_inches
     */
    private void updatePositionSetpoint(double left_position_inches, double right_position_inches) {
        if(usesPositionControl(m_mode)) {
            m_left_master.set(ControlMode.MotionMagic, inchesToEncoderTicks(left_position_inches));
            m_right_master.set(ControlMode.MotionMagic, inchesToEncoderTicks(right_position_inches));
        } else {
            System.out.println("Hit a bad position control state");
            m_left_master.set(ControlMode.MotionMagic, 0);
            m_right_master.set(ControlMode.MotionMagic, 0);
        }
    }

    // encoder stuff

    /**
     * 
     * @param ips
     * @return
     */
    private static double inchesPerSecondToEncoderTicksPer100Ms(double ips) {
        return inchesToEncoderTicks(ips) / 10.0;
    }

    /**
     * 
     * @param ticks_per_100ms
     * @return
     */
    private static double encoderTicksPer100MsToInchesPerSecond(int ticks_per_100ms){
        return encoderTicksToInches(ticks_per_100ms) * 10.0;
    }

    /**
     * 
     * @param inches
     * @return
     */
    private static int inchesToEncoderTicks(double inches) {
        return (int) ((Constants.ENCODERS_TICKS_PER_ROTATION / Constants.WHEEL_CIR) * inches);
    }

    /**
     * 
     * @param ticks
     * @return
     */
    private static double encoderTicksToInches(int ticks) {
        return (Constants.WHEEL_CIR / Constants.ENCODERS_TICKS_PER_ROTATION) * ticks;
    }

    /**
     * 
     * @return
     */
    public  int getLeftDistanceRaw() {
        return m_left_master.getSelectedSensorPosition(0);
    }

    /**
     * 
     * @return
     */
    public double getLeftDistanceInches() {
        return encoderTicksToInches(getLeftDistanceRaw());
    }

    /**
     * 
     * @return
     */
    public int getLeftVelocityRaw() {
        return m_left_master.getSelectedSensorVelocity(0);
    }

    /**
     * 
     * @return
     */
    public double getLeftVelocityInchesPerSecond() {
        return encoderTicksPer100MsToInchesPerSecond(getLeftVelocityRaw());
    }

    /**
     * 
     * @param inches
     */
    public void setLeftDistanceInches(double inches) {
        setLeftDistanceRaw(inchesToEncoderTicks(inches));
    }

    /**
     * 
     * @param ticks
     */
    public void setLeftDistanceRaw(int ticks) {
        m_left_master.setSelectedSensorPosition(ticks, 0, 0);
    }

    /**
     * 
     * @return
     */
    public int getRightDistanceRaw() {
        return -m_right_master.getSelectedSensorPosition(0);
    }

    /**
     * 
     * @return distance of right encoder in inches
     */
    public double getRightDistanceInches() {
        return encoderTicksToInches(getRightDistanceRaw());
    }

    /**
     * 
     * @return velocity of right side of drivetrain in encoder ticks per 100 milli-seconds
     */
    public int getRightVelocityRaw() {
        return m_right_master.getSelectedSensorVelocity(0);
    }

    /**
     * 
     * @return velocity of right side of drivetrain in inches per second
     */
    public double getRightVelocityInchesPerSecond() {
        return encoderTicksPer100MsToInchesPerSecond(getRightVelocityRaw());
    }

    /**
     * 
     * @param inches right encoder distance to set in inches
     */
    public void setRightDistanceInches(double inches) {
        setRightDistanceRaw(inchesToEncoderTicks(inches));
     }

    /**
     * 
     * @param ticks right encoder distance to set in encoder ticks
     */
    public void setRightDistanceRaw(int ticks) {
        m_right_master.setSelectedSensorPosition(-ticks, 0, 0);
    }

    // gyro stuff

    /**
     * 
     * @return robot heading in degrees
     */
    public double getGyroAngle() {
        return m_gyro.getAbsoluteCompassHeading();
    }

    /**
     * 
     * @param angle angle in degrees to be sent to talon register
     */
    public void setGyroAngle(double angle) {
        m_gyro.setCompassAngle(angle, 0);
        m_gyro.setYawToCompass(0);
    } 

    // target stuff
    public boolean getIsOnTarget() {
        return mIsOnTarget;
    }

    public boolean getIsApproaching() {
        return mIsApproaching;
    }

    /**
     * 
     * @return robot angular velocity in degrees per second
     */
    public double getGyroAngularVelocity() {
        double[] xyz = new double[3];
        m_gyro.getRawGyro(xyz);
        return xyz[1];
    }

    // pid stuff

    /**
     * 
     */
    public void reloadGains() {
        // left position gains
        m_left_master.selectProfileSlot(POSITION_CONTROL_SLOT, 0);
        m_left_master.config_kP(0, Constants.POS_kP, 0);
        m_left_master.config_kI(0, Constants.POS_kI, 0);
        m_left_master.config_kD(0, Constants.POS_kD, 0);
        m_left_master.config_kF(0, Constants.POS_kF, 0);
        m_left_master.config_IntegralZone(POSITION_CONTROL_SLOT, Constants.POS_IZONE, 0);
        m_left_master.configClosedloopRamp(Constants.CLOSED_LOOP_RAMP, 0);
        m_left_master.configMotionAcceleration(Constants.POS_MAX_ACCEL, 0);
        m_left_master.configMotionCruiseVelocity(Constants.POS_MAX_VELO, 0);

        // right position gains
        m_right_master.selectProfileSlot(POSITION_CONTROL_SLOT, 0);
        m_right_master.config_kP(0, Constants.POS_kP, 0);
        m_right_master.config_kP(0, Constants.POS_kI, 0);
        m_right_master.config_kP(0, Constants.POS_kD, 0);
        m_right_master.config_kP(0, Constants.POS_kF, 0);
        m_right_master.config_IntegralZone(POSITION_CONTROL_SLOT, Constants.POS_IZONE, 0);
        m_right_master.configClosedloopRamp(Constants.CLOSED_LOOP_RAMP, 0);
        m_right_master.configMotionAcceleration(Constants.POS_MAX_ACCEL, 0);
        m_right_master.configMotionCruiseVelocity(Constants.POS_MAX_VELO, 0);

        // left velocity gains
        m_left_master.selectProfileSlot(VELOCITY_CONTROL_SLOT, 0);
        m_left_master.config_kP(0, Constants.VEL_kP, 0);
        m_left_master.config_kI(0, Constants.VEL_kI, 0);
        m_left_master.config_kD(0, Constants.VEL_kD, 0);
        m_left_master.config_kF(0, Constants.VEL_kF, 0);
        m_left_master.config_IntegralZone(VELOCITY_CONTROL_SLOT, Constants.VEL_IZONE, 0);

        // right position gains
        m_right_master.selectProfileSlot(VELOCITY_CONTROL_SLOT, 0);
        m_right_master.config_kP(0, Constants.VEL_kP, 0);
        m_right_master.config_kP(0, Constants.VEL_kI, 0);
        m_right_master.config_kP(0, Constants.VEL_kD, 0);
        m_right_master.config_kP(0, Constants.VEL_kF, 0);
        m_right_master.config_IntegralZone(VELOCITY_CONTROL_SLOT, Constants.VEL_IZONE, 0);

    }
    
    // abstracted stuff

    @Override
    public void stop() {
        setOpenLoop(0, 0);
        m_left_master.neutralOutput();
        m_right_master.neutralOutput();
    }


    @Override
    public void reset() {
        // m_left_master.setSelectedSensorPosition(0, 0, 0);
        // m_right_master.setSelectedSensorPosition(0, 0, 0);
        m_gyro.setYaw(0.0, 0);
        m_gyro.setYawToCompass(0);
    }

    @Override
    public void outputToSmartDashboard() {
        // TODO: something here
    }



}
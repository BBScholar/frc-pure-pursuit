package org.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.ParamEnum;

import org.frc2018.Constants;
import org.frc2018.Utils;
import org.frc2018.Vector2;

public class Drivetrain extends Subsystem {

    /**
     * Type of drivetrain control
     */
    public enum DriveMode {
        PERCENT_LOOP, // drive with the given percentage outputs
        POSITION_LOOP, // drive the specified distance
        TURN_LOOP, // turn the specified degrees
        VELOCITY_LOOP, // drive the specified velocities
    }

    private TalonSRX m_left_master, m_left_slave;
    private TalonSRX m_right_master, m_right_slave;
    private PigeonIMU m_gyro;

    private DriveMode m_mode;

    private boolean m_is_brakemode;

    private Drivetrain() {
        super();
        m_gyro = new PigeonIMU(Constants.GYRO_PORT);

        m_left_master = new TalonSRX(Constants.LEFT_MASTER_PORT);
        m_left_slave = new TalonSRX(Constants.LEFT_SLAVE_PORT);

        m_right_master = new TalonSRX(Constants.RIGHT_MASTER_PORT);
        m_right_slave = new TalonSRX(Constants.RIGHT_SLAVE_PORT);

        // configure talons
        m_left_master.setSensorPhase(false);
        m_right_master.setSensorPhase(false);

        m_left_slave.follow(m_left_master);
        m_right_slave.follow(m_right_master);

        m_right_master.setInverted(true);
        m_right_slave.setInverted(true);

        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_left_master.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, Constants.TALON_UPDATE_PERIOD_MS, 0);

        configureForPercent();

        m_is_brakemode = false;
        setBrakeMode(false);

        setPercent(0, 0);
    }

    @Override
    public void update() {
        Position.getInstance().update(getLeftDistanceInches(), getRightDistanceInches(), getGyroAngle());
    }

    /**
     * Sets talon brake mode.
     * @param on true if want brake mode to be on, false if want brake mode to be off.
     */
    public void setBrakeMode(boolean on) {
        if(m_is_brakemode == on) return;

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
        m_is_brakemode = on;
    }

    /**
     * 
     * @return boolean representing brake mode status of drivetrain. (True = on, False = off)
     */
    public boolean isBrakeMode() {
        return m_is_brakemode;
    }

    /**
     * 
     * @param left
     * @param right
     */
    public void setPercent(double left, double right) {
        if(m_mode != DriveMode.PERCENT_LOOP) {
            configureForPercent();
        }
        m_left_master.set(ControlMode.PercentOutput, left);
        m_right_master.set(ControlMode.PercentOutput, right);
    }

    /**
     * 
     * @param left
     * @param right
     */
    public void setVelocity(double left, double right) {
        if(m_mode != DriveMode.VELOCITY_LOOP) {
            configureForVelocity();
        }
        left = Math.min(left, Constants.MAX_VELOCITY_SETPOINT);
        right = Math.min(right, Constants.MAX_VELOCITY_SETPOINT);
        m_left_master.set(ControlMode.Velocity, Utils.inchesPerSecondToEncoderTicksPer100Ms(left));
        m_right_master.set(ControlMode.Velocity, Utils.inchesPerSecondToEncoderTicksPer100Ms(right));
    }

    /**
     * Get the current left velocity error
     * @return the current velocity error in inches per second
     */
    public double getLeftVelocityError() {
        return Utils.encoderTicksPer100MsToInchesPerSecond(m_left_master.getClosedLoopError(0));
    }

    /**
     * Get the current right velocity error
     * @return the current velocity error in inches per second
     */
    public double getRightVelocityError() {
        return Utils.encoderTicksPer100MsToInchesPerSecond(m_right_master.getClosedLoopError(0));
    }

    /**
     * Get the current average velocity error
     * @return the current velocity error in inches per second
     */
    public double getAverageVelocityError() {
        return (getLeftVelocityError() + getRightVelocityError()) / 2;
    }

    /**
     * 
     * @param distance relative distance in inches
     * Call this once, and then check output error
     */
    public void setPosition(double distance) {
        if(m_mode != DriveMode.POSITION_LOOP) {
            configureForPosition();
        }
        // account for current robot position, angle units
        int abs_distance = Utils.inchesToEncoderTicks(((getLeftDistanceInches() + getRightDistanceInches()) / 2) + distance);
        int angle_target = m_right_master.getSelectedSensorPosition(1);
        m_right_master.set(ControlMode.Position, abs_distance, DemandType.AuxPID, angle_target); // left follows this
    }

    /**
     * Get the current left position error
     * @return the current position error in inches
     */
    public double getLeftPositionError() {
        return Utils.encoderTicksToInches(m_left_master.getClosedLoopError(0));
    }

    /**
     * Get the current right position error
     * @return the current position error in inches
     */
    public double getRightPositionError() {
        return Utils.encoderTicksToInches(m_right_master.getClosedLoopError(0));
    }

    /**
     * Get the current average position error
     * @return the current position error in inches
     */
    public double getAveragePositionError() {
        return Utils.encoderTicksToInches(m_left_master.getClosedLoopError(0) + m_right_master.getClosedLoopError(0)) / 2;
    }

    /**
     * Get the current position angle error
     * @return the current angle error in degrees
     */
    public double getAnglePositionError() {
        return Utils.talonAngleToDegrees(m_left_master.getClosedLoopError(1));
    }

    /**
     * 
     * @param angle relative angle in degrees
     * Call this once, and then check output error
     */
    public void setTurn(double angle) {
        if(m_mode != DriveMode.TURN_LOOP) {
            configureForTurn();
        }
        // account for current robot position, angle units
        int fixed_distance = Utils.inchesToEncoderTicks((getLeftDistanceInches() + getRightDistanceInches()) / 2);
        int angle_target = m_right_master.getSelectedSensorPosition(1) + Utils.degreesToTalonAngle(angle);
        m_right_master.set(ControlMode.Position, angle_target, DemandType.AuxPID, fixed_distance);
    }

    /**
     * Get the current turn error
     * @return the current angle error in degrees
     */
    public double getTurnError() {
        return Utils.talonAngleToDegrees(m_right_master.getClosedLoopError(0));
    }

    /**
     * Get the current turn position error
     * @return the current turn position error in inches
     */
    public double getTurnFixedError() {
        return Utils.encoderTicksToInches(m_right_master.getClosedLoopError(1));
    }

    // sensor data

    /**
     * 
     * @return
     */
    private int getLeftDistanceRaw() {
        return m_left_master.getSensorCollection().getQuadraturePosition();
    }

    /**
     * 
     * @return
     */
    public double getLeftDistanceInches() {
        return Utils.encoderTicksToInches(getLeftDistanceRaw());
    }

    /**
     * 
     * @return
     */
    private int getLeftVelocityRaw() {
        return m_left_master.getSensorCollection().getQuadratureVelocity();
    }

    /**
     * 
     * @return
     */
    public double getLeftVelocityInchesPerSecond() {
        return Utils.encoderTicksPer100MsToInchesPerSecond(getLeftVelocityRaw());
    }

    /**
     * 
     * @param inches
     */
    private void setLeftDistanceInches(double inches) {
        setLeftDistanceRaw(Utils.inchesToEncoderTicks(inches));
    }

    /**
     * 
     * @param ticks
     */
    private void setLeftDistanceRaw(int ticks) {
        m_left_master.getSensorCollection().setQuadraturePosition(ticks, 0);
    }

    /**
     * 
     * @return
     */
    private int getRightDistanceRaw() {
        return m_right_master.getSensorCollection().getQuadraturePosition();
    }

    /**
     * 
     * @return distance of right encoder in inches
     */
    public double getRightDistanceInches() {
        return Utils.encoderTicksToInches(getRightDistanceRaw());
    }

    /**
     * 
     * @return velocity of right side of drivetrain in encoder ticks per 100 milli-seconds
     */
    private int getRightVelocityRaw() {
        return m_right_master.getSensorCollection().getQuadratureVelocity();
    }

    /**
     * 
     * @return velocity of right side of drivetrain in inches per second
     */
    public double getRightVelocityInchesPerSecond() {
        return Utils.encoderTicksPer100MsToInchesPerSecond(getRightVelocityRaw());
    }

    /**
     * 
     * @param inches right encoder distance to set in inches
     */
    private void setRightDistanceInches(double inches) {
        setRightDistanceRaw(Utils.inchesToEncoderTicks(inches));
     }

    /**
     * 
     * @param ticks right encoder distance to set in encoder ticks
     */
    private void setRightDistanceRaw(int ticks) {
        m_right_master.getSensorCollection().setQuadraturePosition(-ticks, 0);
    }

    /**
     * 
     * @return robot angle in degrees
     */
    public double getGyroAngle() {
        double[] ypr = new double[3];
        m_gyro.getYawPitchRoll(ypr);
        double angle = ypr[0];
        return angle;
    }

    /**
     * 
     * @return robot heading in degrees
     */
    public double getGyroHeading() {
        double[] ypr = new double[3];
        m_gyro.getYawPitchRoll(ypr);
        double angle = ypr[0];
        while(angle >= 360) angle -= 360;
        while(angle < 0) angle += 360;
        return angle;
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

    // configurations

    /**
     * 
     */
    private void configureForPosition() {
        // configure quad encoders for pidf[0] feedback sensors on both talons
        m_left_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        
        m_right_master.configRemoteFeedbackFilter(m_left_master.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0);
        m_right_master.configRemoteFeedbackFilter(m_gyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0);

        m_right_master.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0);
        m_right_master.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0);

        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 0, 0);

        m_right_master.configSelectedFeedbackCoefficient(0.5, 0, 0);
        m_right_master.configSelectedFeedbackCoefficient(Constants.TURN_UNITS_PER_ROTATION / Constants.PIGEON_UNITS_PER_ROTATION, 1, 0);

        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 1, 0);

        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_left_master.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.TALON_UPDATE_PERIOD_MS, 0);

        m_left_master.configPeakOutputForward(+1.0, 0);
        m_left_master.configPeakOutputReverse(-1.0, 0);
        m_right_master.configPeakOutputForward(+1.0, 0);
        m_right_master.configPeakOutputReverse(-1.0, 0);

        // right position gains
        m_right_master.config_kP(0, Constants.POS_kP, 0);
        m_right_master.config_kI(0, Constants.POS_kI, 0);
        m_right_master.config_kD(0, Constants.POS_kD, 0);
        m_right_master.config_kF(0, Constants.POS_kF, 0);
        m_right_master.config_kP(1, Constants.ANGLE_kP, 0);
        m_right_master.config_kI(1, Constants.ANGLE_kI, 0);
        m_right_master.config_kD(1, Constants.ANGLE_kD, 0);
        m_right_master.config_kF(1, Constants.ANGLE_kF, 0);
        m_right_master.config_IntegralZone(0, Constants.POS_IZONE, 0);
        m_right_master.configClosedLoopPeakOutput(0, Constants.POS_MAX_OUTPUT, 0);
        m_right_master.config_IntegralZone(1, Constants.ANGLE_IZONE, 0);
        m_right_master.configClosedLoopPeakOutput(1, Constants.ANGLE_MAX_OUTPUT, 0);
        //m_right_master.configClosedloopRamp(Constants.CLOSED_LOOP_RAMP, 0);
        //m_right_master.configMotionAcceleration(Constants.POS_MAX_ACCEL, 0);
        //m_right_master.configMotionCruiseVelocity(Constants.POS_MAX_VELO, 0);

        // set pidf loop period
        m_right_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 0, 0);
        m_right_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 1, 0);

        // set polarity of pidf[1] (see ctre docs for more details)
        m_right_master.configAuxPIDPolarity(!Constants.INVERT_ANGLE_AUX_PIDF, 0);

        m_right_master.selectProfileSlot(0, 0);
        m_right_master.selectProfileSlot(1, 1);

        m_left_master.follow(m_right_master, FollowerType.AuxOutput1);
        m_left_master.setInverted(false);

        m_mode = DriveMode.POSITION_LOOP;
    }

    private void configureForTurn() {
        m_left_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        
        m_right_master.configRemoteFeedbackFilter(m_left_master.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, 0, 0);
        m_right_master.configRemoteFeedbackFilter(m_gyro.getDeviceID(), RemoteSensorSource.Pigeon_Yaw, 1, 0);

        m_right_master.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, 0);
        m_right_master.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.QuadEncoder, 0);

        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, 1, 0);

        m_right_master.configSelectedFeedbackCoefficient(0.5, 1, 0);
        m_right_master.configSelectedFeedbackCoefficient(Constants.TURN_UNITS_PER_ROTATION / Constants.PIGEON_UNITS_PER_ROTATION, 0, 0);

        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.RemoteSensor1, 0, 0);

        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_right_master.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, Constants.TALON_UPDATE_PERIOD_MS, 0);
        m_left_master.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, Constants.TALON_UPDATE_PERIOD_MS, 0);

        m_left_master.configPeakOutputForward(+1.0, 0);
        m_left_master.configPeakOutputReverse(-1.0, 0);
        m_right_master.configPeakOutputForward(+1.0, 0);
        m_right_master.configPeakOutputReverse(-1.0, 0);

        // right position gains
        m_right_master.config_kP(0, Constants.TURN_kP, 0);
        m_right_master.config_kI(0, Constants.TURN_kI, 0);
        m_right_master.config_kD(0, Constants.TURN_kD, 0);
        m_right_master.config_kF(0, Constants.TURN_kF, 0);
        m_right_master.config_kP(1, Constants.FIXED_kP, 0);
        m_right_master.config_kI(1, Constants.FIXED_kI, 0);
        m_right_master.config_kD(1, Constants.FIXED_kD, 0);
        m_right_master.config_kF(1, Constants.FIXED_kF, 0);
        m_right_master.config_IntegralZone(0, Constants.TURN_IZONE, 0);
        m_right_master.configClosedLoopPeakOutput(0, Constants.TURN_MAX_OUTPUT, 0);
        m_right_master.config_IntegralZone(1, Constants.FIXED_IZONE, 0);
        m_right_master.configClosedLoopPeakOutput(1, Constants.FIXED_MAX_OUTPUT, 0);

        // set pidf loop period
        m_right_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 0, 0);
        m_right_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 1, 0);

        // set polarity of pidf[1] (see ctre docs for more details)
        m_right_master.configAuxPIDPolarity(!Constants.INVERT_FIXED_AUX_PIDF, 0);

        m_right_master.selectProfileSlot(0, 0);
        m_right_master.selectProfileSlot(1, 1);

        m_left_master.follow(m_right_master, FollowerType.AuxOutput1);
        m_left_master.setInverted(true); // check this

        m_mode = DriveMode.TURN_LOOP;
    }

    private void configureForVelocity() {
        m_left_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        
        m_right_master.configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 0, 0);
        m_right_master.configRemoteFeedbackFilter(0x00, RemoteSensorSource.Off, 1, 0);

        m_right_master.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.None, 0);
        m_right_master.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.None, 0);

        m_right_master.configSelectedFeedbackCoefficient(1, 1, 0);
        m_right_master.configSelectedFeedbackCoefficient(1, 0, 0);

        m_right_master.configSelectedFeedbackSensor(FeedbackDevice.None, 1, 0);

        m_left_master.configPeakOutputForward(+1.0, 0);
        m_left_master.configPeakOutputReverse(-1.0, 0);
        m_right_master.configPeakOutputForward(+1.0, 0);
        m_right_master.configPeakOutputReverse(-1.0, 0);

        // right position gains
        m_right_master.config_kP(0, Constants.VEL_kP, 0);
        m_right_master.config_kI(0, Constants.VEL_kI, 0);
        m_right_master.config_kD(0, Constants.VEL_kD, 0);
        m_right_master.config_kF(0, Constants.VEL_kF, 0);
        m_right_master.config_kP(1, 0, 0);
        m_right_master.config_kI(1, 0, 0);
        m_right_master.config_kD(1, 0, 0);
        m_right_master.config_kF(1, 0, 0);
        m_right_master.config_IntegralZone(0, 0, 0);
        m_right_master.configClosedLoopPeakOutput(0, 1.0, 0);
        m_right_master.config_IntegralZone(1, 0, 0);
        m_right_master.configClosedLoopPeakOutput(1, 0, 0);

        // left velocity gains
        m_left_master.config_kP(0, Constants.VEL_kP, 0);
        m_left_master.config_kI(0, Constants.VEL_kI, 0);
        m_left_master.config_kD(0, Constants.VEL_kD, 0);
        m_left_master.config_kF(0, Constants.VEL_kF, 0);
        m_left_master.config_kP(1, 0, 0);
        m_left_master.config_kI(1, 0, 0);
        m_left_master.config_kD(1, 0, 0);
        m_left_master.config_kF(1, 0, 0);
        m_left_master.config_IntegralZone(0, 0, 0);
        m_left_master.configClosedLoopPeakOutput(0, 1.0, 0);
        m_left_master.config_IntegralZone(1, 0, 0);
        m_left_master.configClosedLoopPeakOutput(1, 0, 0);

        // set pidf loop period
        m_right_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 0, 0);
        m_right_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 1, 0);
        m_left_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 0, 0);
        m_left_master.configSetParameter(ParamEnum.ePIDLoopPeriod, Constants.TALON_PIDF_LOOP_PERIOD_MS, 0x00, 1, 0);

        m_right_master.selectProfileSlot(0, 0);
        m_right_master.selectProfileSlot(1, 1);
        m_left_master.selectProfileSlot(0, 0);
        m_left_master.selectProfileSlot(1, 1);

        m_left_master.setInverted(false); // check this

        m_mode = DriveMode.VELOCITY_LOOP;
    }

    private void configureForPercent() {
        m_left_master.configNominalOutputForward(0.0, 0);
        m_left_master.configNominalOutputReverse(0.0, 0);
        m_right_master.configNominalOutputForward(0.0, 0);
        m_right_master.configNominalOutputReverse(0.0, 0);
        m_left_master.setInverted(false);
        m_left_master.configPeakOutputForward(+1.0, 0);
        m_left_master.configPeakOutputReverse(-1.0, 0);
        m_right_master.configPeakOutputForward(+1.0, 0);
        m_right_master.configPeakOutputReverse(-1.0, 0);
        m_mode = DriveMode.PERCENT_LOOP;
    }
    
    // abstracted stuff

    @Override
    public void stop() {
        setPercent(0, 0);
        m_left_master.neutralOutput();
        m_right_master.neutralOutput();
    }


    @Override
    public void reset() {
        m_left_master.setSelectedSensorPosition(0, 0, 0);
        m_right_master.setSelectedSensorPosition(0, 0, 0);
        m_left_master.setSelectedSensorPosition(0, 1, 0);
        m_right_master.setSelectedSensorPosition(0, 1, 0);
        setLeftDistanceInches(0);
        setRightDistanceInches(0);
        m_gyro.setYaw(0.0, 0);
        Position.getInstance().reset();
    }

    @Override
    public void broadcastToSmartDashboard() {
        // TODO: something here
    }



    private static Drivetrain _instance = new Drivetrain();
    
    public static Drivetrain getInstance() {
        return _instance;
    }

}

class Position {

    private double x, y;
    private double last_left, last_right;
    private double last_angle;

    private Position() {
        x = 0;
        y = 0;
        
        last_left = 0;
        last_right = 0;
        last_angle = 0;
    }

    public void update(double left_distance, double right_distance, double angle) {
        angle = Math.toRadians(angle);
        double angle_delta = angle - last_angle;
        if(angle_delta == 0) angle_delta = Constants.EPSILON;
        double left_delta = left_distance - last_left;
        double right_delta = right_distance - last_right;
        double distance = (left_delta + right_delta) / 2.0;
        double radius_of_curvature = distance / angle_delta;
        double delta_y = radius_of_curvature * Math.sin(angle_delta);
        double delta_x = radius_of_curvature * (Math.cos(angle_delta) - 1);
        y += delta_x * Math.cos(last_angle) - delta_y * Math.sin(last_angle);
        x +=  delta_x * Math.sin(last_angle) + delta_y * Math.cos(last_angle);
        last_left = left_distance;
        last_right = right_distance;
        last_angle = angle;
    }

    public Vector2 getPositionVector() {
        return new Vector2(x, y);
    }

    public void reset() {
        x = 0;
        y = 0;
        last_left = 0;
        last_right = 0;
        last_angle = 0;
    }

    @Override
    public String toString() {
        return String.format("Robot Position: X: %.2f, Y:%.2f ", x, y);
    }

    private static Position _instance = new Position();

    public static Position getInstance() {
        return _instance;
    }
}
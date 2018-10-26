package org.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.frc2018.Constants;

public class Arm implements Subsystem {


    private Arm _instance = new Arm();

    public Arm getInstance() {
        return _instance;
    }

    public enum ArmMode {
        OPEN_LOOP,
        ANGLE_CONTROL
    }

    private static final int ANGLE_PID_LOOP = 0;

    private TalonSRX m_talon;

    private ArmMode m_mode;


    private Arm() {
        m_talon = new TalonSRX(Constants.ARM_PORT);
        m_talon.configSelectedFeedbackSensor(FeedbackDevice.Analog, ANGLE_PID_LOOP, 0);

        m_talon.config_kP(ANGLE_PID_LOOP, Constants.ARM_kP, 0);
        m_talon.config_kI(ANGLE_PID_LOOP, Constants.ARM_kI, 0);
        m_talon.config_kD(ANGLE_PID_LOOP, Constants.ARM_kF, 0);
        m_talon.config_kF(ANGLE_PID_LOOP, Constants.ARM_kF, 0);



        m_talon.configReverseSoftLimitEnable(true, 0);
        


        m_talon.setNeutralMode(NeutralMode.Brake);

        m_mode = ArmMode.OPEN_LOOP;
    }

    public void setOpenLoop(double speed) {
        if(m_mode != ArmMode.OPEN_LOOP) {
            // do something
        }
        m_talon.set(ControlMode.PercentOutput, speed * Constants.OPEN_LOOP_ARM_MULT);
    }

    public void setAngle(double angle) {
        
    }

    @Override
    public void update() {
        
    }

    @Override
    public void reset() {
        
    }

    public void reloadGains() {
        m_talon.config_kP(ANGLE_PID_LOOP, Constants.ARM_kP, 0);
        m_talon.config_kI(ANGLE_PID_LOOP, Constants.ARM_kI, 0);
        m_talon.config_kD(ANGLE_PID_LOOP, Constants.ARM_kF, 0);
        m_talon.config_kF(ANGLE_PID_LOOP, Constants.ARM_kF, 0);
    }

}
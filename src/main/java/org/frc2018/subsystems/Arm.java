package org.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.frc2018.Constants;

public class Arm extends Subsystem {
    private TalonSRX m_arm;
    private TalonSRX m_left_intake, m_right_intake;

    private Arm() {
        m_arm = new TalonSRX(Constants.ARM_PORT);
        m_left_intake = new TalonSRX(Constants.LEFT_INTAKE_PORT);
        m_right_intake = new TalonSRX(Constants.RIGHT_INTAKE_PORT);

        m_left_intake.setInverted(Constants.INVERT_INTAKE);
        m_right_intake.setInverted(!Constants.INVERT_INTAKE);

        m_arm.setInverted(Constants.INVERT_ARM);
    }

    @Override
    public void update() {

    }

    public void intake() {
        m_left_intake.setNeutralMode(NeutralMode.Brake);
        m_right_intake.setNeutralMode(NeutralMode.Brake);
        m_left_intake.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
        m_right_intake.set(ControlMode.PercentOutput, Constants.INTAKE_SPEED);
    }

    public void hold() {
        m_left_intake.setNeutralMode(NeutralMode.Brake);
        m_right_intake.setNeutralMode(NeutralMode.Brake);
        m_left_intake.set(ControlMode.PercentOutput, Constants.INTAKE_HOLD_SPEED);
        m_right_intake.set(ControlMode.PercentOutput, Constants.INTAKE_HOLD_SPEED);
    }

    public void drop() {
        m_left_intake.setNeutralMode(NeutralMode.Brake);
        m_right_intake.setNeutralMode(NeutralMode.Brake);
        m_left_intake.set(ControlMode.PercentOutput, Constants.INTAKE_DROP_SPEED);
        m_right_intake.set(ControlMode.PercentOutput, Constants.INTAKE_DROP_SPEED);
    }

    public void spit() {
        m_left_intake.setNeutralMode(NeutralMode.Brake);
        m_right_intake.setNeutralMode(NeutralMode.Brake);
        m_left_intake.set(ControlMode.PercentOutput, Constants.INTAKE_SPIT_SPEED);
        m_right_intake.set(ControlMode.PercentOutput, Constants.INTAKE_SPIT_SPEED);
    }

    public void stopIntake() {
        m_left_intake.set(ControlMode.PercentOutput, 0);
        m_right_intake.set(ControlMode.PercentOutput, 0);
    }

    public void setArm(double speed) { // positive is up
        speed = (Math.abs(speed) > Constants.MAX_ARM_SPEED) ? Math.signum(speed) * Constants.MAX_ARM_SPEED : speed;
        m_arm.set(ControlMode.PercentOutput, speed);
    }

    public void stopArm() {
        System.out.println("stop");
        m_arm.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void stop() {
        setArm(0);
        stopIntake();
    }

    @Override
    public void reset() {
        m_left_intake.setNeutralMode(NeutralMode.Coast);
        m_right_intake.setNeutralMode(NeutralMode.Coast);
    }

    private static Arm _instance = new Arm();

    public static Arm getInstance() {
        return _instance;
    }
}
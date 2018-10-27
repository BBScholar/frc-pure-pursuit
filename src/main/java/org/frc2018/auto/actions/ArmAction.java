package org.frc2018.auto.actions;

import org.frc2018.subsystems.Arm;

public class ArmAction extends Action {

    public static enum ArmDirection {
        UP,
        DOWN,
        HOLD_UP,
        HOLD_DOWN,
        NONE
    }

    public static enum IntakeDirection {
        INTAKE,
        HOLD,
        DROP,
        SPIT,
        NONE
    }

    private ArmDirection arm_direction;
    private IntakeDirection intake_direction;

    public ArmAction(ArmDirection arm_direction, IntakeDirection intake_direction, double timeout) {
        super(timeout);
        this.arm_direction = arm_direction;
        this.intake_direction = intake_direction;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {

        switch(arm_direction) {
            case UP:
                Arm.getInstance().setArm(1.0);
            case DOWN:
                Arm.getInstance().setArm(-1.0);
            case HOLD_UP:
                Arm.getInstance().setArm(0.2);
            case HOLD_DOWN:
                Arm.getInstance().setArm(-0.2);
            case NONE:
                Arm.getInstance().stopArm();
        }

        switch(intake_direction) {
            case INTAKE:
                Arm.getInstance().intake();
            case HOLD:
                Arm.getInstance().hold();
            case DROP:
                Arm.getInstance().drop();
            case SPIT:
                Arm.getInstance().spit();
            case NONE:
                Arm.getInstance().stopIntake();
        }
    }

    @Override
    public void finish() {
        Arm.getInstance().stop();
    }

    @Override
    public void reset() {}
}
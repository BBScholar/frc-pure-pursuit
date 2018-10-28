package org.frc2018.auto.actions;

import org.frc2018.subsystems.Arm;

public class IntakeAction extends Action {

    private double speed;

    public IntakeAction(double timeout, double speed) {
        super(timeout);
        this.speed = speed;
    }

    @Override
    public void start() {
        super.start();
        Arm.getInstance().setIntake(speed);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public boolean next() {
        return true;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void finish() {
        super.finish();
    }

}
package org.frc2018.auto.actions;

public interface Action {

    public void start();

    public void update();

    public boolean isFinished();

    public void done();

}
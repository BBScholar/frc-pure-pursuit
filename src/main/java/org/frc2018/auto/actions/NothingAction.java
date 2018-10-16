package org.frc2018.auto.actions;

import edu.wpi.first.wpilibj.Timer;

public class NothingAction implements Action {

    private double m_duration;
    private double m_start_time;

    public NothingAction(double duration) {
        m_duration = duration;
    }

    @Override
    public void start() {
        m_start_time = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        if(Timer.getFPGATimestamp() - m_start_time >= m_duration) {
            return true;
        }
        return false;
    }

    @Override
    public void done() {
        
    }

}
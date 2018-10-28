package org.frc2018.auto.actions;

import org.frc2018.auto.routines.Routine;

import edu.wpi.first.wpilibj.Timer;

public class Action {

    private Timer _timeout;
    private double _timeout_ms;

    protected Action(double timeout_s) {
        _timeout = new Timer();
        _timeout_ms = timeout_s;
    }

    public void start() {
        reset();
        _timeout.reset();
        _timeout.start();
    }

    public void update() {}

    protected boolean timedOut() {
        double t = _timeout.get();
        boolean result = t >= _timeout_ms;
        // System.out.println("Time: " + t + ", Timeout: " + _timeout_ms + ", Result: " + result);
        return result;
    }

    public boolean next() {
        return timedOut();
    }

    public void finish() {}

    public void reset() {
        _timeout.stop();
        _timeout.reset();

    }
}
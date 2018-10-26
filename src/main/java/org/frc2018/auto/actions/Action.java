package org.frc2018.auto.actions;

import org.frc2018.auto.routines.Routine;

import edu.wpi.first.wpilibj.Timer;

public class Action {

    private Timer _timeout;
    private double _timeout_ms;

    protected Action(double timeout_ms) {
        _timeout = new Timer();
        _timeout_ms = timeout_ms;
    }

    public void start() {
        _timeout.reset();
        _timeout.start();
    }

    public void update() {}

    protected boolean timedOut() {
        return _timeout.get() > _timeout_ms;
    }

    public Routine.RoutineTag next() {
        return timedOut() ? Routine.CURRENT_ROUTINE : Routine.NOT_FINISHED;
    }

    public void finish() {}

    public void reset() {}
}
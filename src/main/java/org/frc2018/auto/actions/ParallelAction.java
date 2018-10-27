package org.frc2018.auto.actions;

import java.util.ArrayList;
import java.util.List;

import org.frc2018.auto.routines.Routine;
import org.frc2018.auto.routines.Routine.RoutineTag;

public class ParallelAction extends Action {

    private final List<Action> mActions;

    public ParallelAction(double timeout, List<Action> actions) {
        super(timeout);
        mActions = actions;
    }

    @Override
    public void start() {
        super.start();
        for(Action a : mActions) {
            a.start();
        }
    }

    @Override
    public void update() {
        super.update();
        for(Action a : mActions) {
            a.update();
        }
    }


    @Override
    public boolean next() {
        if(super.timedOut()) return true;
        for(Action a : mActions) {
            if(!a.next()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void finish() {
        for(Action a : mActions) {
            a.finish();
        }
    }

    @Override
    public void reset() {
        super.reset();
        for(Action a : mActions) {
            a.reset();
        }
    }


}
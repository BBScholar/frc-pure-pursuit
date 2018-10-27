package org.frc2018.auto.actions;

import java.util.ArrayList;

import org.frc2018.auto.routines.Routine;
import org.frc2018.auto.routines.Routine.RoutineTag;

public class ParallelAction extends Action {

    private final ArrayList<Action> mActions;

    public ParallelAction(double timeout, ArrayList<Action> actions) {
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
    protected boolean timedOut() {
        return super.timedOut();
    }

    @Override
    public Routine.RoutineTag next() {
        if(timedOut()) return Routine.CURRENT_ROUTINE;
        for(Action a : mActions) {
            if(a.next().equals(Routine.NOT_FINISHED)) {
                return Routine.NOT_FINISHED;
            }
        }
        return Routine.CURRENT_ROUTINE;
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
package org.frc2018.auto;

import org.frc2018.auto.routines.Routine;

public class AutoRoutineHandler {

    private Routine m_routine;

    public AutoRoutineHandler(Routine routine) {
        m_routine = routine;
    }

    public void start() {
        m_routine.getCurrentAction().start();
    }

    public void update() {
        if(m_routine.isFinished()) return;

        if(m_routine.getCurrentAction().isFinished()) {
            m_routine.getCurrentAction().done();
            m_routine.advanceRoutine();
            m_routine.getCurrentAction().start();
            return;
        }
        m_routine.getCurrentAction().update();
    }

    public void reset() {
        m_routine.reset();
    }


}
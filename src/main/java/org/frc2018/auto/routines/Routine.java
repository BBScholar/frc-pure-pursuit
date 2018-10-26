package org.frc2018.auto.routines;

import java.util.ArrayList;
import java.util.List;

import org.frc2018.auto.actions.Action;

public class Routine {

    public static final String CURRENT_ROUTINE = "__default__";
    public static final String NOT_FINISHED = "";

    private List<Action> m_actions;
    private int m_step_number;

    public Routine() {
        m_actions = new ArrayList<Action>();
        m_step_number = 0;
    }

    public void addAction(Action action) {
        m_actions.add(action);
    }

    public Action getCurrentAction() {
        return m_actions.get(m_step_number);
    }

    public boolean advanceRoutine() {
        if(!(m_step_number + 1 < m_actions.size())) return false;
        m_step_number++;
        return true;
    }

    public void reset() {
        m_step_number = 0;
    }

    public boolean isFinished() {
        return m_step_number == m_actions.size() - 1 && getCurrentAction().next() != NOT_FINISHED;
    }
}
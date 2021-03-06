package org.frc2018.auto.routines;

import java.util.ArrayList;
import java.util.List;

import org.frc2018.auto.actions.Action;

public class Routine {

    private List<Action> m_actions;
    private int m_step_number;

    public Routine() {
        m_actions = new ArrayList<>();
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
        if(m_step_number == m_actions.size() - 1 && getCurrentAction().isFinished()) {
            return true;
        }
        return false;
    }

}
package org.frc2018.auto.routines;

import java.util.ArrayList;
import java.util.List;

import org.frc2018.auto.actions.Action;

public class Routine {

    public static final RoutineTag CURRENT_ROUTINE = new RoutineTag("__default__", -1);
    public static final RoutineTag NOT_FINISHED = new RoutineTag("", -1);

    private List<Action> m_actions;
    private int m_step_number;
    private String name;

    public Routine(String name) {
        m_actions = new ArrayList<Action>();
        m_step_number = 0;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addAction(Action action) {
        m_actions.add(action);
    }

    public Action getCurrentAction() {
        return m_actions.get(m_step_number);
    }

    public int getActionIndex() {
        return m_step_number;
    }

    public boolean advanceRoutine() {
        if(isLastStep()) return false;
        m_step_number++;
        return true;
    }

    public void setActionIndex(int index) {
        m_step_number = index;
    }

    public void reset() {
        for(Action i : m_actions) {
            i.reset();
        }
        m_step_number = 0;
    }

    public boolean isLastStep() {
        return m_step_number >= m_actions.size() - 1;
    }

    public static class RoutineTag {
        private String name;
        private int index;
        public RoutineTag(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public boolean equals(Object o) {
            RoutineTag temp = (RoutineTag) o;
            return temp.getName() == getName() && temp.getIndex() == getIndex();
        }
    }
}
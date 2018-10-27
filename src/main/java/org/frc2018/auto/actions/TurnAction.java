package org.frc2018.auto.actions;

import org.frc2018.auto.routines.Routine;
import org.frc2018.auto.routines.Routine.RoutineTag;
import org.frc2018.subsystems.Drivetrain;

public class TurnAction extends Action {

    public enum TurnType {
        ABSOLUTE, // TODO: impliment absolute angle control
        RELATIVE
    }

    private double m_angle;
    private TurnType m_type;

    public TurnAction(double timeout, double angle, TurnType type) {
        super(timeout);
        m_angle = angle;
        m_type = type;
    }

    public TurnAction(double timeout, double angle) {
        this(timeout, angle, TurnType.RELATIVE);
    }

    @Override
    public void start() {
        super.start();
        Drivetrain.getInstance().setTurn(m_angle);
    }

    @Override
    public void update() {
        super.update();
    }


    @Override
    public boolean next() {
        if(super.timedOut() || Math.abs(Drivetrain.getInstance().getTurnError()) < 3.0) {
            return true;
        }
        return false;
    }

    @Override
    public void reset() {
        super.reset();
    }


}
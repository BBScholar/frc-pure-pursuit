package org.frc2018.subsystems;

import org.frc2018.loops.Looper;

public interface Subsystem {

    /**
     * used to call nessesary functions every robot tick
     */
    public abstract void registerEnabledLoops(Looper enabledLooper);

    /**
     * outputs and recieves data from smart dashboard
     */
    public void outputToSmartDashboard();

    /**
     * stops the subsystem
     */
    public void stop();

    /**
     * resets subsystem,
     * ex: reset sensors
     */
    public void reset();

}
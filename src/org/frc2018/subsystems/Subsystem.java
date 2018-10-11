package org.frc2018.subsystems;

public interface Subsystem {

    /**
     * used to call nessesary functions every robot tick
     */
    public void update(double timestamp);

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
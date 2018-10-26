package org.frc2018.subsystems;

public class Subsystem {

    public Subsystem() {
        // Eventually, this will start broadcast loop to smart dashboard
    }

    /**
     * used to call nessesary functions every robot tick
     */
    public void update() {}

    /**
     * broadcast to smart dashboard
     */
    public void broadcastToSmartDashboard() {}

    /**
     * resets subsystem,
     * ex: reset sensors
     */
    public void reset() {}

    public void stop() {}

}
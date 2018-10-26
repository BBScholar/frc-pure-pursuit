package org.frc2018;

import edu.wpi.first.wpilibj.TimedRobot;

import org.frc2018.subsystems.Drivetrain;

public class Robot extends TimedRobot {

    public Robot() {
        super.setPeriod(Constants.UPDATE_PERIOD);
    }

    @Override
    public void robotInit() {
        
    }

    @Override
    public void robotPeriodic() {
        Drivetrain.getInstance().update();
    }

    @Override
    public void disabledInit() {
        Drivetrain.getInstance().setPercent(0, 0);
        Drivetrain.getInstance().reset();
    }

    @Override
    public void disabledPeriodic() {
        
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        Drivetrain.getInstance().setBrakeMode(false);
        Drivetrain.getInstance().setPercent(0, 0);
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
       
    }

    @Override
    public void testPeriodic() {
    }

}

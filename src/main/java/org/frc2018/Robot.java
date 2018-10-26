package org.frc2018;

import edu.wpi.first.wpilibj.TimedRobot;

import org.frc2018.controllers.TeleopController;
import org.frc2018.subsystems.Arm;
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
        Arm.getInstance().update();
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
        TeleopController.getInstance().init();
    }

    @Override
    public void teleopPeriodic() {
        TeleopController.getInstance().handle();
    }

    @Override
    public void testInit() {
       
    }

    @Override
    public void testPeriodic() {
    }

}

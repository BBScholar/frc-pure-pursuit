package org.frc2018;

import edu.wpi.first.wpilibj.TimedRobot;

import org.frc2018.controllers.AutoController;
import org.frc2018.controllers.TeleopController;
import org.frc2018.subsystems.Arm;
import org.frc2018.subsystems.Drivetrain;

public class Robot extends TimedRobot {

    public Robot() {
        super.setPeriod(Constants.UPDATE_PERIOD);
    }

    @Override
    public void robotInit() {
        AutoController.getInstance().reset();
    }

    @Override
    public void robotPeriodic() {
        Drivetrain.getInstance().update();
        Arm.getInstance().update();
    }

    @Override
    public void disabledInit() {
        Drivetrain.getInstance().stop();
        Drivetrain.getInstance().reset();
        Arm.getInstance().stop();
        Arm.getInstance().reset();
    }

    private boolean auto_rotate = false;
    private boolean cube_rotate = false;
    

    @Override
    public void disabledPeriodic() {
        if(!auto_rotate && TeleopController.getInstance().getRotateAuto()) {
            AutoController.getInstance().rotateAuto();
            auto_rotate = true;
        } else if(!TeleopController.getInstance().getRotateAuto()){
            auto_rotate = false;
        }
    }

    @Override
    public void autonomousInit() {
        AutoController.getInstance().reset();
        AutoController.getInstance().init();
    }

    @Override
    public void autonomousPeriodic() {
        AutoController.getInstance().handle();
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

package org.frc2018;

import org.frc2018.subsystems.Drive;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class Robot extends TimedRobot {

    private XboxController driver = new XboxController(0);

    public Robot() {
        super.setPeriod(0.005);
    }

    @Override
    public void robotInit() {
        
    }

    @Override
    public void robotPeriodic() {
        
    }

    @Override
    public void disabledInit() {
        Drive.getInstance().reset();
    }

    @Override
    public void disabledPeriodic() {
        
    }

    @Override
    public void autonomousInit() {
        Drive.getInstance().setPositionSetpoint(24, 24);
    }

    @Override
    public void autonomousPeriodic() {
        
    }

    @Override
    public void teleopInit() {
        Drive.getInstance().setBrakeMode(true);
        Drive.getInstance().setOpenLoop(0, 0);
    }

    @Override
    public void teleopPeriodic() {
        Drive.getInstance().setOpenLoop(-driver.getY(Hand.kLeft), -driver.getY(Hand.kRight));
    }

    @Override
    public void testInit() {
        super.testInit();
    }

    @Override
    public void testPeriodic() {
        super.testPeriodic();
    }

    


}
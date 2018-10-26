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
        
    }

    @Override
    public void disabledInit() {
        Drive.getInstance().setPercent(0, 0);
        // Drive.getInstance().setBrakeMode(true);
        Drive.getInstance().reset();
    }

    @Override
    public void disabledPeriodic() {
        
    }

    @Override
    public void autonomousInit() {
        Drive.getInstance().setBrakeMode(true);
        Drive.getInstance().reset();
        /*
        Drive.getInstance().update();
        Drive.getInstance().setVelocitySetpoint(-50, -50);
        Drive.getInstance().setPositionSetpoint(-48, -48);
        */
        Routine routine = new Routine();
        routine.addAction(new NothingAction(1));
        routine.addAction(new DrivePathAction(new Path("/home/lvuser/paths/path.csv")));
        routine.addAction(new NothingAction(1));
        handler = new AutoRoutineHandler(routine);
        handler.start();
    }

    @Override
    public void autonomousPeriodic() {
        Drive.getInstance().update();
        handler.update();
    }

    @Override
    public void teleopInit() {
        Drive.getInstance().setBrakeMode(false);
        Drive.getInstance().setOpenLoop(0, 0);
    }

    @Override
    public void teleopPeriodic() {
        Drive.getInstance().update();
        Drive.getInstance().setOpenLoop(-driver.getY(Hand.kLeft), -driver.getY(Hand.kRight));
        Drive.getInstance().update();
    }

    @Override
    public void testInit() {
       
    }

    @Override
    public void testPeriodic() {
        super.testPeriodic();
    }

    


}

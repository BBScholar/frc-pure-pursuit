package org.frc2018;

import org.frc2018.auto.AutoModeExecuter;
import org.frc2018.auto.modes.TestMode;
import org.frc2018.subsystems.Drive;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {

    private Drive drive = Drive.getInstance();
    private AutoModeExecuter mAutoModeExecuter = null;

    public Robot() {
        super.setPeriod(0.005);
    }

    @Override
    public void robotInit() {
        super.robotInit();
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void autonomousInit() {
        mAutoModeExecuter = new AutoModeExecuter();
        mAutoModeExecuter.setAutoMode(new TestMode());
        mAutoModeExecuter.start();
    }

    @Override
    public void autonomousPeriodic() {

    }


    private void allPeriodic() {
        drive.getInstance().update(Timer.getFPGATimestamp());
    }


    


}
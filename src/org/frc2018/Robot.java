package org.frc2018;

import org.frc2018.auto.AutoModeExecuter;
import org.frc2018.auto.modes.TestMode;
import org.frc2018.loops.Looper;
import org.frc2018.loops.RobotStateEstimator;
import org.frc2018.math.RigidTransform2d;
import org.frc2018.math.Translation2d;
import org.frc2018.subsystems.Drive;
import org.frc2018.subsystems.RobotState;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {

    private AutoModeExecuter mAutoModeExecuter = null;

    private Drive mDrive = Drive.getInstance();
    private RobotState mRobotState = RobotState.getInstance();

    private Looper mLooper = new Looper();

    public Robot() {
        super.setPeriod(0.005);
        
    }

    @Override
    public void robotInit() {
        mLooper.register(RobotStateEstimator.getInstance());;
        mDrive.registerEnabledLoops(mLooper);
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void disabledInit() {
        RobotState.getInstance().reset(Timer.getFPGATimestamp(), new RigidTransform2d());
        Drive.getInstance().setOpenLoop(0, 0);
        mLooper.stop();
    }

    @Override
    public void autonomousInit() {

        mAutoModeExecuter = new AutoModeExecuter();
        mAutoModeExecuter.setAutoMode(new TestMode());
        mAutoModeExecuter.start();
        RobotState.getInstance().reset(Timer.getFPGATimestamp(), new RigidTransform2d());
        mLooper.start();
    }

    @Override
    public void autonomousPeriodic() {
        //Drive.getInstance().setOpenLoop(-0.1, -0.1);
        allPeriodic();
    }


    private void allPeriodic() {
       
    }


    


}
package org.frc2018;

import org.frc2018.subsystems.Drive;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

    private Drive drive = Drive.getInstance();

    public Robot() {
        super.setPeriod(0.005);
    }

    @Override
    public void robotInit() {
        super.robotInit();
    }

    @Override
    public void robotPeriodic() {
        rls.update(drive.getLeftDistanceInches(), drive.getRightDistanceInches(), drive.getGyroAngleRadians());
    }



    


}
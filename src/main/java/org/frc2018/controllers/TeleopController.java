package org.frc2018.controllers;

import org.frc2018.Constants;
import org.frc2018.subsystems.Arm;
import org.frc2018.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

public class TeleopController extends Controller {
    private XboxController driver, codriver;

    private TeleopController() {
        driver = new XboxController(Constants.DRIVER_PORT);
        codriver = new XboxController(Constants.CODRIVER_PORT);
    }

    @Override
    public void init() {
        Drivetrain.getInstance().setBrakeMode(false);
        Drivetrain.getInstance().stop();
    }

    @Override
    public void handle() {
        double base = Math.abs(driver.getY(Hand.kLeft)) < Constants.XBOX_DEADBAND ? 0 : driver.getY(Hand.kLeft);
        double turn = Math.abs(driver.getX(Hand.kRight)) < Constants.XBOX_DEADBAND ? 0 : driver.getX(Hand.kRight);

        Drivetrain.getInstance().setPercent(base + turn, base - turn);

        double arm = Math.abs(codriver.getY(Hand.kLeft)) < Constants.XBOX_DEADBAND ? 0 : driver.getY(Hand.kLeft) * Constants.MAX_ARM_SPEED;

        Arm.getInstance().setArm(arm);

        if(codriver.getBumper(Hand.kLeft)) Arm.getInstance().intake();
        else if(codriver.getBumper(Hand.kRight)) Arm.getInstance().spit();
        else if(codriver.getTriggerAxis(Hand.kLeft) > Constants.XBOX_DEADBAND) Arm.getInstance().hold();
        else if(codriver.getTriggerAxis(Hand.kRight) > Constants.XBOX_DEADBAND) Arm.getInstance().drop();
        else Arm.getInstance().stopIntake();
    }

    private static TeleopController _instance = new TeleopController();

    public static TeleopController getInstance() {
        return _instance;
    }
}
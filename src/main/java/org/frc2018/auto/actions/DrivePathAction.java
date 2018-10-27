package org.frc2018.auto.actions;

import org.frc2018.Vector2;
import org.frc2018.path.Path;
import org.frc2018.path.PathFollower;
import org.frc2018.subsystems.Drivetrain;

public class DrivePathAction extends Action {

    private Path m_path;
    private PathFollower m_path_follower;
    private Vector2 init_position;
    private double init_angle;

    public DrivePathAction(Path path, double timeout_ms) {
        super(timeout_ms);
        m_path = path;
        m_path_follower = new PathFollower(m_path);
    }   

    @Override
    public void start() {
        super.start();
        init_position = Drivetrain.getInstance().getRobotPosition();
        init_angle = Drivetrain.getInstance().getGyroAngle();
        Drivetrain.getInstance().setBrakeMode(true);
        System.out.println(m_path);
        Drivetrain.getInstance().setVelocity(10, 10);
    }

    @Override
    public void update() {
        Vector2 relative_robot_pos = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
        double[] velocities = m_path_follower.update(relative_robot_pos, Drivetrain.getInstance().getGyroAngle() - init_angle);
        Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
    }

    @Override
    public boolean next() {
        Vector2 relative_robot_pos = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
        return (super.timedOut() || m_path_follower.doneWithPath(relative_robot_pos)) ? true : false;
    }

    @Override
    public void finish() {
        Drivetrain.getInstance().stop();
    }

    @Override
    public void reset() {
        super.reset();
    }

}
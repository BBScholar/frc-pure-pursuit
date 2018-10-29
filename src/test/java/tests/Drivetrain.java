package tests;

import org.frc2018.Vector2;
import org.frc2018.Constants;

public class Drivetrain {
    private static Drivetrain _instance = new Drivetrain();
    public static Drivetrain getInstance() {
        return _instance;
    }

    private double m_left_velocity, m_right_velocity;
    private double m_left_distance, m_right_distance;
    private double m_angle;

    private Drivetrain() {
        m_left_velocity = 0;
        m_right_velocity = 0;
        m_left_distance = 0;
        m_right_distance = 0;
        m_angle = 0;
    }

    public void update(double time_interval) {
        double old_left_distance = m_left_distance;
        double old_right_distance = m_right_distance;
        m_left_distance += m_left_velocity * time_interval;
        m_right_distance += m_right_velocity * time_interval;
        double left_delta = m_left_distance - old_left_distance;
        double right_delta = m_right_distance - old_right_distance;
        m_angle += Math.toDegrees((right_delta - left_delta) / Constants.TRACK_WIDTH);
        Position.getInstance().update(m_left_distance, m_right_distance, m_angle);
    }

    public void setVelocity(double left, double right) {
        m_left_velocity = left;
        m_right_velocity = right;
    }

    public double getRobotAngle() {
        return m_angle;
    }

    public void setRobotAngle(double angle) {
        m_angle = angle;
        Position.getInstance().setInitialAngle(angle);
    }

    public void setRobotPosition(Vector2 position) {
        Position.getInstance().setPositionVector(position);
    }

    public Vector2 getRobotPosition() {
        return Position.getInstance().getPositionVector();
    }
}

class Position {

    private double x, y;
    private double last_left, last_right;
    private double last_angle;

    private Position() {
        x = 0;
        y = 0;
        
        last_left = 0;
        last_right = 0;
        last_angle = 0;
    }

    public void update(double left_distance, double right_distance, double angle) {
        angle = Math.toRadians(angle);
        double angle_delta = angle - last_angle;
        if(angle_delta == 0) angle_delta = Constants.EPSILON;
        double left_delta = left_distance - last_left;
        double right_delta = right_distance - last_right;
        double distance = (left_delta + right_delta) / 2.0;
        double radius_of_curvature = distance / angle_delta;
        double delta_y = radius_of_curvature * Math.sin(angle_delta);
        double delta_x = radius_of_curvature * (Math.cos(angle_delta) - 1);
        y -= delta_x * Math.cos(last_angle) - delta_y * Math.sin(last_angle);
        x +=  delta_x * Math.sin(last_angle) + delta_y * Math.cos(last_angle);
        last_left = left_distance;
        last_right = right_distance;
        last_angle = angle;
    }

    public Vector2 getPositionVector() {
        return new Vector2(x, y);
    }

    public void setPositionVector(Vector2 position) {
        x = position.x;
        y = position.y;
    }

    public void setInitialAngle(double angle) {
        last_angle = angle;
    }

    public void reset() {
        x = 0;
        y = 0;
        last_left = 0;
        last_right = 0;
        last_angle = 0;
    }

    @Override
    public String toString() {
        return String.format("Robot Position: X: %.2f, Y:%.2f ", x, y);
    }

    private static Position _instance = new Position();

    public static Position getInstance() {
        return _instance;
    }
}
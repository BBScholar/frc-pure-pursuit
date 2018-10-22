package org.frc2018;

import org.frc2018.math.Vector2;

public class Position {

    /*
    * Singleton design pattern
    */
    private static Position _instance = new Position();

    public static Position getInstance() {
        return _instance;
    }

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

    public void update(double left_value, double right_value, double angle) {
        angle = Math.toRadians(angle); // Calculate current angle of the robot
        double angle_delta = angle - last_angle; // Calculate angle delta
        if(angle_delta == 0) angle_delta = Constants.EPSILON; // Make sure we don't get a devide by zero error
        double left_delta = left_value - last_left; // Calculate left drive delta
        double right_delta = right_value - last_right; // Calculate right drive delta
        double distance = (left_delta + right_delta) / 2.0; // calculate average distance delta of the robot
        double radius_of_curvature = distance / angle_delta; // Calculate the radius that the robot is currently turning around
        double delta_y = radius_of_curvature * Math.sin(angle_delta); // Delta y of the robot relative to the last robot position and angle
        double delta_x = radius_of_curvature * (Math.cos(angle_delta) - 1); // Delta x of the robot relative to the last robot position and angle
        x += delta_x * Math.cos(last_angle) - delta_y * Math.sin(last_angle); // Rotate coordinates so that they are relative to the field
        y +=  delta_x * Math.sin(last_angle) + delta_y * Math.cos(last_angle); // Rotate coordinates so that they are relative to the field
        last_left = left_value;
        last_right = right_value;
        last_angle = angle;
    }

    public Vector2 getPosition() {
        return new Vector2(x, y);
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

}


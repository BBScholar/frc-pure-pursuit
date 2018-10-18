package org.frc2018;

import org.frc2018.math.Vector2;

public class Position {

    private static Position _instance = new Position();
    private static double EPSILON = 0.0001;

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
        angle = Math.toRadians(angle);
        double angle_delta = angle - last_angle;
        if(angle_delta == 0) angle_delta = Position.EPSILON;
        double left_delta = left_value - last_left;
        double right_delta = right_value - last_right;
        double distance = (left_delta + right_delta) / 2.0;
        double radius_of_curvature = distance / angle_delta;
        double delta_y = radius_of_curvature * Math.sin(angle_delta);
        double delta_x = radius_of_curvature * (Math.cos(angle_delta) - 1);
        x += delta_x * Math.cos(last_angle) - delta_y * Math.sin(last_angle);
        y +=  delta_x * Math.sin(last_angle) + delta_y * Math.cos(last_angle);
        last_left = left_value;
        last_right = right_value;
        last_angle = angle;
    }

    public void setPosition(Vector2 pos) {
        x = pos.x;
        y = pos.y;
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


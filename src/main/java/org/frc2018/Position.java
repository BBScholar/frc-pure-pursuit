package org.frc2018;

import org.frc2018.math.Vector2;

public class Position {

    private static Position _instance = new Position();

    public static Position getInstance() {
        return _instance;
    }

    private double x, y;
    private double last_left, last_right;

    private Position() {
        x = 0;
        y = 0;

        last_left = 0;
        last_right = 0;
    }

    public void update(double left_value, double right_value, double angle) {
        angle = Math.toRadians(angle);
        double left_delta = left_value - last_left;
        double right_delta = right_value - last_right;
        double distance = (left_delta + right_delta) / 2.0;
        x += distance * Math.cos(angle);
        y +=  distance * Math.sin(angle);
        last_left = left_value;
        last_right = right_value;
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
    }

    @Override
    public String toString() {
        return String.format("Robot Position: X: %.3f, Y:%.3f ", x, y);
    }

}


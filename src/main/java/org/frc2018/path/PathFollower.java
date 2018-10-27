package org.frc2018.path;

import java.util.Map;
import java.util.Optional;

import org.frc2018.Constants;
import org.frc2018.Vector2;
import org.frc2018.path.Path;


public class PathFollower {

    private Path m_path;
    private int m_last_closest_point_index;

    public PathFollower(Path path) {
        m_path = path;
        m_last_closest_point_index = 0;
    }

    private Vector2 calculateLookahead(Vector2 robot_pos, double robot_angle) {
        m_last_closest_point_index = m_path.findClosestPointIndex(robot_pos, m_last_closest_point_index);
        Vector2 lookahead = null;
        for(int i = m_last_closest_point_index; i < m_path.getPathLength() - 1; i++) {
            Vector2 begin = m_path.getPoint(i);
            Vector2 end =  m_path.getPoint(i + 1);
            Vector2 d = Vector2.subtract(end, begin);
            Vector2 f = Vector2.subtract(begin, robot_pos);

            /**
             * find the distance from the 
             */
            double a = Vector2.dot(d,d);
            double b = 2.0 * Vector2.dot(f, d);
            double c = Vector2.dot(f, f) - Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2);
            double dis = (b * b) - (4.0 * a * c);

            if(dis < 0) {
                continue;
            } else {
                dis = Math.sqrt(dis);
                double t1 = (-b - dis) / (2.0 * a);
                double t2 = (-b + dis) / (2.0 * a);

                if(t1 >= 0 && t1 <= 1) {
                    Vector2 temp = Vector2.multiply(d, t1);
                    lookahead = Vector2.add(begin, temp);
                    break;
                } else if(t2 >= 0 && t2 <= 1) {
                    Vector2 temp = Vector2.multiply(d, t2);
                    lookahead = Vector2.add(begin, temp);
                    break;
                }
            }

        }

        if(lookahead == null) {
            lookahead = m_path.getPoint(m_path.getPathLength() - 1);
            // System.out.println("Using Last Lookahead: " + lookahead);
        } else {
            double distance_between_robot_end = Vector2.distanceBetween(robot_pos, m_path.getPoint(m_path.getPathLength() - 1));
            if (distance_between_robot_end < Constants.LOOK_AHEAD_DISTANCE) {
                lookahead = m_path.getPoint(m_path.getPathLength() - 1);
            }

            Vector2 robot_to_lookahead = Vector2.subtract(lookahead, robot_pos);
            Vector2 robot_direction = Vector2.representHeadingWithUnitVector(-Math.toDegrees(robot_angle) + 90);
            double angle_to_lookahead = Math.abs(Vector2.angleBetween(robot_to_lookahead, robot_direction));
            // System.out.println(angle_to_lookahead);
        }
        return lookahead;
    }

    private double calculateCurvature(Vector2 robot_pos, Vector2 look_ahead, double robot_angle) {
        double a = (1 / Math.tan(robot_angle));
        double b = -1;
        double c = -(1 / Math.tan(robot_angle)) * robot_pos.y + robot_pos.x;

        double x = Math.abs(a * look_ahead.y + b * look_ahead.x + c) / (Math.sqrt(a * a + b * b));
        double curvature = (2.0 * x) / (Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2));

        double side = Math.signum(Math.sin(robot_angle) * (look_ahead.x - robot_pos.x) - Math.cos(robot_angle) * (look_ahead.y - robot_pos.y));
        return curvature * side;
    }

    // public double getTargetVelocity(Vector2 robot_pos) {

    // }

    public double[] update(Vector2 robot_pos, double robot_angle) {
        double[] output = new double[2];
        robot_angle = Math.toRadians(robot_angle);
        Vector2 lookahead = calculateLookahead(robot_pos, robot_angle);
        double curvature = calculateCurvature(robot_pos, lookahead, robot_angle);

        output[0] = m_path.getPointVelocity(m_last_closest_point_index) * (2.0 + (curvature * Constants.TRACK_WIDTH)) / 2.0;
        output[1] = m_path.getPointVelocity(m_last_closest_point_index) * (2.0 - (curvature * Constants.TRACK_WIDTH)) / 2.0;

        return output;
    }

    public boolean doneWithPath(Vector2 robot_pos) {
        double distance_between_robot_end = Vector2.distanceBetween(robot_pos, m_path.getPoint(m_path.getPathLength() - 1));
        return distance_between_robot_end < Constants.LOOK_AHEAD_DISTANCE;
    }

}
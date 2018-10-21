package org.frc2018.path;

import java.util.Map;
import java.util.Optional;

import org.frc2018.Constants;
import org.frc2018.math.Vector2;
import org.frc2018.path.Path;

public class PathFollower {

    private Path m_path;
    private Vector2 last_lookahead;

    public PathFollower(Path path) {
        m_path = path;
        last_lookahead = m_path.getPoint(1);
        //last_lookahead = null;
    }

    private Vector2 calculateLookahead(Vector2 robot_pos) {
        int index = m_path.findClosestPointIndex(robot_pos);
        Vector2 lookahead = null;
        for(int i = index; i < m_path.getPathLength() - 1; i++) {
            Vector2 begin = m_path.getPoint(i);
            Vector2 end =  m_path.getPoint(i + 1);
            Vector2 d = Vector2.subtract(end, begin);
            Vector2 f = Vector2.subtract(begin, robot_pos);

            double a = Vector2.dot(d,d);
            double b = 2.0 * Vector2.dot(f, d);
            double c = Vector2.dot(f, f) - Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2);
            double dis = b*b - 4.0 * a * c;

            if(dis < 0) {
                continue;
            } else {
                dis = Math.sqrt(dis);
                double t1 = (-b - dis) / (2.0 * a);
                double t2 = (-b + dis) / (2.0 * a);

                if(t1 >= 0 && t1 <= 1) {
                    Vector2 temp = Vector2.multiply(d, t1);
                    lookahead = Vector2.add(begin, temp);
                    last_lookahead = Vector2.copyVector(lookahead);
                    break;
                } 

                if(t2 >= 0 && t2 <= 1) {
                    Vector2 temp = Vector2.multiply(d, t2);
                    lookahead = Vector2.add(begin, temp);
                    last_lookahead = Vector2.copyVector(lookahead);
                    break;
                }

                continue;
            }

        }

        if(lookahead == null) {
            lookahead = last_lookahead;
        }
        return lookahead;

    }

    private double calculateCurvature(Vector2 robot_pos, Vector2 look_ahead, double robot_angle) {
        // calculate curvature
        //double a = -1 / Math.max(Math.tan(robot_angle), Constants.EPSILON);
        //double b = -1;
        //double c = robot_pos.x / Math.max(Math.tan(robot_angle), Constants.EPSILON) + robot_pos.y;

        double a = -Math.tan(robot_angle);
        double b = 1;
        double c = Math.tan(robot_angle) * robot_pos.x - robot_pos.y;

        double x = Math.abs(a * look_ahead.x + b * look_ahead.y + c) / (Math.sqrt(a * a + b * b));
        double curvature = (2.0 * x) / (Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2));
        //curvature *= 10;
        // calculate side mult
        double side = Math.signum(Math.sin(robot_angle) * (look_ahead.x - robot_pos.x) - Math.cos(robot_angle) * (look_ahead.y - robot_pos.y));
        //System.out.println("Curvature: " + curvature + ", side: " + side);
        // return
        return curvature * side;
    }

    public VelocitySetpoint update(Vector2 robot_pos, double robot_angle) {
        Vector2 lookahead = calculateLookahead(robot_pos);
        double curvature = calculateCurvature(robot_pos, lookahead, robot_angle);
        //curvature *= 2.0;
        //System.out.println("Lookahead: " + lookahead);
        VelocitySetpoint set = new VelocitySetpoint();
        set.left_velocity = m_path.getClosestPointVelocity(robot_pos) * (2.0 - (curvature * Constants.TRACK_WIDTH)) / 2.0;
        set.right_velocity = m_path.getClosestPointVelocity(robot_pos) * (2.0 + (curvature * Constants.TRACK_WIDTH)) / 2.0;
        //System.out.println();
        //System.out.println( "lookahead:  " + lookahead + ", Robot position: " + robot_pos + ", closest point index: " + m_path.findClosestPointIndex(robot_pos) + ", curvature: " + curvature);
        if(m_path.getBackwards()) {
            set.left_velocity *= -1;
            set.right_velocity *= -1;
        }
        return set;
    }
    
    /*
    public void setPath(Path path) {
        m_path = path;
    }
    */

    public boolean doneWithPath(Vector2 robot_pos) {
        return m_path.doneWithPath(robot_pos);
    }

    public static class VelocitySetpoint {
        public double left_velocity;
        public double right_velocity;
    }

}
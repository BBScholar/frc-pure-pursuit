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
        Vector2 closest = m_path.getClosestPoint(robot_pos);
        Vector2 next = m_path.getNextPoint(robot_pos);
        Vector2 d = Vector2.subtract(next, closest);
        Vector2 f = Vector2.subtract(closest, robot_pos);
        double a = Vector2.dot(d,d);
        double b = 2.0 * Vector2.dot(f, d);
        double c = Vector2.dot(f, f) - Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2);
        double dis = b*b - 4.0 * a * c;

        if(dis < 0) {
            // return new Vector2(0, 0);
            return last_lookahead;
        } else {
            dis = Math.sqrt(dis);
            double t1 = (-b - dis) / (2 * a);
            double t2 = (-b + dis) / (2 * a);

            if(t1 >= 0 && t1 <= 1) {
                // return t1 intersection
                d.multiply(t1);
                Vector2 new_look_ahead = Vector2.add(closest, d);
                last_lookahead = Vector2.copyVector(new_look_ahead);
                return new_look_ahead;
            }
            if(t2 >= 0 && t2 <= 1) {
                // return t2 intersection
                d.multiply(t2);
                Vector2 new_look_ahead = Vector2.add(closest, d);
                last_lookahead = Vector2.copyVector(new_look_ahead);
                return new_look_ahead;
            }

            // no intersection
            return last_lookahead;
        }

    }

    private double calculateCurvature(Vector2 robot_pos, Vector2 look_ahead, double robot_angle) {
        // calculate curvature
        double a = -Math.tan(robot_angle);
        double b = 1;
        double c = Math.tan(robot_angle) * robot_pos.x - robot_pos.y;
        double x = Math.abs(a * look_ahead.x + b * look_ahead.y + c) / (Math.sqrt(a * a + b * b));
        double curvature = (2.0 * x) / (Math.pow(Constants.LOOK_AHEAD_DISTANCE, 2));

        // calculate side mult
        double side = Math.signum(Math.sin(robot_angle) * (look_ahead.x - robot_pos.x) - Math.cos(robot_angle) * (look_ahead.y - robot_pos.y));
        
        // return
        return curvature * side;
    }

    public VelocitySetpoint update(Vector2 robot_pos, double robot_angle) {
        Vector2 lookahead = calculateLookahead(robot_pos);
        double curvature = calculateCurvature(robot_pos, lookahead, robot_angle);
        VelocitySetpoint set = new VelocitySetpoint();
        set.left_velocity = m_path.getClosestPointVelocity(robot_pos) * (2.0 + (curvature * Constants.TRACK_WIDTH)) / 2.0;
        set.right_velocity = m_path.getClosestPointVelocity(robot_pos) * (2.0 - (curvature * Constants.TRACK_WIDTH)) / 2.0;
        // System.out.println(lookahead);
        System.out.println("Robot position: " + robot_pos + " - Closest point index: " + m_path.findClosestPointIndex(robot_pos));
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
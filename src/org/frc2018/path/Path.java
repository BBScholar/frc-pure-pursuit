package org.frc2018.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.frc2018.math.Translation2d;

public class Path {

    // constants
    private static final double kSpacing = 6.0; // inches
    private static final double kTolerance = 0.001; 
    private static final double defaultKa = 0.85;
    private static final double kMaxPathVelocity = 120.0; // in/s
    private static final double velocityKConstant = 2.0; // 1 - 5
    private static final double kMaxPathAcceleration = 12.0; // in/s

    // waypoint list
    private double[][] path;  // x y coords
    private double[][] smooth_path; // smoothed x y coords
    private double[] distances; // distance along path at each point
    private double[] curvature;  // cruvature at each point
    private double[] max_velocity; // max velocity at each point
    private double[] target_velocity; // target velocity at each point

    public Path(List<Translation2d> points, double a) {
        this.path = injectPoints(translationListToArray(points));
        this.smooth_path = smoothPath(this.path, a, 1 - a, 0.001);
        this.distances = distanceAlongPath(smooth_path);
        this.curvature = curvatureAtPoints(smooth_path);
        
    }

    public Path(List<Translation2d> points) {
        this(points, defaultKa);
    }


    private static double[][] injectPoints(double[][] path) {
        List<Translation2d> newPoints = new ArrayList<>();
        for(int i = 0; i < path.length - 1; i++) {
            Translation2d start = new Translation2d(path[i][0], path[i][1]);
            Translation2d end = new Translation2d(path[i+1][0], path[i+1][1]);
            Translation2d v = new Translation2d(start, end);
            int points_that_fit = (int) Math.ceil(v.mag() / kSpacing);
            v = v.normalize().scale(kSpacing);
            for (int j = 0; j < points_that_fit; j++) {
                newPoints.add(new Translation2d( start.x() + v.x() * i, start.y() + v.y() * i));
            }
        }
        newPoints.add(new Translation2d(path[path.length][0], path[path.length][1]));
        return translationListToArray(newPoints);
    }

    private static double[][] smoothPath(double[][] path, double a, double b, double tolerance){
        //copy array
        double[][] newPath = doubleArrayCopy(path);
        double change = tolerance;
        while(change >= tolerance){
            change = 0.0;
            for(int i = 1; i<path.length-1; i++)
                for(int j = 0; j<path[i].length; j++){
                    double aux = newPath[i][j];
                    newPath[i][j] += a * (path[i][j] - newPath[i][j]) + b *(newPath[i-1][j] + newPath[i+1][j] - (2.0 * newPath[i][j]));change += Math.abs(aux - newPath[i][j]);
                }
            }
        return newPath;
    }

    private static double[] distanceAlongPath(double[][] path) {
        double[] result = new double[path.length];
        result[0] = 0;
        for (int i = 1; i < path.length; i++) {
            Translation2d start = new Translation2d(path[i - 1][0], path[i - 1][1]);
            Translation2d end = new Translation2d(path[i][0], path[i][1]);
            Translation2d v = new Translation2d(start, end);
            result[i] = result[i - 1] + v.mag();
        }

        return result;
    }

    private static double[] curvatureAtPoints(double[][] path) {
        double[] result = new double[path.length];
        for(int i = 1; i < path.length - 1; i++) {
            // point we're trying to get curavture for (x1, y1)
            Translation2d P = new Translation2d(path[i][0], path[i][1]);
            // point behind (x2, y2)
            Translation2d Q = new Translation2d(path[i - 1][0], path[i - 1][1]);
            // point infront (x3, y3)
            Translation2d R = new Translation2d(path[i + 1][0], path[i + 1][1]);
            
            if(P.x() == Q.x()) {
                P.setX(P.x() + 1E-10);
            }

            double k1 = 0.5 * (Math.pow(P.x(), 2) + Math.pow(P.y(), 2) - Math.pow(Q.x(), 2) - Math.pow(Q.y(), 2)) / (P.x() - Q.x());
            double k2 = (P.y() - Q.y()) / (P.x() - Q.x());
            double b = 0.5 * (Math.pow(P.x(),2) - 2.0 * Q.x() * k1 + Math.pow(Q.y(), 2) - Math.pow(R.x(), 2) + 2.0 * R.x() * k1 - Math.pow(R.y(), 2)) 
                / (R.x() * k2 - R.y() + Q.y() - Q.x() * k2);
            double a = k1 - k2 * b;
            double r = Math.hypot((P.x() - a), (P.y() - b));
            result[i] = 1.0 / r;
        }
        return result;
    }

    private static double[][] doubleArrayCopy(double[][] array) {
        double[][] result = new double[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                result[i][j] = array[i][j];
            }
        }

        return result;
    }



    private static double[][] translationListToArray(List<Translation2d> points) {
        double[][] result = new double[points.size()][2];
        int i = 0;
        for(Translation2d p : points) {
            result[i][0] = p.x();
            result[i][1] = p.y();
        }
        return result;
    }


}
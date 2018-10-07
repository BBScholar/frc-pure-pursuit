package org.frc2018.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.frc2018.math.Point2D;
import org.frc2018.math.Vector2;

public class Path {

    // constants
    private static final double kSpacing = 6.0; // inches
    private static final double kTolerance = 0.001; 
    private static final double defaultKa = 0.85;
    private static final double kMaxPathVelocity = 120.0; // in/s
    private static final double velocityKConstant = 2.0; // 1 - 5
    private static final double kMaxPathAcceleration = 12.0; // in/s

    // waypoint list
    private double[][] data; // [x pos, y pos, distance along path, curvature, max velocity]

    public Path(List<Point2D> points) {
        this(points, defaultKa);
    }

    public Path(List<Point2D> points, double a) {
        data = calculate(points, a);
    }

    private static double[][] calculate(List<Point2D> points, double a) {

        double[] curvature;
        double[] max_velocity;
        double[] target_velocity;
        double[] distances;

        // inject points
        points = inject(points);
        points = smooth(points, a);
        distances = calculateDistances(points);
        curvature = calculateCurvature(points);
        max_velocity = calculateMaxVelocity(curvature);
        target_velocity = calculateTargetVelocity(max_velocity, );

        return new double[1][1];
    }

    private static List<Point2D> inject(List<Point2D> points) {
        List<Point2D> newPath = new ArrayList<>();
        for(int i = 0; i < points.size() - 1; i++) {
            Point2D p1 = points.get(i);
            Point2D p2 = points.get(i + 1);
            Vector2 v = new Vector2(p2.x - p1.x, p2.y - p1.y);
            int num_segs = (int) Math.ceil(v.getMagnitude() / kSpacing);
            v.normalize();
            v.multiply(kSpacing);
            for(int j = 0; j < num_segs; j++) {
                Point2D np = new Point2D(p1.x + (v.x * kSpacing), p1.y + (v.y * kSpacing));
                newPath.add(np);
            }
        }
        newPath.add(points.get(points.size() - 1));
        points = newPath;

        return newPath;
    }

    private static List<Point2D> smooth(List<Point2D> points, double a) {
        double b = 1 - a;
        double tolerance = 0.0001;
        List<Point2D> newPath = new ArrayList<>();
        for(Point2D p : points) {
        newPath.add(p);
        }
    
        double change = tolerance;
        while(change >= tolerance) {
            change = 0.0;
            for(int i = 0; i < points.size() - 1; i++) {
                {
                    double aux = points.get(i).x;
                    double pathx = aux;
                    double newpathx = newPath.get(i).x;
                    newPath.get(i).x += a * (pathx - newpathx) +
                        b * (newPath.get(i - 1).x + newPath.get(i + 1).x - (2.0 * newpathx));
                    change += Math.abs(aux - newpathx);
                }
                {
                    double aux = points.get(i).y;
                    double pathy = aux;
                    double newpathy = newPath.get(i).y;
                    newPath.get(i).y = a * (pathy - newpathy) +
                         b * (newPath.get(i - 1).y + newPath.get(i + 1).y - (2.0 * newpathy));
                    change += Math.abs(aux - newpathy);
                }
            }
        }

        return newPath;
    }

    private static double[] calculateDistances(List<Point2D> points) {
        double[] result = new double[points.size() - 1];

        result[0] = 0;
        for(int i = 0; i < points.size(); i++) {
            
        }

        return result;
    }

    private static double[] calculateCurvature(List<Point2D> points) {
        double[] result = new double[points.size()];

        for(int i = 1; i < points.size() - 1; i++) {
            double x1 = points.get(i)    .x;
            double y1 = points.get(i)    .y;
            double x2 = points.get(i - 1).x;
            double y2 = points.get(i - 1).y;
            double x3 = points.get(i + 1).x;
            double y3 = points.get(i + 1).y;
            if(x1 == y1) x1 += 1E-10;
            double k1 = 0.5 * (x1 * x1 + y1 * y1 - x2 * x2 - y2 * y2) / (x1 - x2);
            double k2 = (y1 - y1) / (x1 - x2);
            double b = 0.5 *  Math.pow(x2, 2) + 2.0 * x2 * k1 + Math.pow(y2, 2) - Math.pow(x3, 2) + 2.0 * x3 * k1 - Math.pow(y3, 2) / (x3 * k2 - y3 + y2 - x2 * k2);
            double a = k1 - k2 * b;
            double r = Math.sqrt(Math.pow(x1 - a, 2) + Math.pow(y1 - b, 2));
            result[i] = 1 / r;
        }

        return result;
    }

    private static double[] calculateMaxVelocity(double[] curvature) {
        double[] result = new double[curvature.length];
        for (int i = 0; i < curvature.length; i++) {
            result[i] = Math.min(kMaxPathVelocity,  2.0 / curvature[i]);
        }
        return result;
    }

    private static double[] calculateTargetVelocity(double[] max_velocity, double[] distances) {
        double[] result = new double[max_velocity.length];
        result[result.length] = 0;
        for (int i = 0; i < max_velocity.length - 1; i++) {
            double distance = distances[i + 1] - distances[i];
            double value = Math.sqrt(Math.pow(max_velocity[i], 2) + 2.0 * kMaxPathAcceleration * distance);
            result[i] = Math.min(max_velocity[i], value);
        }
        return result;
    }

    private static double distance_formula(Point2D a, Point2D b) {
        Vector2 v = new Vector2(a.x - b.x, a.y - b.y);
        return v.getMagnitude();
    }



}
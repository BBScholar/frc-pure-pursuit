package org.frc2018.path;

import java.util.ArrayList;
import java.util.List;

import org.frc2018.math.Point2D;
import org.frc2018.math.Vector2;

public class Path {

    // constants
    private static final double kSpacing = 6.0; // inches
    private static final double kTolerance = 0.001; 
    private static final double defaultKa = 0.85;

    // waypoint list
    private double[][] data; // [x pos, y pos, distance along path]

    private double[] distance_between;


    public Path(List<Point2D> points) {
        this(points, defaultKa);
    }

    public Path(List<Point2D> points, double a) {
        points = injectPoints(points);
        points = smoothPoints(points, a, 1 - a, kTolerance);
        distance_between = calculateDistances(points);
    }

    private static List<Point2D> injectPoints(List<Point2D> points) {
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
        return newPath;
    }

    private static List<Point2D> smoothPoints(List<Point2D> path, double a, double b, double tolerance) {
        List<Point2D> newPath = new ArrayList<>();
        for(Point2D p : path) {
            newPath.add(p);
        }

        double change = tolerance;
        while(change >= tolerance) {
            change = 0.0;
            for(int i = 0; i < path.size() - 1; i++) {
                {
                    double aux = path.get(i).x;
                    double pathx = aux;
                    double newpathx = newPath.get(i).x;
                    newPath.get(i).x += a * (pathx - newpathx) +
                        b * (newPath.get(i - 1).x + newPath.get(i + 1).x - (2.0 * newpathx));
                    change += Math.abs(aux - newpathx);
                }
                {
                    double aux = path.get(i).y;
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

    private static double[] calculateDistances(List<Point2D> path) {
        double[] result = new double[path.size()];
        for(int i = 1; i < result.length; i++) {
            result[i] = result[i - 1] + distance_formula(path.get(i), path.get(i - 1));
        }
        return result;
    }

    private static double distance_formula(Point2D a, Point2D b) {
        Vector2 v = new Vector2(a.x - b.x, a.y - b.y);
        return v.getMagnitude();
    }

    private static double[][] arraylistTo2dArray(List<Point2D> path) {
        return new double[1][1];
    }


}
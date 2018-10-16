package org.frc2018.path;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;

import org.frc2018.math.Vector2;

public class Path {

    private Vector2[] coordinates;
    private double[] target_velocities;

    private int last_closest_index = 0;

    private boolean backwards;

    public Path(String filepath) {
        this(filepath, false);
    }

    public Path(String filepath, boolean backwards) {
        this.backwards = backwards;

        List<Vector2> temp_coords = new ArrayList<>();
        List<Double> temp_velo = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filepath));
            String[] line = reader.readNext();
            while(line!=null) {
                temp_coords.add(new Vector2( Double.parseDouble(line[0]), Double.parseDouble(line[1])));
                temp_velo.add(Double.parseDouble(line[2]));
                line = reader.readNext();
            }
            reader.close();

            coordinates = new Vector2[temp_coords.size()];
            target_velocities = new double[temp_velo.size()];

            for(int i = 0; i < temp_coords.size(); i++) {
                coordinates[i] = temp_coords.get(i);
                target_velocities[i] = temp_velo.get(i);
            }


        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public Vector2 getClosestPoint(Vector2 robot_pos) {
        return coordinates[findClosestPointIndex(robot_pos)];
    }

    public Vector2 getNextPoint(Vector2 robot_pos) {
        return coordinates[findClosestPointIndex(robot_pos) + 1];
    }

    public double getClosestPointVelocity(Vector2 robot_pos) {
        return target_velocities[findClosestPointIndex(robot_pos)];
    }

    public boolean doneWithPath(Vector2 robot_pos) {
        if(findClosestPointIndex(robot_pos) == coordinates.length - 1) {
            return true;
        }
        return false;
    }

    private int findClosestPointIndex(Vector2 robot_pos) {
        int index = last_closest_index;
        Vector2 last_closest = coordinates[index];
        double min_distance = Vector2.distanceBetween(robot_pos, last_closest);
        for(int i = index; i < coordinates.length; i++) {
            Vector2 temp = coordinates[i];
            double temp_distance = Vector2.distanceBetween(robot_pos, temp);
            if(temp_distance <  min_distance) {
                index = i;
                min_distance = temp_distance;
            }
        }
        return index;
    }

    public boolean getBackwards() {
        return this.backwards;
    }

    public void setBackwards(boolean backwards) {
        this.backwards = backwards;
    }

}
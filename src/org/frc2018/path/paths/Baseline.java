package org.frc2018.path.paths;

import java.util.ArrayList;

import org.frc2018.math.RigidTransform2d;
import org.frc2018.math.Rotation2d;
import org.frc2018.math.Translation2d;
import org.frc2018.path.Path;
import org.frc2018.path.PathBuilder;
import org.frc2018.path.PathContainer;
import org.frc2018.path.PathBuilder.Waypoint;

public class Baseline implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(16, 160, 0, 0));
        sWaypoints.add(new Waypoint(86, 160, 0, 60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(16, 160), Rotation2d.fromDegrees(180.0));
    }

    @Override
    public boolean isReversed() {
        return true;
    }

}
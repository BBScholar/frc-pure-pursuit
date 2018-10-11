package org.frc2018.path.paths;


import java.util.ArrayList;

import org.frc2018.math.RigidTransform2d;
import org.frc2018.math.Rotation2d;
import org.frc2018.math.Translation2d;
import org.frc2018.path.Path;
import org.frc2018.path.PathBuilder;
import org.frc2018.path.PathContainer;
import org.frc2018.path.PathBuilder.Waypoint;

public class Left_Switch_Grab_Cube implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(100,200,0,0));
        sWaypoints.add(new Waypoint(75,200,10,60));
        sWaypoints.add(new Waypoint(75,180,10,60));
        sWaypoints.add(new Waypoint(100,160,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(100, 200), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return true; 
    }
	// WAYPOINT_DATA: [{"position":{"x":100,"y":200},"speed":0,"radius":0,"comment":""},{"position":{"x":75,"y":200},"speed":60,"radius":10,"comment":""},{"position":{"x":75,"y":180},"speed":60,"radius":10,"comment":""},{"position":{"x":100,"y":160},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: true
	// FILE_NAME: Left_Switch_Cube1
}

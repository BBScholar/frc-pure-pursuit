package org.frc2018.path.paths;


import java.util.ArrayList;

import org.frc2018.math.RigidTransform2d;
import org.frc2018.math.Rotation2d;
import org.frc2018.math.Translation2d;
import org.frc2018.path.Path;
import org.frc2018.path.PathBuilder;
import org.frc2018.path.PathContainer;
import org.frc2018.path.PathBuilder.Waypoint;

public class Left_Switch_Cube1 implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(10 ,150,0 ,0));
        sWaypoints.add(new Waypoint(50 ,150,15,60));
        sWaypoints.add(new Waypoint(70 ,200,15,60));
        sWaypoints.add(new Waypoint(100,200,0 ,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(10, 150), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return true; 
    }
	// WAYPOINT_DATA: [{"position":{"x":10,"y":150},"speed":0,"radius":0,"comment":""},{"position":{"x":30,"y":150},"speed":60,"radius":10,"comment":""},{"position":{"x":50,"y":200},"speed":60,"radius":10,"comment":""},{"position":{"x":100,"y":200},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: true
	// FILE_NAME: Left_Switch_Cube1
}

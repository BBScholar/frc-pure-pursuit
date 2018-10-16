package org.frc2018.auto.actions;

import org.frc2018.path.Path;
import org.frc2018.subsystems.Drive;

public class DrivePathAction implements Action {

    private Path m_path;

    public DrivePathAction(Path path) {
        m_path = path;
    }   

    @Override
    public void start() {
        Drive.getInstance().setWantDrivePath(m_path);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        System.out.println("Done with path!");
        return Drive.getInstance().doneWithPath();
    }

    @Override
    public void done() {
        
    }



}
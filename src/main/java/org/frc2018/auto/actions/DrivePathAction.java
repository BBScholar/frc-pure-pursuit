package org.frc2018.auto.actions;

import org.frc2018.path.Path;
import org.frc2018.subsystems.Drivetrain;

public class DrivePathAction extends Action {

    private Path m_path;

    public DrivePathAction(Path path) {
        m_path = path;
    }   

    @Override
    public void start() {
        Drivetrain.getInstance().setWantDrivePath(m_path);
    }

    @Override
    public void update() {

    }

    @Override
    public boolean isFinished() {
        boolean ret = Drive.getInstance().doneWithPath();
        if(ret){
            System.out.println("Done with path!");
        }
        return ret;
    }

    @Override
    public void done() {
        
    }



}
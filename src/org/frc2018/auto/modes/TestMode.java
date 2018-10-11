package org.frc2018.auto.modes;

import org.frc2018.auto.AutoModeBase;
import org.frc2018.auto.AutoModeEndedException;
import org.frc2018.auto.commands.DrivePathCommand;
import org.frc2018.path.paths.*;

public class TestMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException { 
        runAction(new DrivePathCommand(new Left_Switch_Cube1()));
        runAction(new DrivePathCommand(new Left_Switch_Grab_Cube()));
    }
}
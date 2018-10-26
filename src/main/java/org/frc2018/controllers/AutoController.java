package org.frc2018.controllers;

import org.frc2018.auto.actions.DrivePathAction;
import org.frc2018.auto.routines.Routine;
import org.frc2018.path.Path;

public class AutoController extends Controller {

    private DrivePathAction drive_action;

    private AutoController() {

    }

    @Override
    public void init() {
        Path path = new Path("/home/lvuser/paths/path.csv");
        System.out.println(path);
        drive_action = new DrivePathAction(path, 1000);
        drive_action.start();
    }

    @Override
    public void handle() {
        if(drive_action.next() == Routine.NOT_FINISHED) {
            drive_action.update();
        } else {
            drive_action.finish();
        }
    }

    private static AutoController _instance = new AutoController();

    public static AutoController getInstance() {
        return _instance;
    }
}
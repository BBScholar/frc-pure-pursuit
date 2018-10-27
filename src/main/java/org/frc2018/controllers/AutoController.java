package org.frc2018.controllers;

import org.frc2018.auto.actions.Action;
import org.frc2018.auto.actions.ArmAction;
import org.frc2018.auto.actions.DrivePathAction;
import org.frc2018.auto.actions.ArmAction.ArmDirection;
import org.frc2018.auto.actions.ArmAction.IntakeDirection;
import org.frc2018.auto.routines.Routine;
import org.frc2018.path.Path;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoController extends Controller {

    private Routine left_one_cube = new Routine("left_one_cube");
    private Routine right_one_cube = new Routine("right_one_cube");

    private Routine current_routine;
    private Action current_action;

    private boolean is_finished;

    private AutoController() {
        left_one_cube.addAction(new DrivePathAction(new Path("/home/lvuser/paths/center_to_left.csv", true), 10));
        left_one_cube.addAction(new ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5));
        right_one_cube.addAction(new DrivePathAction(new Path("/home/lvuser/paths/center_to_right.csv", true), 10));
        right_one_cube.addAction(new ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5));

        is_finished = false;
    }

    @Override
    public void init() {
        if(DriverStation.getInstance().getGameSpecificMessage().substring(0, 1).equals("L")) {
            current_routine = left_one_cube;
            System.out.println("choosing left");
        } else {
            System.out.println("choosing riught");
            current_routine = right_one_cube;
        }
        current_action = current_routine.getCurrentAction();
        current_action.start();
    }

    @Override
    public void handle() {
        if(is_finished) {
            return;
        }
        if(current_routine.isLastStep() && current_action.next()) {
            current_action.finish();
            return;
        }
        if(current_action == null){ 
            current_action = current_routine.getCurrentAction();
            current_action.start();
        } else if(current_action.next()) {
            System.out.println("Advancing routine");
            current_action.finish();
            current_routine.advanceRoutine();
            current_action = current_routine.getCurrentAction();
            current_action.start();
        } else {
            current_action.update();
        }
    }

    private static AutoController _instance = new AutoController();

    public static AutoController getInstance() {
        return _instance;
    }
}
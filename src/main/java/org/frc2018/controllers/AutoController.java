package org.frc2018.controllers;

import org.frc2018.auto.actions.Action;
import org.frc2018.auto.actions.ArmAction;
import org.frc2018.auto.actions.DrivePathAction;
import org.frc2018.auto.actions.DriveStraightAction;
import org.frc2018.auto.actions.NothingAction;
import org.frc2018.auto.actions.TurnAction;
import org.frc2018.auto.actions.ArmAction.ArmDirection;
import org.frc2018.auto.actions.ArmAction.IntakeDirection;
import org.frc2018.auto.routines.Routine;
import org.frc2018.path.Path;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoController extends Controller {

    private Routine left_one_cube = new Routine("left_one_cube");
    private Routine right_one_cube = new Routine("right_one_cube");
    private Routine baseline = new Routine("baseline");
    private Routine left_outer_one_cube = new Routine("left_outer_one_cube");
    private Routine right_outer_one_cube = new Routine("right_outer_one_cube");

    private Routine current_routine;
    private Action current_action;

    private boolean is_finished;

    private AutoController() {
        left_one_cube.addAction(new DrivePathAction(new Path("/home/lvuser/paths/center_to_left.csv", true), 10));
        left_one_cube.addAction(new ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5));
        
        right_one_cube.addAction(new DrivePathAction(new Path("/home/lvuser/paths/center_to_right.csv", true), 10));
        right_one_cube.addAction(new ArmAction(ArmDirection.HOLD_UP, IntakeDirection.DROP, 0.5));

        baseline.addAction(new ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.2));
        baseline.addAction(new DriveStraightAction(3, -106));

        right_outer_one_cube.addAction(new NothingAction(0.0));
        right_outer_one_cube.addAction(new ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4));
        right_outer_one_cube.addAction(new DriveStraightAction(3, -150));
        right_outer_one_cube.addAction(new TurnAction(2, 90));
        right_outer_one_cube.addAction(new DriveStraightAction(1.5, -35));
        right_outer_one_cube.addAction(new ArmAction(ArmDirection.NONE, IntakeDirection.DROP, 0.4));

        left_outer_one_cube.addAction(new NothingAction(0.0));
        left_outer_one_cube.addAction(new ArmAction(ArmDirection.UP, IntakeDirection.HOLD, 0.4));
        left_outer_one_cube.addAction(new DriveStraightAction(3, -150));
        left_outer_one_cube.addAction(new TurnAction(1.5, -90));
        left_outer_one_cube.addAction(new DriveStraightAction(1.5, -35));
        left_outer_one_cube.addAction(new ArmAction(ArmDirection.NONE, IntakeDirection.DROP, 0.4));

        is_finished = false;
    }

    @Override
    public void init() {
        if(DriverStation.getInstance().getGameSpecificMessage().substring(0, 1).equals("L")) {
            current_routine = left_one_cube;
        } else {
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
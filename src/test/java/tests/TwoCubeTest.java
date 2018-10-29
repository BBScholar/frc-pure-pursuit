package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.frc2018.Vector2;
import org.frc2018.auto.actions.DrivePathAction;
import org.frc2018.controllers.AutoController;
import org.frc2018.path.Path;
import org.frc2018.path.PathFollower;

import tests.Drivetrain;

public class TwoCubeTest {

    @Test
    public void testTwoCube() {
        Path first_path = new Path("src/test/java/tests/center_to_left.csv", true);
        Path second_path = new Path("src/test/java/tests/left_to_pyramid.csv", false);
        PathFollower first_follower = new PathFollower(first_path);
        PathFollower second_follower = new PathFollower(second_path);

        Drivetrain.getInstance().setRobotAngle(180.0);
        int iterations = 0;
        while(!first_follower.doneWithPath(Drivetrain.getInstance().getRobotPosition())) {
            double[] velocities = first_follower.update(Drivetrain.getInstance().getRobotPosition(), Drivetrain.getInstance().getRobotAngle());
            Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
            Drivetrain.getInstance().update(0.005);
            iterations++;
        }
        System.out.println("First Position: " + Drivetrain.getInstance().getRobotPosition() + " Iterations: " + iterations);

        Vector2 init_position = Drivetrain.getInstance().getRobotPosition();
        double init_angle = Drivetrain.getInstance().getRobotAngle();
        iterations = 0;
        System.out.println("________PATH_________");
        System.out.println(second_path);
        System.out.println("______END_PATH_______");
        while(!second_follower.doneWithPath(Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position))) {
            if(iterations % 100 == 0)
                System.out.println((Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position)) + ":" + (Drivetrain.getInstance().getRobotAngle() - init_angle));
            double[] velocities = second_follower.update(Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position), Drivetrain.getInstance().getRobotAngle() - init_angle);
            Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
            Drivetrain.getInstance().update(0.005);
            iterations++;
        }
        System.out.println("Second Position: " + Drivetrain.getInstance().getRobotPosition() + " Iterations: " + iterations);
    }

}
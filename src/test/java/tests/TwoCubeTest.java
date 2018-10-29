package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.frc2018.Vector2;
import org.frc2018.auto.actions.DrivePathAction;
import org.frc2018.controllers.AutoController;
import org.frc2018.path.Path;
import org.frc2018.path.PathFollower;

import tests.Drivetrain;

public class TwoCubeTest {

    @Test
    public void forwardsPathTest() {
        Path forwards_path = new Path("src/test/java/tests/center_to_left.csv", false);
        PathFollower forwards_path_follower = new PathFollower(forwards_path);
        // Initialize the robot
        Drivetrain.getInstance().setRobotAngle(0.0);
        Drivetrain.getInstance().setRobotPosition(new Vector2(0, 0));


        // Account for initial position
        Vector2 init_position = Drivetrain.getInstance().getRobotPosition();
        double init_angle = Drivetrain.getInstance().getRobotAngle();

        while(!forwards_path_follower.doneWithPath(Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position))) {
            Vector2 corrected_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
            double corrected_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
            double[] velocities = forwards_path_follower.update(corrected_position, corrected_angle);
            Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
            Drivetrain.getInstance().update(0.005);
        }

        Vector2 final_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
        double final_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
        System.out.println("Final robot position: " + final_position + "    Final robot angle: " + final_angle);
        System.out.println("Desired robot position: " + forwards_path.getPoint(forwards_path.getPathLength() - 2));
        assertEquals(final_position.x, forwards_path.getPoint(forwards_path.getPathLength() - 2).x, 0.2);
        assertEquals(final_position.y, forwards_path.getPoint(forwards_path.getPathLength() - 2).y, 0.2);
    }

    @Test
    public void backwardsPathTest() {
        Path backwards_path = new Path("src/test/java/tests/left_to_pyramid.csv", false);
        PathFollower backwards_path_follower = new PathFollower(backwards_path);
        // Initialize the robot
        Drivetrain.getInstance().setRobotAngle(0.0);
        Drivetrain.getInstance().setRobotPosition(new Vector2(0.0, 0.0));


        // Account for initial position
        Vector2 init_position = Drivetrain.getInstance().getRobotPosition();
        double init_angle = Drivetrain.getInstance().getRobotAngle();

        while(!backwards_path_follower.doneWithPath(Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position))) {
            Vector2 corrected_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
            double corrected_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
            double[] velocities = backwards_path_follower.update(corrected_position, corrected_angle);
            Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
            Drivetrain.getInstance().update(0.005);
        }

        Vector2 final_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
        double final_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
        System.out.println("Final robot position: " + final_position + "    Final robot angle: " + final_angle);
        System.out.println("Desired robot position: " + backwards_path.getPoint(backwards_path.getPathLength() - 2));
        assertEquals(final_position.x, backwards_path.getPoint(backwards_path.getPathLength() - 2).x, 0.2);
        assertEquals(final_position.y, backwards_path.getPoint(backwards_path.getPathLength() - 2).y, 0.2);
    }

    @Test
    public void forwardsPathReversedRobotTest() {
        double update_period = 0.005; // seconds
        double max_timeout = 10.0; // seconds

        Path forwards_path = new Path("src/test/java/tests/center_to_left.csv", true);
        PathFollower forwards_path_follower = new PathFollower(forwards_path);
        // Initialize the robot
        Drivetrain.getInstance().setRobotAngle(180.0);
        Drivetrain.getInstance().setRobotPosition(new Vector2(0, 0));


        // Account for initial position
        Vector2 init_position = Drivetrain.getInstance().getRobotPosition();
        double init_angle = Drivetrain.getInstance().getRobotAngle();

        for(int i = 0; !forwards_path_follower.doneWithPath(Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position)); i++) {
            Vector2 corrected_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
            double corrected_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
            double[] velocities = forwards_path_follower.update(corrected_position, corrected_angle);
            Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
            Drivetrain.getInstance().update(update_period);

            if(i > max_timeout / update_period) {
                Vector2 final_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
                double final_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
                System.out.println("Final robot position: " + final_position + "    Final robot angle: " + final_angle);
                System.out.println("Desired robot position: " + forwards_path.getPoint(forwards_path.getPathLength() - 2));
                
                fail("the maximum number of iterations was reached!");
            }
        }

        Vector2 final_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
        double final_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
        System.out.println("Final robot position: " + final_position + "    Final robot angle: " + final_angle);
        System.out.println("Desired robot position: " + forwards_path.getPoint(forwards_path.getPathLength() - 2));
        assertEquals(final_position.x, forwards_path.getPoint(forwards_path.getPathLength() - 2).x, 0.2);
        assertEquals(final_position.y, forwards_path.getPoint(forwards_path.getPathLength() - 2).y, 0.2);
    }

    @Test
    public void backwardsPathReversedRobotTest() {
        double update_period = 0.005; // seconds
        double max_timeout = 10.0; // seconds

        Path backwards_path = new Path("src/test/java/tests/left_to_pyramid.csv", true);
        PathFollower backwards_path_follower = new PathFollower(backwards_path);
        // Initialize the robot
        Drivetrain.getInstance().setRobotAngle(180.0);
        Drivetrain.getInstance().setRobotPosition(new Vector2(102.4, 67.3));

        // Account for initial position
        Vector2 init_position = Drivetrain.getInstance().getRobotPosition();
        double init_angle = Drivetrain.getInstance().getRobotAngle();

        for(int i = 0; !backwards_path_follower.doneWithPath(Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position)); i++) {
            Vector2 corrected_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
            double corrected_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
            double[] velocities = backwards_path_follower.update(corrected_position, corrected_angle);
            Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
            Drivetrain.getInstance().update(update_period);

            if(i > max_timeout / update_period) {
                Vector2 final_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
                double final_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
                System.out.println("Final robot position: " + final_position + "    Final robot angle: " + final_angle);
                System.out.println("Desired robot position: " + backwards_path.getPoint(backwards_path.getPathLength() - 2));

                fail("the maximum number of iterations was reached!");
            }
        }

        Vector2 final_position = Vector2.subtract(Drivetrain.getInstance().getRobotPosition(), init_position);
        double final_angle = Drivetrain.getInstance().getRobotAngle() - init_angle;
        System.out.println("Final robot position: " + final_position + "    Final robot angle: " + final_angle);
        System.out.println("Desired robot position: " + backwards_path.getPoint(backwards_path.getPathLength() - 2));
        assertEquals(final_position.x, backwards_path.getPoint(backwards_path.getPathLength() - 2).x, 0.2);
        assertEquals(final_position.y, backwards_path.getPoint(backwards_path.getPathLength() - 2).y, 0.2);
    }

}
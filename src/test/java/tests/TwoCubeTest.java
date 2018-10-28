package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.frc2018.auto.actions.DrivePathAction;
import org.frc2018.controllers.AutoController;
import org.frc2018.path.Path;
import org.frc2018.path.PathFollower;

import tests.Drivetrain;

public class TwoCubeTest {

    @Test
    public void test_test() {
        Path test_path = new Path("src/test/java/tests/path.csv", false);
        PathFollower test_follower = new PathFollower(test_path);
        double[] velocities = test_follower.update(Drivetrain.getInstance().getRobotPosition(), Drivetrain.getInstance().getRobotAngle());
        Drivetrain.getInstance().setVelocity(velocities[0], velocities[1]);
        assertEquals(true && false, false);
    }

}
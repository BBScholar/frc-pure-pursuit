package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import tests.Position;
import org.frc2018.Vector2;
import org.frc2018.path.Path;
import org.frc2018.path.PathFollower;
import org.frc2018.Utils;

public class PurePursuitTests {

    @Test
    public void testRls() {
        // try driving straight
        Position.getInstance().update(100, 100, 0);
        Vector2 pos1 = Position.getInstance().getPositionVector();
        System.out.println("POSITION: " + pos1);
        assertEquals(100, pos1.x);
        assertEquals(0, pos1.y);
        // reset
        Position.getInstance().reset();
        // try turning
        Position.getInstance().update(15.71, 0, -45);
        Vector2 pos2 = Position.getInstance().getPositionVector();
        System.out.println("POSITION: " + pos2);
        assertEquals(7.07, pos2.x, 0.01);
        assertEquals(-2.93, pos2.y, 0.01);
    }

    @Test
    public void testUtils() {
        assertEquals(Utils.degreesToTalonAngle(350), 3500);
        assertEquals(Utils.encoderTicksPer100MsToInchesPerSecond(350), 350 * 10 * 18.8495559 / 1024, 0.001);
        assertEquals(Utils.encoderTicksToInches(350), 350 * 18.8495559 / 1024, 0.001);
        assertEquals(Utils.inchesPerSecondToEncoderTicksPer100Ms(10), 1024 / 18.8495559, 0.03);
        assertEquals(Utils.inchesToEncoderTicks(10), 10 * 1024 / 18.8495559, 0.3);
        assertEquals(Utils.talonAngleToDegrees(3600), 360);
    }

    @Test
    public void testPath() {
        Path path = new Path("src/test/java/tests/path.csv");
        System.out.println(path.findClosestPointIndex(new Vector2(-23, 77), 0));
    }

}
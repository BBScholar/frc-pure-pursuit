package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.frc2018.Position;
import org.frc2018.math.Vector2;

public class PurePursuitTests {

    @Test
    public void test_rls() {
        // try driving straight
        Position.getInstance().update(100, 100, 0);
        Vector2 pos1 = Position.getInstance().getPosition();
        System.out.println("POSITION: " + pos1);
        assertEquals(100, pos1.x);
        assertEquals(0, pos1.y);
        // reset
        Position.getInstance().reset();
        // try turning
        Position.getInstance().update(15.71, 0, -45);
        Vector2 pos2 = Position.getInstance().getPosition();
        System.out.println("POSITION: " + pos2);
        assertEquals(7.07, pos2.x, 0.01);
        assertEquals(2.93, pos2.y, 0.01);
    }



}
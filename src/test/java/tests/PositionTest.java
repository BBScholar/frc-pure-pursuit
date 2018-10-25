package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.frc2018.Position;
import org.frc2018.math.Vector2;
import org.junit.jupiter.api.Test;

public class PositionTest {

    @Test
    public void test_position() {
        Position.getInstance().update(100, 100, 0);
        Vector2 v = Position.getInstance().getPosition();
        assertEquals(0, v.x, 0.1);
        assertEquals(100, v.y, 0.1);
    }



}
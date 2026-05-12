package entity;

import entity.misc.ExpOrb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class ExpOrbTest extends JavaFXInitializer {

    private ExpOrb orb;

    @BeforeEach
    void setUp() {
        orb = new ExpOrb(10, "redorb", null);
    }

    @Test
    void testInitialState() {
        assertEquals("redorb", orb.getName());
        assertEquals(10, orb.getXpAmount());
    }
}

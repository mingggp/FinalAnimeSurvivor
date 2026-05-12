package entity;

import entity.misc.Chest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class ChestTest extends JavaFXInitializer {

    private Chest chest;

    @BeforeEach
    void setUp() {
        Chest baseChest = new Chest(null);
        chest = new Chest(baseChest, 500.0, 500.0);
    }

    @Test
    void testChestCreation() {
        // Just verify it doesn't crash and initializes properly
        assertEquals(500.0, chest.getMapX());
        assertEquals(500.0, chest.getMapY());
        assertNotNull(chest.getHitbox());
    }
}

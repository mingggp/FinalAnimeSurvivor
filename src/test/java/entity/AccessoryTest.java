package entity;

import entity.accessory.Accessory;
import entityInterface.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the level / upgrade logic that lives in the abstract
 * {@link Accessory} base class.
 */
class AccessoryTest {

    /** Minimal Accessory subclass — no resources, no GameManager needed. */
    private static class StubAccessory extends Accessory {
        int procCount = 0;
        StubAccessory(int maxLevel){
            super("stub-accessory", maxLevel);
        }
        @Override public void procEffect(){ procCount++; }
        @Override public GameObject copy(){ return this; }
    }

    private StubAccessory accessory;

    @BeforeEach
    void setUp(){
        accessory = new StubAccessory(3);
    }

    @Test
    void initialLevelIsOne(){
        assertEquals("1", accessory.getLevel());
    }

    @Test
    void upgradeIncrementsLevel(){
        accessory.upgrade();
        assertEquals("2", accessory.getLevel());
        accessory.upgrade();
        // level reached maxLevel
        assertEquals("Max", accessory.getLevel());
    }

    @Test
    void upgradeCapsAtMaxLevel(){
        accessory.upgrade();
        accessory.upgrade();
        accessory.upgrade();
        accessory.upgrade();
        accessory.upgrade();
        // Even after many upgrades, getLevel() stays at "Max"
        assertEquals("Max", accessory.getLevel(),
                "Level must not exceed maxLevel after repeated upgrades");
    }

    @Test
    void procEffectIsCallable(){
        accessory.procEffect();
        accessory.procEffect();
        accessory.procEffect();
        assertEquals(3, accessory.procCount);
    }

    @Test
    void nameIsSetByConstructor(){
        assertEquals("stub-accessory", accessory.getName());
    }
}

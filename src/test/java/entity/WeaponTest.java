package entity;

import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the level, upgrade and cooldown logic that lives in the abstract
 * {@link Weapon} base class. A minimal in-memory stub is used so the tests
 * never need to touch JavaFX or the rest of the game world.
 */
class WeaponTest {

    /** Minimal Weapon subclass with no rendering / sprite dependencies. */
    private static class StubWeapon extends Weapon {
        StubWeapon(int maxLevel, double cooldown){
            super("stub", maxLevel, cooldown);
        }
        @Override public void use(double dt){ timeSinceUse += dt; }
        @Override public void update(double dt){ }
        @Override public void render(GraphicsContext gc){ }
        @Override public boolean isExpired(){ return false; }
        @Override public void upgrade(){ this.setLevel(this.level + 1); }
        @Override public GameObject copy(){ return this; }
        void setTimeSinceUse(double t){ this.timeSinceUse = t; }
    }

    private StubWeapon weapon;

    @BeforeEach
    void setUp(){
        weapon = new StubWeapon(5, 2.0);
    }

    @Test
    void initialLevelIsOne(){
        assertEquals("1", weapon.getLevel());
        assertEquals(5, weapon.getMaxLevel());
        assertEquals(2.0, weapon.getCooldown());
    }

    @Test
    void upgradeIncrementsLevelUntilMax(){
        weapon.upgrade();
        assertEquals("2", weapon.getLevel());
        weapon.upgrade();
        weapon.upgrade();
        assertEquals("4", weapon.getLevel());
        weapon.upgrade();
        // level == maxLevel — getLevel() must return "Max"
        assertEquals("Max", weapon.getLevel(),
                "getLevel() should return \"Max\" when level reaches maxLevel");
    }

    @Test
    void cooldownProgressIsZeroJustAfterFiring(){
        weapon.setTimeSinceUse(0);
        assertEquals(0.0, weapon.getCooldownProgress(), 1e-9);
    }

    @Test
    void cooldownProgressIsOneWhenReady(){
        weapon.setTimeSinceUse(2.0);
        assertEquals(1.0, weapon.getCooldownProgress(), 1e-9);
    }

    @Test
    void cooldownProgressClampsAboveOne(){
        weapon.setTimeSinceUse(99.0);
        assertEquals(1.0, weapon.getCooldownProgress(), 1e-9,
                "Progress must clamp at 1.0 even after long idle");
    }

    @Test
    void cooldownProgressIsHalfWayThrough(){
        weapon.setTimeSinceUse(1.0);
        assertEquals(0.5, weapon.getCooldownProgress(), 1e-9);
    }

    @Test
    void zeroCooldownWeaponAlwaysReady(){
        StubWeapon instant = new StubWeapon(3, 0);
        assertEquals(1.0, instant.getCooldownProgress(), 1e-9,
                "Weapon with cooldown 0 is always ready (no divide-by-zero)");
    }

    @Test
    void useAdvancesTimeSinceUse(){
        weapon.setTimeSinceUse(0);
        weapon.use(0.5);
        assertEquals(0.25, weapon.getCooldownProgress(), 1e-9);
    }

    @Test
    void cooldownChangesAffectProgress(){
        weapon.setTimeSinceUse(1.0);
        weapon.setCooldown(4.0);
        assertEquals(0.25, weapon.getCooldownProgress(), 1e-9);
    }
}

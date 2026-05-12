package entity;

import entity.character.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest extends JavaFXInitializer {

    private Character character;

    @BeforeEach
    void setUp() {
        // Initializing with test values, no weapon needed for basic logic tests
        character = new Character("sukuna", 300, 1000.0, 256, null,null,null);
    }

    @Test
    void testInitialState() {
        assertEquals("sukuna", character.getName());
        assertEquals(300, character.getSpeed());
        assertEquals(1000.0, character.getMaxHP());
        assertEquals(1000.0, character.getCurrentHP());
        assertFalse(character.isDead());
    }

    @Test
    void testReceiveDamage() {
        character.receiveDamage(200.0);
        assertEquals(800.0, character.getCurrentHP());
        
        // Test i-frame
        character.receiveDamage(100.0);
        // It shouldn't take damage because iFrameTime was set to 0.5 in receiveDamage
        assertEquals(800.0, character.getCurrentHP(), "Should not take damage during i-frame");
    }

    @Test
    void testHeal() {
        character.receiveDamage(500.0);
        character.heal(200.0);
        assertEquals(700.0, character.getCurrentHP());
        
        character.heal(500.0);
        assertEquals(1000.0, character.getCurrentHP(), "HP should not exceed maxHP");
    }

    @Test
    void testIsDead() {
        assertFalse(character.isDead());
        
        // We simulate a lot of damage by skipping the i-frame (since we cannot easily wait in tests without calling update)
        // Actually, let's just set the HP directly or use reflection. Or call receiveDamage multiple times with an update in between.
        character.setCurrentHP(0);
        assertTrue(character.isDead());
        
        character.setCurrentHP(-50);
        assertTrue(character.isDead());
    }
}

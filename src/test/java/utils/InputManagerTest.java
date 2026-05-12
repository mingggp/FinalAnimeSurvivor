package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputManagerTest {

    private InputManager inputManager;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
    }

    @Test
    void testAddAndRemoveKey() {
        inputManager.addKey("W");
        assertTrue(inputManager.isKeyPressed("W"));
        assertFalse(inputManager.isKeyPressed("A"));

        inputManager.removeKey("W");
        assertFalse(inputManager.isKeyPressed("W"));
    }

    @Test
    void testMultipleKeys() {
        inputManager.addKey("W");
        inputManager.addKey("D");
        
        assertTrue(inputManager.isKeyPressed("W"));
        assertTrue(inputManager.isKeyPressed("D"));
        assertFalse(inputManager.isKeyPressed("S"));
    }
}

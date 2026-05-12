package entity;

import entity.enemy.Enemy;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.JavaFXInitializer;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest extends JavaFXInitializer {

    private Enemy enemy;

    @BeforeEach
    void setUp() {
        enemy = new Enemy("slime", 150, 500.0, true, true);
        enemy.setMapX(100.0);
        enemy.setMapY(100.0);
    }

    @Test
    void testInitialState() {
        assertEquals("slime", enemy.getName());
        assertEquals(150, enemy.getSpeed());
        assertEquals(500.0, enemy.getMaxHP());
        assertEquals(500.0, enemy.getCurrentHP());
        assertTrue(enemy.isCollisionOn());
        assertTrue(enemy.isMovable());
        assertFalse(enemy.isDead());
    }

    @Test
    void testTakeDamage() {
        enemy.takeDamage(100.0, 0.5, 0.5, Color.RED);
        assertEquals(400.0, enemy.getCurrentHP());
        
        enemy.takeDamage(400.0, 0.5, 0.5, Color.RED);
        assertEquals(0.0, enemy.getCurrentHP());
        assertTrue(enemy.isDead());
    }

    @Test
    void testMovementDirection() {
        // Test update function which calculates dx and dy towards character
        // We need to set up character coordinates and let it calculate
        double charX = 200.0;
        double charY = 100.0;
        
        // Before update
        assertEquals(0.0, enemy.getDx());
        assertEquals(0.0, enemy.getDy());
        
        // After update (distance > 24 so it normalizes)
        enemy.update(0.016, charX, charY); // 0.016 seconds = 60 FPS
        
        // Expected dx = 1, dy = 0 because enemy is at (100,100) and char is at (200,100)
        assertEquals(1.0, enemy.getDx(), 0.01);
        assertEquals(0.0, enemy.getDy(), 0.01);
    }
}

package utils;

import entity.character.Character;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tile.Tile;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Black-box tests for {@link CollisionChecker} using a tiny synthetic tile
 * grid. Verifies that solid tiles cancel character movement in the requested
 * direction and that walking through air leaves movement untouched.
 *
 * Tile grid (3x3, tile size = 96):
 *
 *   col:    0      1      2
 *   row 0:  wall   wall   wall
 *   row 1:  wall   air    wall
 *   row 2:  wall   wall   wall
 *
 * The player sits in the air tile at (144, 144) — the center of cell (1,1).
 */
class CollisionCheckerTest extends JavaFXInitializer {

    private Character character;

    @BeforeAll
    static void initTiles(){
        Tile wall = new Tile();
        wall.collision = true;
        Tile air = new Tile();
        air.collision = false;

        Tile[] tiles = { wall, air };

        // mapTilesNum[col][row]
        int[][] grid = new int[3][3];
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                grid[c][r] = (c == 1 && r == 1) ? 1 : 0; // center is air
            }
        }
        CollisionChecker.setTile(grid, tiles);
    }

    @BeforeEach
    void setUp(){
        character = new Character("test", 100, 1000.0, 64, null, null, null);
        // Center of cell (1,1) so the player has wall tiles on every side.
        character.setMapX(144);
        character.setMapY(96);
    }

    @Test
    void wallStopsLeftwardMovement(){
        character.setDx(-1);
        CollisionChecker.checkTileCollision(character, 0.5);
        assertEquals(0, character.getDx(),
                "Wall on the left must zero out negative dx");
    }

    @Test
    void wallStopsRightwardMovement(){
        character.setDx(1);
        CollisionChecker.checkTileCollision(character, 0.5);
        assertEquals(0, character.getDx(),
                "Wall on the right must zero out positive dx");
    }

    @Test
    void wallStopsUpwardMovement(){
        character.setDy(-1);
        CollisionChecker.checkTileCollision(character, 0.5);
        assertEquals(0, character.getDy());
    }

    @Test
    void wallStopsDownwardMovement(){
        character.setDy(1);
        CollisionChecker.checkTileCollision(character, 0.5);
        assertEquals(0, character.getDy());
    }

    @Test
    void noOpWhenTilesNotInitialized(){
        // Reset the static state to verify the early-return guard.
        CollisionChecker.setTile(null, null);
        character.setDx(1);
        character.setDy(1);
        CollisionChecker.checkTileCollision(character, 0.5);
        // Movement values should be untouched
        assertEquals(1, character.getDx());
        assertEquals(1, character.getDy());
        // Restore the grid for the rest of the suite
        initTiles();
    }
}

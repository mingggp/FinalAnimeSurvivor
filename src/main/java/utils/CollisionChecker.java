package utils;

import entity.character.Character;
import entity.enemy.Enemy;
import tile.Tile;


/**
 * Static utility that detects and resolves collisions between entities and
 * solid tiles on the tile map.
 *
 * <h2>How it works</h2>
 * <p>Before the entity moves, the checker looks ahead by one tick's movement
 * in each axis (using the entity's speed and {@code accumulateDeltaTime}).  If
 * the projected position overlaps a solid tile, the corresponding velocity
 * component ({@code dx} or {@code dy}) is zeroed out so the entity stops
 * flush against the wall instead of passing through it.
 *
 * <h2>Coordinate system</h2>
 * <p>Tiles are 96 × 96 pixels in world space.  The tile grid is indexed as
 * {@code mapTilesNum[col][row]}, where tile (0,0) is the top-left corner of
 * the map.
 *
 * <h2>Thread safety</h2>
 * <p>The static fields are written once at game start via {@link #setTile} and
 * then only read — no locking is required for the read-only game-loop access.
 */
public class CollisionChecker {

    /**
     * Tile-type index grid: {@code mapTilesNum[col][row]} holds the index into
     * the {@link #tiles} array for the tile at that column and row.
     */
    private static int[][] mapTilesNum;

    /**
     * Array of tile archetypes; each entry defines whether that tile type
     * blocks movement ({@link Tile#collision}).
     */
    private static Tile[] tiles;

    /**
     * Initialises (or resets) the static tile data used by all subsequent
     * collision checks.
     *
     * <p>Pass {@code null} for both parameters to disable collision checking
     * until the next valid call (used by unit tests to exercise the early-return
     * guard).
     *
     * @param mapTilesNum tile-index grid ({@code [col][row]}), or {@code null}
     * @param tiles       tile archetype array, or {@code null}
     */
    public static void setTile(int[][] mapTilesNum, Tile[] tiles) {
        CollisionChecker.mapTilesNum = mapTilesNum;
        CollisionChecker.tiles = tiles;
    }

    /**
     * Checks tile collisions for a {@link Character} and zeroes out the
     * velocity component that would cause a solid-tile overlap.
     *
     * <p>The character hitbox is an upright rectangle:
     * {@code (mapX − width/2, mapY) → (mapX + width/2, mapY + height)}.
     *
     * @param character            the player character to check
     * @param accumulateDeltaTime  seconds since the last frame (used to project
     *                             the next-frame position one tick ahead)
     */
    public static void checkTileCollision(Character character, double accumulateDeltaTime) {

        if (mapTilesNum == null || tiles == null) return;

        double leftHitboxX = (character.getMapX() - character.getWidth() / 2.0);
        double RightHitboxX = (character.getMapX() + character.getWidth() / 2.0);
        double TopHitboxY = (character.getMapY());
        double BottomHitboxY = (character.getMapY() + character.getHeight());

        int entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX) / 96)));
        int entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX) / 96)));
        int entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY) / 96)));
        int entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY) / 96)));

        int tileNum1, tileNum2;

        // --- Vertical (Y-axis) checks ---
        if (character.getDy() < 0) {
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY - character.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityTopRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                character.setDy(0);
            }
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY) / 96)));
        } else if (character.getDy() > 0) {
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY + character.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityBottomRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                character.setDy(0);
            }
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY) / 96)));
        }

        // --- Horizontal (X-axis) checks ---
        if (character.getDx() < 0) {
            entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX - character.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityLeftCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                character.setDx(0);
            }
        } else if (character.getDx() > 0) {
            entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX + character.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityRightCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                character.setDx(0);
            }
        }
    }

    /**
     * Checks tile collisions for an {@link Enemy} and zeroes out the velocity
     * component that would cause a solid-tile overlap.
     *
     * <p>The enemy hitbox is centred on ({@code mapX}, {@code mapY}):
     * {@code (mapX − width/2, mapY − height/2) → (mapX + width/2, mapY + height/2)}.
     *
     * @param enemy                the enemy to check
     * @param accumulateDeltaTime  seconds since the last frame
     */
    public static void checkTileCollision(Enemy enemy, double accumulateDeltaTime) {

        if (mapTilesNum == null || tiles == null) return;

        double leftHitboxX = (enemy.getMapX() - enemy.getWidth() / 2.0);
        double RightHitboxX = (enemy.getMapX() + enemy.getWidth() / 2.0);
        double TopHitboxY = (enemy.getMapY() - enemy.getHeight() / 2.0);
        double BottomHitboxY = (enemy.getMapY() + enemy.getHeight() / 2.0);

        int entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX) / 96)));
        int entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX) / 96)));
        int entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY) / 96)));
        int entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY) / 96)));

        int tileNum1, tileNum2;

        if (enemy.getDy() < 0) {
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY - enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityTopRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDy(0);
            }
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY) / 96)));
        } else if (enemy.getDy() > 0) {
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY + enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityBottomRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDy(0);
            }
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY) / 96)));
        }
        if (enemy.getDx() < 0) {
            entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX - enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityLeftCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDx(0);
            }
        } else if (enemy.getDx() > 0) {
            entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX + enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityRightCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDx(0);
            }
        }
    }
}

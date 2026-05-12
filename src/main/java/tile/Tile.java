package tile;

import javafx.scene.image.Image;

/**
 * Data container for a single tile type in the game's tile map.
 *
 * <p>There are a small number of {@code Tile} archetypes (e.g. grass, wall,
 * water) stored in the {@link TileManager#tiles} array.  The map grid
 * ({@link TileManager#mapTilesNum}) stores integer indices into that array,
 * so many map cells can share the same {@code Tile} instance.
 *
 * <p>{@link utils.CollisionChecker} reads the {@link #collision} flag to
 * decide whether an entity's movement should be blocked.
 */
public class Tile {

    /** Sprite image drawn for this tile type; loaded via {@link utils.SpriteManager}. */
    public Image image;

    /**
     * Whether this tile type blocks entity movement.
     * {@code true} for walls, water, and other impassable surfaces;
     * {@code false} for walkable terrain.
     */
    public boolean collision = false;
}

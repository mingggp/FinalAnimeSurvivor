package entityInterface.itemInterface;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;

/**
 * Implemented by items that can appear on the ground and be picked up by walking
 * over them.
 *
 * <p>When an enemy dies, {@link core.GameManager} may add a dropped item to
 * {@code droppedItemList}.  Each frame the game loop calls
 * {@link #updateAsDroppedItem(double)} and {@link #renderAsDroppedItem(GraphicsContext)}
 * to animate the item.  Once the player's hitbox intersects
 * {@link #getItemHitBox()}, the item is added to the backpack.
 *
 * <p>Magnet accessories tag items via {@link entity.item.Item#tag()} so they
 * slide toward the player automatically.
 */
public interface Droppable {

    /**
     * Advances the dropped item's state (e.g. movement toward a magnetised player).
     *
     * @param accumulateDeltaTime seconds since the last frame
     */
    void updateAsDroppedItem(double accumulateDeltaTime);

    /**
     * Draws the dropped item sprite at its current world position.
     *
     * @param gc the game canvas's {@link GraphicsContext}
     */
    void renderAsDroppedItem(GraphicsContext gc);

    /**
     * Returns the axis-aligned bounding box used to detect pickup collisions.
     *
     * @return current hit-box in world coordinates
     */
    BoundingBox getItemHitBox();

    /**
     * Overrides the item's hit-box (e.g. after the item has moved).
     *
     * @param itemHitBox new hit-box in world coordinates
     */
    void setItemHitBox(BoundingBox itemHitBox);
}

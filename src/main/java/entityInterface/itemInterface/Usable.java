package entityInterface.itemInterface;

/**
 * Implemented by items that can be actively consumed from the backpack.
 *
 * <p>When {@link core.GameManager} runs with auto-use mode enabled it iterates
 * over the backpack each frame and calls {@link #use(int)} on every
 * {@code Usable} item.  Implementations should consume one unit of the item
 * (decrement its amount) and apply their effect to the player or game world.
 */
public interface Usable {

    /**
     * Activates the item's one-time effect and decrements its stack count.
     *
     * @param slot the backpack slot index (0-35) where this item currently sits,
     *             used to remove the item when its amount reaches zero
     */
    void use(int slot);
}

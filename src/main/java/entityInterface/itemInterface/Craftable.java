package entityInterface.itemInterface;

/**
 * Implemented by items that are assembled from raw materials rather than
 * dropped by enemies.
 *
 * <p>{@link core.GameManager} scans the craftable item list whenever the
 * player opens a chest and calls {@link #isCraftable()} to decide whether
 * to offer the item.  If the player picks it, {@link #deductMaterial()} is
 * called to consume the required ingredients from the backpack.
 *
 * @see entity.item.craftable.MalevolentKitchen
 * @see entity.item.craftable.UnlimitedHollowPurple
 */
public interface Craftable {

    /**
     * Returns {@code true} if all required material items are present in the
     * player's backpack and the item can be crafted right now.
     *
     * @return {@code true} if crafting is possible
     */
    boolean isCraftable();

    /**
     * Removes the required material items from the player's backpack.
     *
     * <p>Called immediately after the player selects this item as a chest
     * reward.  Implementations must ensure they only remove the exact
     * quantities needed even if the player has extras.
     */
    void deductMaterial();
}

package entityInterface;

/**
 * Implemented by active game objects that need per-frame logic updates.
 *
 * <p>The game loop in {@link core.GameManager#update(double)} iterates over
 * the {@code usingWeaponList} and {@code usingItemList} collections and calls
 * {@link #update(double)} on each element.  Once {@link #isExpired()} returns
 * {@code true}, the element is removed from the collection.
 *
 * <p>Note: {@link entity.weapon.Weapon} also has a separate {@code use(double)}
 * method (called on the equipped weapon every frame); {@code update()} is called
 * on a spawned projectile instance that was added to {@code usingWeaponList}.
 */
public interface Updatable {

    /**
     * Advances the state of this object by one simulation step.
     *
     * @param accumulateDeltaTime seconds elapsed since the previous frame
     */
    void update(double accumulateDeltaTime);

    /**
     * Returns {@code true} when this object has finished its job and should
     * be removed from the active list.
     *
     * <p>A weapon projectile returns {@code true} after it has travelled its
     * maximum range or lifetime; a consumable item returns {@code true} after
     * its effect has worn off.
     *
     * @return {@code true} if this object should be discarded
     */
    boolean isExpired();
}

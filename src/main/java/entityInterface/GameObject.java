package entityInterface;

/**
 * Prototype interface implemented by every game object that can be duplicated.
 *
 * <p>The game maintains "master lists" of weapons, accessories, and items that
 * act as templates.  When a copy is needed (e.g. handing a weapon to the player
 * or dropping an item on the map), {@link #copy()} produces a fresh, independent
 * instance from the template without needing to know the concrete type.
 *
 * <p>Every class that extends {@link entity.Entity} must implement this interface,
 * providing a deep-enough copy that the clone can be used independently of the
 * original.
 */
public interface GameObject {

    /**
     * Creates and returns a copy of this game object.
     *
     * <p>The returned object must be independent of the original — modifying
     * it must not affect the template stored in the master list.
     *
     * @return a new instance with the same initial state as this object
     */
    GameObject copy();
}

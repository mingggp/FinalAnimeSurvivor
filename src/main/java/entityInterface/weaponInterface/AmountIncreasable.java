package entityInterface.weaponInterface;

/**
 * Implemented by weapons that can fire additional projectiles simultaneously.
 *
 * <p>Accessories call {@link #increaseAmount(int)} to let qualifying weapons
 * emit more hits, beams, or orbs per activation (e.g. extra Bible books,
 * or extra projectiles from {@link entity.weapon.Standard}).
 */
public interface AmountIncreasable {

    /**
     * Increases the number of simultaneous projectiles / units by {@code delta}.
     *
     * <p>A value of {@code 1} adds one extra projectile on top of the current
     * count.  Implementations may impose an upper bound.
     *
     * @param delta number of additional projectiles to add (must be &gt; 0)
     */
    void increaseAmount(int delta);
}

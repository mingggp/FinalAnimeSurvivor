package entityInterface.weaponInterface;

/**
 * Implemented by weapons whose active effect duration can be extended by accessories.
 *
 * <p>Some weapons (e.g. {@link entity.weapon.Bible}) remain active for a fixed
 * duration before expiring.  Accessories that implement this interface call
 * {@link #increaseDuration(double)} to extend how long each wave stays active.
 */
public interface DurationIncreasable {

    /**
     * Multiplies the weapon's effect duration by the given factor.
     *
     * <p>A {@code multiplier} of {@code 1.2} extends the duration by 20 %.
     *
     * @param multiplier factor applied to the current duration (e.g. {@code 1.2})
     */
    void increaseDuration(double multiplier);
}

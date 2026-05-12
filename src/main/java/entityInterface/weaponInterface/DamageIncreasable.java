package entityInterface.weaponInterface;

/**
 * Implemented by weapons whose base damage can be amplified by accessories.
 *
 * <p>Accessories such as {@link entity.accessory.SukunaArm} call
 * {@link #increaseDamage(double)} each frame via {@code procEffect()} to
 * apply a multiplicative damage bonus to all qualifying equipped weapons.
 */
public interface DamageIncreasable {

    /**
     * Multiplies the weapon's current damage output by the given factor.
     *
     * <p>A {@code multiplier} of {@code 1.1} represents a 10 % increase.
     * Implementations should clamp or document any upper bound they impose.
     *
     * @param multiplier the factor to apply (e.g. {@code 1.1} for +10 %)
     */
    void increaseDamage(double multiplier);
}

package entityInterface.weaponInterface;

/**
 * Implemented by weapons whose firing cooldown can be reduced by accessories.
 *
 * <p>{@link entity.accessory.KeyPad} calls {@link #decreaseCooldown(double)}
 * each frame so that equipped weapons fire more frequently as the accessory
 * is upgraded.
 */
public interface CooldownDecreasable {

    /**
     * Multiplies the weapon's current cooldown by the given factor.
     *
     * <p>A {@code multiplier} of {@code 0.9} reduces the cooldown by 10 %.
     * Values above {@code 1.0} would lengthen the cooldown; accessories
     * should always pass values in (0, 1].
     *
     * @param multiplier factor applied to the current cooldown (e.g. {@code 0.9})
     */
    void decreaseCooldown(double multiplier);
}

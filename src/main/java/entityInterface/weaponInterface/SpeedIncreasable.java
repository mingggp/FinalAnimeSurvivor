package entityInterface.weaponInterface;

/**
 * Implemented by weapons whose projectile or effect speed can be boosted by accessories.
 *
 * <p>Accessories call {@link #increaseSpeed(double)} each frame to make
 * qualifying weapons travel faster across the map.
 */
public interface SpeedIncreasable {

    /**
     * Multiplies the weapon's current speed by the given factor.
     *
     * <p>A {@code multiplier} of {@code 1.1} increases travel speed by 10 %.
     *
     * @param multiplier factor applied to the current speed (e.g. {@code 1.1})
     */
    void increaseSpeed(double multiplier);
}

package entityInterface.weaponInterface;

/**
 * Implemented by weapons whose hit-area or sprite size can be enlarged by accessories.
 *
 * <p>Accessories such as {@link entity.accessory.SukunaCloak} call
 * {@link #increaseSize(double)} so that weapon attacks cover a larger area.
 */
public interface SizeIncreasable {

    /**
     * Multiplies the weapon's current hit-area size by the given factor.
     *
     * <p>A {@code multiplier} of {@code 1.15} grows the attack area by 15 %.
     *
     * @param multiplier factor applied to the current size (e.g. {@code 1.15})
     */
    void increaseSize(double multiplier);
}

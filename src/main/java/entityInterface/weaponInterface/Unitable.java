package entityInterface.weaponInterface;

/**
 * Implemented by weapons that can be united (fused) with another weapon to
 * form a combined, more powerful variant.
 *
 * <p>The union mechanic is triggered from a chest reward.
 * {@link core.GameManager} checks {@link #isUnitable()} to determine
 * eligibility, then calls {@link #Unite(int)} to apply the fusion in-place.
 */
public interface Unitable {

    /**
     * Returns {@code true} if this weapon currently meets the requirements
     * for union (e.g. a compatible partner weapon is equipped in another slot).
     *
     * @return {@code true} if the weapon can be united right now
     */
    boolean isUnitable();

    /**
     * Performs the union: modifies or replaces the weapon at the given slot
     * index with the fused result.
     *
     * @param Index the slot index (0-5) in {@link core.GameManager#getWeaponList()}
     *              where this weapon currently resides
     */
    void Unite(int Index);
}

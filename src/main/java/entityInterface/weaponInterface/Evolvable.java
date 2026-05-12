package entityInterface.weaponInterface;

/**
 * Implemented by weapons that can evolve into a more powerful evolved form.
 *
 * <p>A weapon becomes evolvable after it reaches its maximum base level and
 * the player opens a chest.  {@link core.GameManager} checks
 * {@link #isEvolvable()} to determine whether to offer the evolve option, and
 * then calls {@link #Evolve(int)} to replace the weapon in its slot with the
 * evolved variant.
 *
 * <p>Example evolution chain:
 * <pre>
 *   {@link entity.weapon.Standard} (Lv Max) ──► {@link entity.weapon.evolvedWeapon.Lazer}
 * </pre>
 */
public interface Evolvable {

    /**
     * Returns {@code true} if this weapon currently meets the conditions
     * required to evolve (e.g. maximum level reached, required item in
     * backpack).
     *
     * @return {@code true} if the weapon can evolve right now
     */
    boolean isEvolvable();

    /**
     * Performs the evolution: replaces this weapon in {@code weaponList[index]}
     * with its evolved counterpart and applies any stat boosts.
     *
     * @param index the slot index (0-5) in {@link core.GameManager#getWeaponList()}
     *              where this weapon currently resides
     */
    void Evolve(int index);
}

package entity.weapon;

import entity.Entity;
import entityInterface.Renderable;
import entityInterface.Updatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Abstract base class for all weapons in the game.
 *
 * <p>A weapon has three distinct lifecycle phases each game frame:
 * <ol>
 *   <li>{@link #use(double)} — called every frame while the weapon is equipped;
 *       the weapon decides internally when to actually fire based on its cooldown.</li>
 *   <li>{@link #update(double)} — called on every active projectile / effect that
 *       was spawned by {@code use()}; the projectile moves, deals damage, etc.</li>
 *   <li>{@link #render(GraphicsContext)} — draws the projectile / effect on screen.</li>
 * </ol>
 *
 * <p>Weapons support a level system (1 … maxLevel).  Once the level reaches
 * {@code maxLevel}, {@link #getLevel()} returns {@code "Max"} instead of a
 * numeric string.  Each concrete subclass must implement {@link #upgrade()} to
 * apply its own stat changes when the player levels up the weapon.
 *
 * <p>The {@link #getCooldownProgress()} helper is used by the UI to drive the
 * cooldown overlay in {@link gui.gameLayout.ItemSquare}.
 *
 * @see entity.weapon.evolvedWeapon
 * @see entityInterface.Updatable
 * @see entityInterface.Renderable
 */
public abstract class Weapon extends Entity implements Updatable, Renderable {

    /** Inventory / HUD icon displayed in the weapon slot. */
    private Image Icon;

    /**
     * Current level of the weapon (1-based).
     * Protected so that concrete subclasses such as evolved weapons can
     * read the level directly; external code should use {@link #getLevel()}.
     */
    protected int level;

    /** Maximum level this weapon can reach. */
    private int maxLevel;

    /** Time between automatic uses (seconds). Zero means instant / always ready. */
    private double cooldown;

    /**
     * Seconds elapsed since the weapon was last fired.
     *
     * <p>Each concrete weapon is responsible for incrementing this counter in
     * its {@link #use(double)} implementation and resetting it to zero when
     * the weapon fires.  Stored in the base class so the UI can read cooldown
     * progress through {@link #getCooldownProgress()} without knowing the
     * concrete subclass.
     */
    protected double timeSinceUse;

    /**
     * Constructs a weapon with the given identity and stats.
     *
     * @param name     logical name (must match the icon resource file name)
     * @param maxLevel maximum upgrade level; must be ≥ 1
     * @param cooldown seconds between uses; 0 means always ready
     */
    public Weapon(String name, int maxLevel, double cooldown) {
        this.setName(name);
        this.setLevel(1);
        this.maxLevel = maxLevel;
        this.setCooldown(cooldown);
    }

    /**
     * Returns the fraction of the cooldown that has elapsed, clamped to [0, 1].
     *
     * <p>A value of {@code 1.0} means the weapon is ready to fire; values below
     * {@code 1.0} indicate it is still cooling down.  Weapons with a cooldown of
     * zero or less always return {@code 1.0} (never blocked by cooldown).
     *
     * <p>Used by {@link gui.gameLayout.ItemSquare#tickCooldown(Weapon)} to
     * animate the cooldown overlay on the HUD.
     *
     * @return cooldown progress in [0, 1]
     */
    public double getCooldownProgress() {
        if (cooldown <= 0) return 1.0;
        double p = timeSinceUse / cooldown;
        if (p < 0) return 0;
        if (p > 1) return 1;
        return p;
    }

    /**
     * Ticks the weapon each game frame while it is in the equipped weapon slot.
     *
     * <p>Implementations should increment {@link #timeSinceUse} by
     * {@code accumulateDeltaTime} and fire (spawning a projectile in
     * {@link core.GameManager#getUsingWeaponList()}) when the cooldown elapses.
     *
     * @param accumulateDeltaTime seconds since the last frame
     */
    public abstract void use(double accumulateDeltaTime);

    /**
     * Advances the state of an active projectile / effect that was already spawned.
     *
     * <p>After {@link #use(double)} creates and adds a weapon instance to
     * {@code usingWeaponList}, the game loop calls {@code update()} on it every
     * frame until {@link #isExpired()} returns {@code true}.
     *
     * @param accumulateDeltaTime seconds since the last frame
     */
    public abstract void update(double accumulateDeltaTime);

    /**
     * Draws this weapon's active visual effect to the canvas.
     *
     * @param gc the {@link javafx.scene.canvas.GraphicsContext} of the game canvas
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Returns {@code true} when this weapon effect has finished and should be
     * removed from the active weapon list by the game loop.
     *
     * @return {@code true} if the effect has expired
     */
    public abstract boolean isExpired();

    /**
     * Applies an upgrade to this weapon — increasing damage, range, speed,
     * reducing cooldown, etc.  Each subclass defines its own upgrade logic.
     */
    public abstract void upgrade();

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    /** @return the HUD icon image, or {@code null} if not yet loaded */
    public Image getIcon() {
        return Icon;
    }

    /** @param icon the icon image to display in the weapon slot */
    public void setIcon(Image icon) {
        Icon = icon;
    }

    /**
     * Returns the current level as a display string.
     *
     * @return the numeric level (e.g. {@code "3"}) or {@code "Max"} when the
     *         weapon has reached {@link #maxLevel}
     */
    public String getLevel() {
        if (level < maxLevel) {
            return String.valueOf(level);
        }
        return "Max";
    }

    /**
     * Directly sets the internal level counter.  Prefer {@link #upgrade()} for
     * normal gameplay progression; this method is mainly used by tests and
     * the evolved-weapon constructors.
     *
     * @param level new level value (1-based)
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /** @return the maximum upgrade level for this weapon */
    public int getMaxLevel() {
        return maxLevel;
    }

    /** @return cooldown in seconds between automatic uses */
    public double getCooldown() {
        return cooldown;
    }

    /**
     * Overrides the weapon cooldown.  Called by {@link entityInterface.weaponInterface.CooldownDecreasable}
     * accessories such as {@link entity.accessory.KeyPad}.
     *
     * @param cooldown new cooldown in seconds (use 0 for instant)
     */
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}

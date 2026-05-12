package entity.accessory;

import entity.Entity;
import javafx.scene.image.Image;

/**
 * Abstract base class for all passive accessories the player can equip.
 *
 * <p>Accessories sit in the six {@code accessoryList} slots managed by
 * {@link core.GameManager}.  Unlike weapons, they are not added to a
 * separate "using" list — their effect is applied every frame by the game
 * loop calling {@link #procEffect()} directly.
 *
 * <p>The level / upgrade logic mirrors that of {@link entity.weapon.Weapon}:
 * upgrades increment the level until it reaches {@link #maxLevel}, at which
 * point {@link #getLevel()} returns {@code "Max"}.  Concrete subclasses define
 * exactly what changes with each upgrade.
 *
 * @see entity.accessory.SixEye
 * @see entity.accessory.SukunaArm
 */
public abstract class Accessory extends Entity {

    /** HUD icon displayed in the accessory slot. */
    private Image Icon;

    /**
     * Current upgrade level (1-based).
     * Protected so that subclasses can branch on the numeric level in
     * {@link #procEffect()}.
     */
    protected int level;

    /** Upper bound for {@link #level}; additional upgrades are ignored. */
    private int maxLevel;

    /**
     * Constructs an accessory with the given identity and maximum upgrade level.
     *
     * @param name     logical name (should match the icon resource file name)
     * @param maxLevel maximum upgrade level; must be ≥ 1
     */
    public Accessory(String name, int maxLevel) {
        this.setName(name);
        this.level = 1;
        this.maxLevel = maxLevel;
    }

    /** @return the HUD icon image, or {@code null} if not yet loaded */
    public Image getIcon() {
        return Icon;
    }

    /** @param icon the icon image to display in the accessory slot */
    public void setIcon(Image icon) {
        Icon = icon;
    }

    /**
     * Returns the current level as a display string.
     *
     * @return numeric level (e.g. {@code "2"}) or {@code "Max"} when fully upgraded
     */
    public String getLevel() {
        if (level < maxLevel) {
            return String.valueOf(level);
        }
        return "Max";
    }

    /**
     * Increments the upgrade level by one, capping at {@link #maxLevel}.
     * Subclasses that need to apply additional stat changes on upgrade
     * should override this method and call {@code super.upgrade()} first.
     */
    public void upgrade() {
        this.level = level + 1;
        if (level > maxLevel) level = maxLevel;
    }

    /**
     * Applies the accessory's passive effect for the current frame.
     *
     * <p>This method is called by {@link core.GameManager#update(double)} once
     * per frame for every non-null slot in {@code accessoryList}.  Implementations
     * may modify character stats, spawn VFX, buff weapons, etc.
     */
    public abstract void procEffect();
}

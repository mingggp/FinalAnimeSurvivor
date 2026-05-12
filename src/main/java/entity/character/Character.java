package entity.character;

import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.CollisionChecker;
import utils.InputManager;
import entity.Entity;

/**
 * Represents the player-controlled character.
 *
 * <p>Each selectable hero (e.g. Sukuna, Gojo) is stored as a {@code Character}
 * instance in {@link core.GameManager#getAllCharacterList()}.  When a run starts,
 * the selected hero is copied into the active {@code character} slot.
 *
 * <h2>Movement</h2>
 * <p>WASD input is read from {@link InputManager} each frame.  The movement
 * vector is normalised for diagonal movement, tested against solid tiles via
 * {@link CollisionChecker}, and then applied as pixel displacement.
 *
 * <h2>HP &amp; i-frames</h2>
 * <p>Damage is gated by {@link #iFrameTime} — after being hit the character is
 * temporarily invincible to prevent being damaged every frame by the same
 * enemy.  Passive regeneration heals 1 % of max HP per second.
 *
 * <h2>Magnet radius</h2>
 * <p>Accessories and items within {@link #magnetRadius} pixels are attracted
 * toward the character automatically.
 */
public class Character extends Entity {

    /** Current hit points. When this reaches 0 {@link #isDead()} returns {@code true}. */
    private double currentHP;

    /** Maximum hit points; determines the length of the HP bar. */
    private double maxHP;

    /** Movement speed in pixels per second. */
    private double speed;

    /** Sprite dimensions in pixels (32 × 48). */
    private int width, height;

    /** 100 × 100 icon shown on the character-select screen. */
    private Image icon;

    /** 96 × 96 sprite sheet frame rendered during gameplay. */
    private Image sprite;

    /**
     * Radius (pixels) within which dropped items and exp-orbs are automatically
     * pulled toward the character.
     */
    private double magnetRadius;

    /**
     * The weapon this character starts a run with; copied into
     * {@code weaponList[0]} when the game begins.
     */
    private Weapon starterWeapon;

    /** Axis-aligned bounding box used for collision detection, updated every frame. */
    private BoundingBox hitbox;

    /**
     * Normalised horizontal velocity direction for the current frame
     * ({@code -1}, {@code 0}, or {@code 1} before normalisation).
     */
    private double dx, dy;

    /**
     * Remaining invincibility time in seconds after taking a hit.
     * Prevents the character from receiving damage every frame.
     */
    private double iFrameTime;

    /** Countdown until the next passive HP-regeneration tick (1 second). */
    private double healInterval;

    /**
     * Creates a character with all stats specified explicitly.
     *
     * @param name          logical name (e.g. {@code "gojo"})
     * @param speed         movement speed in pixels per second
     * @param maxHP         maximum and starting hit points
     * @param magnetRadius  item-magnet radius in pixels
     * @param weapon        starting weapon; placed in slot 0 when the game starts
     * @param icon          100×100 character-select icon
     * @param sprite        96×96 in-game sprite
     */
    public Character(String name, int speed, double maxHP, int magnetRadius,
                     Weapon weapon, Image icon, Image sprite) {
        this.width = 32;
        this.height = 48;
        this.setName(name);
        this.speed = speed;
        this.setMaxHP(maxHP);
        this.setCurrentHP(maxHP);
        this.icon = icon;
        this.sprite = sprite;
        this.magnetRadius = magnetRadius;
        this.starterWeapon = weapon;
    }

    /**
     * Copy constructor — produces a play-ready clone of the template character.
     *
     * <p>Used by the character-select screen to hand an independent instance
     * to {@link core.GameManager} without modifying the master template.
     *
     * @param character the template to clone
     */
    public Character(Character character) {
        this.width = character.getWidth();
        this.height = character.getHeight();
        this.setName(character.getName());
        this.setSpeed(character.getSpeed());
        this.setCurrentHP(character.getCurrentHP());
        this.setMaxHP(character.getMaxHP());
        this.icon = character.icon;
        this.sprite = character.sprite;
        this.magnetRadius = character.magnetRadius;
        this.starterWeapon = character.starterWeapon;
    }

    /**
     * Advances the character's state by one frame.
     *
     * <p>Steps performed in order:
     * <ol>
     *   <li>Decrement timers (i-frames, heal interval).</li>
     *   <li>Apply passive HP regeneration (1 % max HP / second).</li>
     *   <li>Read WASD input and build the movement vector.</li>
     *   <li>Normalise the vector for diagonal movement.</li>
     *   <li>Check tile collisions (may zero out dx/dy).</li>
     *   <li>Apply displacement to world position.</li>
     *   <li>Recompute the axis-aligned hitbox.</li>
     * </ol>
     *
     * @param inputManager        the keyboard state for this frame
     * @param accumulateDeltaTime seconds since the last frame
     */
    public void update(InputManager inputManager, double accumulateDeltaTime) {

        iFrameTime -= accumulateDeltaTime;
        healInterval -= accumulateDeltaTime;

        if (healInterval < 0 && currentHP < maxHP && !isDead()) {
            currentHP += maxHP / 100;
            healInterval = 1;
            if (currentHP > maxHP) currentHP = maxHP;
        }

        dx = 0;
        dy = 0;
        if (inputManager.isKeyPressed("W")) dy -= 1;
        if (inputManager.isKeyPressed("S")) dy += 1;
        if (inputManager.isKeyPressed("A")) dx -= 1;
        if (inputManager.isKeyPressed("D")) dx += 1;

        // Normalise diagonal movement so the character doesn't move faster at 45°
        if (dx != 0 && dy != 0) {
            double length = Math.hypot(dx, dy);
            dx /= length;
            dy /= length;
        }

        CollisionChecker.checkTileCollision(this, accumulateDeltaTime);

        this.setMapX(this.getMapX() + (dx * this.getSpeed() * accumulateDeltaTime));
        this.setMapY(this.getMapY() + (dy * this.getSpeed() * accumulateDeltaTime));

        this.hitbox = new BoundingBox(this.getMapX() - (this.getWidth() / 2.0),
                this.getMapY(), this.getWidth(), this.getHeight());
    }

    /**
     * Draws the character sprite and HP bar at the fixed screen centre.
     *
     * <p>The character is always rendered at the screen centre (960, 540); the
     * camera follows the character by scrolling the world around it.
     *
     * @param gc the game canvas's {@link GraphicsContext}
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(sprite, 960 - 48, 540 - 48);
        gc.setFill(Color.BLACK);
        gc.fillRect(960 - 48, 540 + 60, 96, 20);
        gc.setFill(Color.RED);
        gc.fillRect(960 - 45, 540 + 63, 90 * (currentHP / maxHP), 14);
    }

    /**
     * Applies damage to the character, subject to the i-frame cooldown.
     *
     * <p>If the character is still in its invincibility window (i.e.
     * {@link #iFrameTime} > 0) or is already dead, the call is ignored.
     * Otherwise HP is reduced and a 0.2-second i-frame window is granted.
     *
     * @param amount raw damage to subtract (positive value)
     */
    public void receiveDamage(double amount) {
        if (iFrameTime <= 0 && currentHP > 0) {
            currentHP -= amount;
            iFrameTime = 0.2;
        }
    }

    /**
     * Restores HP by the given amount, capped at {@link #maxHP}.
     *
     * @param amount HP to restore (positive value)
     */
    public void heal(double amount) {
        this.currentHP += amount;
        if (currentHP > maxHP) currentHP = maxHP;
    }

    /** @return current HP (may be ≤ 0 after the death sequence begins) */
    public double getCurrentHP() { return currentHP; }

    /** @param currentHP new HP value */
    public void setCurrentHP(double currentHP) { this.currentHP = currentHP; }

    /** @return maximum HP for this character */
    public double getMaxHP() { return maxHP; }

    /** @param maxHP new maximum HP */
    public void setMaxHP(double maxHP) { this.maxHP = maxHP; }

    /** @return character-select icon (100×100) */
    public Image getIcon() { return icon; }

    /** @return item-magnet radius in pixels */
    public double getMagnetRadius() { return magnetRadius; }

    /** @param magnetRadius new magnet radius in pixels */
    public void setMagnetRadius(double magnetRadius) { this.magnetRadius = magnetRadius; }

    /** @return movement speed in pixels per second */
    public double getSpeed() { return speed; }

    /** @param speed new movement speed in pixels per second */
    public void setSpeed(double speed) { this.speed = speed; }

    /** @return sprite width (always 32 px) */
    public int getWidth() { return width; }

    /** @return sprite height (always 48 px) */
    public int getHeight() { return height; }

    /** @return normalised Y velocity for the current frame */
    public double getDy() { return dy; }

    /** @param dy new Y velocity direction */
    public void setDy(double dy) { this.dy = dy; }

    /** @return normalised X velocity for the current frame */
    public double getDx() { return dx; }

    /** @param dx new X velocity direction */
    public void setDx(double dx) { this.dx = dx; }

    /** @return the axis-aligned bounding box in world coordinates */
    public BoundingBox getHitbox() { return hitbox; }

    /** @return the weapon placed in slot 0 at the start of a run */
    public Weapon getStarterWeapon() { return starterWeapon; }

    /**
     * Returns {@code true} when the character's HP has dropped to zero or below.
     *
     * @return {@code true} if the character is dead
     */
    public boolean isDead() { return currentHP <= 0; }

    /**
     * Not yet implemented — characters are not cloned via the Prototype pattern.
     *
     * @return always {@code null}
     */
    @Override
    public GameObject copy() { return null; }
}

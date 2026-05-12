package entity.enemy;

import entity.Entity;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.CollisionChecker;
import vfx.VFXManager;

/**
 * Represents a single enemy unit in the game world.
 *
 * <p>Enemies are created in two steps:
 * <ol>
 *   <li>A <em>template</em> instance is added to
 *       {@link core.EnemySpawner#addEnemyToSpawner(Enemy)} with stats but no
 *       world position.</li>
 *   <li>The spawner clones the template via the
 *       {@link #Enemy(Enemy, double, double)} copy constructor to produce a
 *       positioned instance that is added to the live
 *       {@code inGameEnemyList}.</li>
 * </ol>
 *
 * <h2>Movement</h2>
 * <p>Each frame the enemy steers directly toward the player's world position.
 * If a knockback timer is active the enemy is pushed in the <em>opposite</em>
 * direction instead; if a stun timer is active it stands still.
 *
 * <h2>Damage</h2>
 * <p>Weapons call {@link #takeDamage(double, double, double, Color)} to reduce
 * HP, apply stun / knockback, and spawn a floating damage-text VFX.
 */
public class Enemy extends Entity {

    /** Sprite image loaded from classpath via {@link utils.SpriteManager}. */
    private Image sprite;

    /** Current hit points. At zero the enemy is considered dead. */
    private double currentHP;

    /** Maximum hit points (also the starting HP). */
    private double maxHP;

    /** Movement speed in pixels per second. */
    private double speed;

    /** Sprite / hitbox dimensions in pixels (96 × 96). */
    private int width, height;

    /** Axis-aligned bounding box; recomputed every frame after movement. */
    private BoundingBox hitbox;

    /** Whether this enemy checks tile collisions (currently always {@code true}). */
    private boolean collisionOn;

    /** Whether this enemy moves toward the player (currently always {@code true}). */
    private boolean movable;

    /**
     * Normalised movement direction components calculated from the vector
     * toward the player.
     */
    private double dx, dy;

    /**
     * Remaining seconds the enemy is stunned and cannot move.
     * Set by {@link #takeDamage(double, double, double, Color)}.
     */
    private double stuntTimer;

    /**
     * Remaining seconds the enemy is knocked back away from the player.
     * Takes priority over normal steering while positive.
     */
    private double knockbackTimer;

    /**
     * Template constructor — creates an enemy with stats but no map position.
     *
     * <p>Instances created with this constructor are stored in the spawner's
     * list and are never added to the live game world directly.
     *
     * @param name        logical name; must match the sprite resource filename
     *                    (e.g. {@code "slime"} → {@code slime.png})
     * @param speed       movement speed in pixels per second
     * @param maxHP       maximum (and starting) hit points
     * @param collisionOn whether the enemy should check tile collisions
     * @param movable     whether the enemy should chase the player
     */
    public Enemy(String name, int speed, double maxHP,
                 boolean collisionOn, boolean movable) {
        this.width = 96;
        this.height = 96;
        this.setName(name);
        this.speed = speed;
        this.setMaxHP(maxHP);
        this.setCurrentHP(maxHP);
        this.collisionOn = true;
        this.movable = true;
        this.setSprite(utils.SpriteManager.loadImage(name + ".png"));
    }

    /**
     * Spawner copy constructor — creates a positioned, play-ready enemy from
     * a template.
     *
     * @param enemy the template enemy to copy stats from
     * @param mapX  initial world-space X coordinate
     * @param mapY  initial world-space Y coordinate
     */
    public Enemy(Enemy enemy, double mapX, double mapY) {
        this.width = enemy.getWidth();
        this.height = enemy.getHeight();
        this.setMapX(mapX);
        this.setMapY(mapY);
        this.setName(enemy.getName());
        this.speed = enemy.getSpeed();
        this.setCurrentHP(enemy.getCurrentHP());
        this.setMaxHP(enemy.getMaxHP());
        this.collisionOn = enemy.isCollisionOn();
        this.movable = enemy.isMovable();
        this.sprite = enemy.sprite;
        this.hitbox = new BoundingBox(
                this.getMapX() - (this.getWidth() / 2.0),
                this.getMapY() - (this.getHeight() / 2.0),
                this.getWidth(), this.getHeight());
    }

    /**
     * Advances the enemy's state by one frame.
     *
     * <p>Steps:
     * <ol>
     *   <li>Decrement stun / knockback timers.</li>
     *   <li>Apply tile collision to prevent walking through walls.</li>
     *   <li>If knocked back, move away from the player; if stunned, stay still;
     *       otherwise move toward the player.</li>
     *   <li>Recompute the hitbox at the new position.</li>
     *   <li>Recalculate the normalised direction vector toward the player.</li>
     * </ol>
     *
     * @param accumulateDeltaTime seconds since the last frame
     * @param characterMapX       player's current world X
     * @param characterMapY       player's current world Y
     */
    public void update(double accumulateDeltaTime, double characterMapX, double characterMapY) {
        stuntTimer -= accumulateDeltaTime;
        knockbackTimer -= accumulateDeltaTime;

        CollisionChecker.checkTileCollision(this, accumulateDeltaTime);
        double ex = this.getMapX();
        double ey = this.getMapY();

        if (knockbackTimer >= 0) {
            this.setMapX(ex - (dx * speed * accumulateDeltaTime));
            this.setMapY(ey - (dy * speed * accumulateDeltaTime));
        } else if (stuntTimer <= 0) {
            this.setMapX(ex + (dx * speed * accumulateDeltaTime));
            this.setMapY(ey + (dy * speed * accumulateDeltaTime));
        }

        this.hitbox = new BoundingBox(
                this.getMapX() - (this.getWidth() / 2.0),
                this.getMapY() - (this.getHeight() / 2.0),
                this.getWidth(), this.getHeight());

        dx = characterMapX - ex;
        dy = characterMapY - ey;
        double distance = Math.hypot(dx, dy);

        if (distance != 0) {
            dx /= distance;
            dy /= distance;
        }
    }

    /**
     * Draws the enemy sprite if it falls within the visible screen area.
     *
     * @param gc             the game canvas's {@link GraphicsContext}
     * @param characterMapX  player's world X (used to compute screen offset)
     * @param characterMapY  player's world Y
     */
    public void render(GraphicsContext gc, double characterMapX, double characterMapY) {
        double screenX = this.getMapX() - characterMapX + 960;
        double screenY = this.getMapY() - characterMapY + 540;
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if (mapX > characterMapX - 960 - 96 && mapX < characterMapX + 960 + 96
                && mapY > characterMapY - 540 - 96 && mapY < characterMapY + 540 + 96) {
            gc.drawImage(this.getSprite(), screenX - 48, screenY - 48);
        }
    }

    /**
     * Applies damage to this enemy and triggers stun / knockback effects.
     *
     * <p>HP is always reduced regardless of any timer.  Stun and knockback
     * timers are only extended if the new duration would be longer than the
     * remaining time (so strong hits override weaker ongoing effects).
     * A floating damage-text VFX is always spawned.
     *
     * @param amount            raw damage to subtract
     * @param stuntDuration     seconds the enemy should be stunned (0 = none)
     * @param knockbackDuration seconds the enemy should be knocked back (0 = none)
     * @param color             colour of the floating damage number
     */
    public void takeDamage(double amount, double stuntDuration,
                           double knockbackDuration, Color color) {
        this.setCurrentHP(this.getCurrentHP() - amount);
        if (stuntTimer < stuntDuration) stuntTimer = stuntDuration;
        if (knockbackTimer < knockbackDuration) knockbackTimer = knockbackDuration;
        VFXManager.spawnDamageText(this.getMapX(), this.getMapY(), amount, color);
    }

    /**
     * Returns {@code true} when the enemy's HP has dropped to zero or below.
     *
     * @return {@code true} if dead
     */
    public boolean isDead() { return currentHP <= 0; }

    /** @return current HP */
    public double getCurrentHP() { return currentHP; }

    /** @param currentHP new HP value */
    public void setCurrentHP(double currentHP) { this.currentHP = currentHP; }

    /** @return maximum HP */
    public double getMaxHP() { return maxHP; }

    /** @param maxHP new maximum HP */
    public void setMaxHP(double maxHP) { this.maxHP = maxHP; }

    /** @return in-game sprite image */
    public Image getSprite() { return sprite; }

    /** @param sprite new sprite image */
    public void setSprite(Image sprite) { this.sprite = sprite; }

    /** @return sprite / hitbox width (always 96 px) */
    public int getWidth() { return width; }

    /** @return sprite / hitbox height (always 96 px) */
    public int getHeight() { return height; }

    /** @return movement speed in pixels per second */
    public double getSpeed() { return speed; }

    /** @return {@code true} if the knockback timer is still active */
    public boolean isKnockBack() { return knockbackTimer > 0; }

    /** @return whether this enemy collides with tiles */
    public boolean isCollisionOn() { return collisionOn; }

    /** @return whether this enemy chases the player */
    public boolean isMovable() { return movable; }

    /** @return axis-aligned bounding box in world coordinates */
    public BoundingBox getHitbox() { return hitbox; }

    /** @return current Y direction component (normalised) */
    public double getDy() { return dy; }

    /** @param dy new Y direction */
    public void setDy(double dy) { this.dy = dy; }

    /** @return current X direction component (normalised) */
    public double getDx() { return dx; }

    /** @param dx new X direction */
    public void setDx(double dx) { this.dx = dx; }

    /** Not implemented — enemy templates are cloned via the copy constructor. */
    @Override
    public GameObject copy() { return null; }
}

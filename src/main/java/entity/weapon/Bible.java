package entity.weapon;

import core.GameManager;
import entity.enemy.Enemy;
import entityInterface.GameObject;
import entityInterface.weaponInterface.AmountIncreasable;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.DurationIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import entityInterface.weaponInterface.SpeedIncreasable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Bible — an orbital weapon inspired by Vampire Survivors' rotating-bible mechanic.
 *
 * <p>When the cooldown elapses, a wave of {@link #amount} glowing books is spawned
 * and rotated around the player at a fixed {@link #radius}.  Any enemy within the
 * collision radius of a book takes damage every {@link #hitInterval} seconds.  The
 * wave expires after {@link #duration} seconds.
 *
 * <h2>Implementation pattern</h2>
 * <p>The <em>equipped</em> Bible instance sits in a weapon slot and drives its own
 * cooldown via {@link #use(double)}.  When the cooldown fires it adds {@code this}
 * to {@link core.GameManager#getUsingWeaponList()}.  The game loop then calls
 * {@link #update(double)} and {@link #render(GraphicsContext)} on the same instance
 * until {@link #isExpired()} returns {@code true}.
 *
 * <h2>Upgrade scaling</h2>
 * <ul>
 *   <li>Every level: damage ×1.15, angular speed ×1.05, duration ×1.05, cooldown ×0.95</li>
 *   <li>Levels 3, 5, 7: +1 book</li>
 * </ul>
 */
public class Bible extends Weapon
        implements DamageIncreasable, SizeIncreasable, AmountIncreasable,
                   SpeedIncreasable, DurationIncreasable, CooldownDecreasable {

    /** Game manager — provides player position and the enemy list. */
    private final GameManager gameManager;

    /** Book sprite drawn at each orbital position. */
    private final Image sprite;

    /** Damage dealt to enemies per hit interval. */
    private double damage;

    /** Orbit radius in pixels (distance of books from the player centre). */
    private double radius;

    /** Rendered size of each book sprite in pixels (square). */
    private double bookSize;

    /** Angular velocity of the wave in radians per second. */
    private double angularSpeed;

    /** Seconds a spawned wave stays active before expiring. */
    private double duration;

    /** Number of books evenly distributed around the orbit. */
    private int amount;

    /** Minimum seconds between damage ticks against the same enemy group. */
    private double hitInterval;

    /** Accumulator for the hit-interval timer. */
    private double timeSinceLastHit;

    /** Current rotation angle of the wave in radians. */
    private double phase;

    /** {@code true} while a wave is active and should update / render. */
    private boolean active;

    public Bible(GameManager gameManager){
        super("Bible", 8, 5);
        this.gameManager = gameManager;
        this.sprite = utils.SpriteManager.loadImage("weapon/icon/bible.png");
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/bible.png"));
        timeSinceUse = this.getCooldown();
        damage = 12;
        radius = 180;
        bookSize = 56;
        angularSpeed = Math.PI;     // half a revolution per second
        duration = 3.5;
        amount = 2;
        hitInterval = 0.25;
        timeSinceLastHit = hitInterval;
        active = false;
        phase = 0;
    }

    public Bible(Bible other){
        super(other.getName(), other.getMaxLevel(), other.getCooldown());
        this.gameManager = other.gameManager;
        this.sprite = other.sprite;
        this.setIcon(other.getIcon());
        this.damage = other.damage;
        this.radius = other.radius;
        this.bookSize = other.bookSize;
        this.angularSpeed = other.angularSpeed;
        this.duration = other.duration;
        this.amount = other.amount;
        this.hitInterval = other.hitInterval;
        this.timeSinceLastHit = other.hitInterval;
        this.timeSinceUse = 0;
        this.phase = 0;
        this.active = true;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        timeSinceUse += accumulateDeltaTime;
        if (timeSinceUse >= this.getCooldown()) {
            timeSinceUse = 0;
            // Spawn a fresh wave by registering this weapon as "using".
            // The using list updates and renders it until duration elapses.
            active = true;
            phase = 0;
            timeSinceLastHit = hitInterval;
            if (!gameManager.getUsingWeaponList().contains(this)) {
                gameManager.getUsingWeaponList().add(this);
            }
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {
        if (!active) return;
        phase += angularSpeed * accumulateDeltaTime;
        timeSinceLastHit += accumulateDeltaTime;

        if (timeSinceLastHit >= hitInterval) {
            timeSinceLastHit = 0;
            double playerX = gameManager.getCharacter().getMapX();
            double playerY = gameManager.getCharacter().getMapY();
            double halfBook = bookSize / 2.0;

            for (int i = 0; i < amount; i++) {
                double angle = phase + (2 * Math.PI * i) / amount;
                double bx = playerX + radius * Math.cos(angle);
                double by = playerY + radius * Math.sin(angle);
                for (Enemy enemy : gameManager.getInGameEnemyList()) {
                    double dx = enemy.getMapX() - bx;
                    double dy = enemy.getMapY() - by;
                    if (dx * dx + dy * dy < halfBook * halfBook * 4) {
                        enemy.takeDamage(damage, 0.05, 0.05, Color.GOLD);
                    }
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active || sprite == null) return;
        double playerX = gameManager.getCharacter().getMapX();
        double playerY = gameManager.getCharacter().getMapY();
        for (int i = 0; i < amount; i++) {
            double angle = phase + (2 * Math.PI * i) / amount;
            double bx = playerX + radius * Math.cos(angle);
            double by = playerY + radius * Math.sin(angle);
            gc.drawImage(sprite,
                    gameManager.getScreenX(bx) - bookSize / 2.0,
                    gameManager.getScreenY(by) - bookSize / 2.0,
                    bookSize, bookSize);
        }
    }

    @Override
    public boolean isExpired() {
        // The "using" instance dies after one duration; the live weapon in
        // the player's slot persists and keeps re-arming via use().
        if (active && timeSinceUse >= duration) {
            active = false;
            return true;
        }
        return false;
    }

    @Override
    public void upgrade() {
        this.setLevel(Integer.parseInt(getLevel()) + 1);
        damage *= 1.15;
        switch (level) {
            case 3, 5, 7 -> amount += 1;
            default -> { }
        }
        angularSpeed *= 1.05;
        duration *= 1.05;
        setCooldown(Math.max(1.0, getCooldown() * 0.95));
    }

    @Override
    public GameObject copy() {
        return new Bible(this);
    }

    @Override public void increaseDamage(double multiplier)  { damage *= multiplier; }
    @Override public void increaseSize(double multiplier)    { bookSize *= multiplier; radius *= Math.sqrt(multiplier); }
    @Override public void increaseAmount(int delta)          { amount += delta; }
    @Override public void increaseSpeed(double multiplier)   { angularSpeed *= multiplier; }
    @Override public void increaseDuration(double multiplier){ duration *= multiplier; }
    @Override public void decreaseCooldown(double multiplier){ setCooldown(getCooldown() * multiplier); }
}

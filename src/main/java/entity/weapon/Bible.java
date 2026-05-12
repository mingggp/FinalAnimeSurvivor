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
 * Bible — a Vampire-Survivors-style orbital weapon. On each cooldown a wave
 * of book projectiles spawns and rotates around the player, damaging any
 * enemy they pass through. The active wave lives for {@code duration}
 * seconds. Upgrades scale damage, count, size, speed, duration and cooldown.
 */
public class Bible extends Weapon
        implements DamageIncreasable, SizeIncreasable, AmountIncreasable,
                   SpeedIncreasable, DurationIncreasable, CooldownDecreasable {

    private final GameManager gameManager;
    private final Image sprite;

    private double damage;
    private double radius;       // orbit radius
    private double bookSize;     // sprite render size
    private double angularSpeed; // radians per second
    private double duration;     // how long an active wave lives
    private int amount;          // how many books in the wave
    private double hitInterval;
    private double timeSinceLastHit;
    private double phase;        // current rotation angle for the wave
    private boolean active;      // true while a wave is in flight

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

package entity.weapon;

import core.GameManager;
import entity.accessory.Accessory;
import entity.accessory.SixEye;
import entity.enemy.Enemy;
import entity.weapon.evolvedWeapon.MaximumOutputInfinity;
import entityInterface.GameObject;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.Evolvable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.SoundManager;

import java.util.Objects;

/**
 * Weapon — Infinity / Mugen (Gojo's starting weapon).
 *
 * <p>Projects a persistent circular field centred on the player that deals
 * damage to any enemy overlapping its radius every {@link #hitInterval} seconds.
 * The field has no cooldown (always active) and never expires.
 *
 * <p>Can evolve into {@link MaximumOutputInfinity} when the player has
 * {@link entity.accessory.SixEye} equipped and Infinity is at max level.
 */
public class Infinity extends Weapon implements Evolvable, DamageIncreasable, SizeIncreasable {

    /** Game manager — provides player position and enemy list. */
    private final GameManager gameManager;

    /** Animated infinity-ring sprite. */
    private final Image sprite;

    /** Radius of the damage field in pixels; grows with upgrades and SizeIncreasable accessories. */
    private double radius;

    /** Damage dealt to enemies per hit interval. */
    private double damage;

    /** AABB hitbox centred on the player; updated each frame. */
    private BoundingBox hitbox;

    /** Lifespan in seconds (very large — effectively permanent). */
    private double duration;

    /** Minimum seconds between damage ticks. */
    private double hitInterval;

    /** Accumulator for the hit-interval timer. */
    private double timeSinceLastHit;

    /** Animation frame counter for the rotating sprite. */
    private int spriteCounter;




    public Infinity(GameManager gameManager) {
        super("Mugen", 8, 0);
        this.gameManager = gameManager;
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/infinityIcon.png"));
        this.sprite = utils.SpriteManager.loadImage("weapon/asset/infinity.png");
        timeSinceUse = this.getCooldown();
        radius = 144;
        damage = 3;
        duration = 100;
        hitInterval = 0.15;
        timeSinceLastHit =0.5;
        spriteCounter=0;
    }
    public Infinity(Infinity infinity){
        super(infinity.getName(), infinity.getMaxLevel(), infinity.getCooldown());
        this.gameManager = infinity.gameManager;
        this.setIcon(infinity.getIcon());
        this.sprite = infinity.sprite;
        this.radius= infinity.radius;
        this.damage = infinity.damage;
        this.duration = infinity.duration;
        this.hitInterval = infinity.hitInterval;
        this.timeSinceLastHit = infinity.timeSinceLastHit;
        this.spriteCounter = infinity.spriteCounter;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse =0;
            if(!gameManager.getUsingWeaponList().contains(this))gameManager.getUsingWeaponList().add(this);
        }

    }

    @Override
    public void update(double accumulateDeltaTime) {
        this.setMapX(gameManager.getCharacter().getMapX());
        this.setMapY(gameManager.getCharacter().getMapY());
        if(timeSinceUse<duration && timeSinceLastHit > hitInterval) {
            timeSinceLastHit =0;
            SoundManager.getInstance().playSFX(SoundManager.SFX_GOJO_ATK);
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                if (((enemy.getMapX()-this.getMapX())*
                     (enemy.getMapX()-this.getMapX())+
                    ((enemy.getMapY()-this.getMapY())*
                     (enemy.getMapY()-this.getMapY())))
                        < radius*radius){
                    enemy.takeDamage(damage, 0, 0.05, Color.WHITE);
                }
            }
        }
        timeSinceUse+=accumulateDeltaTime;
        timeSinceLastHit+=accumulateDeltaTime;
        spriteCounter ++;
        if(spriteCounter>2)spriteCounter=0;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(sprite, spriteCounter*192,0 ,192,192,gameManager.getScreenX(this.getMapX())-radius,gameManager.getScreenY(this.getMapY())-radius,2*radius,2*radius);
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void upgrade() {
        this.setLevel(Integer.parseInt(getLevel())+1);
        radius*=1.1;
        damage *= 1.2;
    }

    @Override
    public GameObject copy() {
        return new Infinity(this);
    }

    @Override
    public boolean isEvolvable() {
        for(Accessory accessory : gameManager.getAccessoryList()){
            if(accessory instanceof SixEye){
                return Objects.equals(this.getLevel(), "Max");
            }
        }
        return false;
    }

    @Override
    public void Evolve(int index) {
        gameManager.getUsingWeaponList().remove(this);
        gameManager.getWeaponList()[index] = new MaximumOutputInfinity(this.gameManager);
    }

    @Override
    public void increaseSize(double multiplier) {
        this.radius*=multiplier;
    }
    @Override
    public void increaseDamage(double multiplier) {
        damage*=multiplier;
    }
}

package entity.weapon.evolvedWeapon;

import core.GameManager;
import entity.enemy.Enemy;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MaximumOutputInfinity extends Weapon implements SizeIncreasable, DamageIncreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private double timeSinceUse;
    private double radius;
    private double damage;
    private BoundingBox hitbox;
    private double duration;
    private double hitInterval;
    private double timeSinceLastHit;
    private int spriteCounter;

    public MaximumOutputInfinity(GameManager gameManager) {
        super("Maximum Infinity", 1, 0);
        this.gameManager = gameManager;
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/maximumInfinityIcon.png"));
        this.sprite = utils.SpriteManager.loadImage("weapon/asset/maximumInfinity.png");
        timeSinceUse = this.getCooldown();
        radius = 360;
        damage = 15;
        duration = 100;
        hitInterval = 0.15;
        timeSinceLastHit =0.5;
        spriteCounter=0;
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
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                if (((enemy.getMapX()-this.getMapX())*
                        (enemy.getMapX()-this.getMapX())+
                        ((enemy.getMapY()-this.getMapY())*
                                (enemy.getMapY()-this.getMapY())))
                        < radius*radius){
                    enemy.takeDamage(damage, 0, 0.08, Color.WHITE);
                }
            }
        }
        timeSinceUse+=accumulateDeltaTime;
        timeSinceLastHit+=accumulateDeltaTime;
        spriteCounter ++;
        if(spriteCounter>11)spriteCounter=0;
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

    }

    @Override
    public GameObject copy() {
        return null;
    }
    @Override
    public void increaseSize(double multiplier) {
        radius*=multiplier;
    }
    @Override
    public void increaseDamage(double multiplier) {
        damage*=1.1;
    }
}

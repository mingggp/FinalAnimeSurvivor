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

import java.util.Objects;

public class Infinity extends Weapon implements Evolvable , DamageIncreasable, SizeIncreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private double radius;
    private double damage;
    private BoundingBox hitbox;
    private double duration;
    private double hitInterval;
    private double timeSinceLastHit;
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

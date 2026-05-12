package entity.weapon.evolvedWeapon;

import core.GameManager;
import entity.character.Character;
import entity.enemy.Enemy;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vfx.VFXManager;

public class MaximumCleave extends Weapon implements SizeIncreasable , CooldownDecreasable, DamageIncreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private double timeSinceUse;
    private double maxRange;
    private double range;
    private double damage;
    private double hitInterval;
    private double timeSinceLastHit;
    private double animationTime;
    private int spriteCounter;
    private double duration;


    public MaximumCleave(GameManager gameManager) {
        super("Spider Web", 1, 3);
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/maximumCleaveIcon.png"));
        this.sprite = (utils.SpriteManager.loadImage("weapon/asset/SpiderWeb.png"));
        this.gameManager = gameManager;
        timeSinceUse = 10;
        maxRange = 96*4;
        damage = 100;
        hitInterval = 0.15;
        timeSinceLastHit =0.5;
        range = maxRange/10;
        duration = 0.46;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        timeSinceUse += accumulateDeltaTime;
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse =0;
            animationTime=0;
            spriteCounter=0;
            if(!gameManager.getUsingWeaponList().contains(this))gameManager.getUsingWeaponList().add(this);
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {

        timeSinceLastHit+= accumulateDeltaTime;
        animationTime += accumulateDeltaTime;
        this.setMapX(gameManager.getCharacter().getMapX());
        this.setMapY(gameManager.getCharacter().getMapY());

        if (animationTime*24>1){
            if(range<maxRange)range+=maxRange/10;
            ++spriteCounter;
            animationTime-=  1.0/24;
        }
        if(spriteCounter>10)spriteCounter=10;

        if( timeSinceLastHit >=hitInterval) {
            timeSinceLastHit = 0;
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                if (((enemy.getMapX()-this.getMapX())*
                        (enemy.getMapX()-this.getMapX())+
                        ((enemy.getMapY()-this.getMapY())*
                                (enemy.getMapY()-this.getMapY())))
                        < range*range){
                    enemy.takeDamage(damage, 0, 0.05, Color.BLACK);
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (timeSinceUse < duration) {
            gc.drawImage(sprite, spriteCounter * 256, 0, 256, 256, gameManager.getScreenX(this.getMapX()) - range, gameManager.getScreenY(this.getMapY()) - range, range * 2, range * 2);

        }
    }
    @Override
    public boolean isExpired() {
        return timeSinceUse>duration;
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
        maxRange*=multiplier;
        range=maxRange/10;
    }
    @Override
    public void decreaseCooldown(double multiplier){
        setCooldown(this.getCooldown()*multiplier);
    }
    @Override
    public void increaseDamage(double multiplier) {
        damage*=1.1;
    }
}

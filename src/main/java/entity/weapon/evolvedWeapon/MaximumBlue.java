package entity.weapon.evolvedWeapon;

import core.GameManager;
import entity.enemy.Enemy;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.DurationIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


import java.util.ArrayList;

public class MaximumBlue extends Weapon implements SizeIncreasable , DurationIncreasable , CooldownDecreasable, DamageIncreasable {
    private final GameManager gameManager;
    private final Image sprite;
    private double animationTime;
    private double damage;
    private double radius;
    private double duration;
    private int spriteCounterRow;
    private int spriteCounterCol;
    private int sourceSize = 512;
    private double timeSinceLastHit;
    private double hitInterval;
    private double dx;
    private double dy;
    private double speed;
    private ArrayList<Enemy> hittableEnemyList;

    public MaximumBlue(GameManager gameManager) {
        super("Maximum Blue", 1, 4);
        this.gameManager = gameManager;
        this.sprite = utils.SpriteManager.loadImage("weapon/asset/MaximumBlue.png");
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/maximumBlueIcon.png"));
        timeSinceUse = this.getCooldown();
        damage = 20;
        duration = 8;
        spriteCounterRow=0;
        spriteCounterCol=0;
        speed = 60;
        radius=256;
        hitInterval=0.15;
        timeSinceLastHit=0.4;
        hittableEnemyList=new ArrayList<>();
    }
    public MaximumBlue(MaximumBlue maximumBlue) {
        super(maximumBlue.getName(), 1, maximumBlue.getCooldown());
        this.gameManager = maximumBlue.gameManager;
        this.sprite = maximumBlue.sprite;
        this.setIcon(maximumBlue.getIcon());
        animationTime=0;
        timeSinceUse = 0;
        damage = maximumBlue.damage;
        duration = maximumBlue.duration;
        spriteCounterRow=0;
        spriteCounterCol=0;
        speed = maximumBlue.speed;
        radius= maximumBlue.radius;
        hitInterval=maximumBlue.hitInterval;
        timeSinceLastHit=0.4;
        hittableEnemyList=new ArrayList<>();
    }

    @Override
    public void use(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse =0;

            Enemy closest = gameManager.getClosestTarget();
            MaximumBlue maximumBlue = new MaximumBlue(this);

            if(closest != null){
                maximumBlue.setMapX(closest.getMapX());
                maximumBlue.setMapY(closest.getMapY());
                maximumBlue.dx = -closest.getDx();
                maximumBlue.dy = -closest.getDy();
            }
            else {
                maximumBlue.setMapX(gameManager.getCharacter().getMapX());
                maximumBlue.setMapY(gameManager.getCharacter().getMapY());
                maximumBlue.dx =Math.random();
                maximumBlue.dy = Math.sqrt(1-maximumBlue.dx*maximumBlue.dx);
                if (Math.random()>0.5)maximumBlue.dx*=-1;
                if (Math.random()>0.5)maximumBlue.dy*=-1;
            }
            gameManager.getUsingWeaponList().add(maximumBlue);
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        animationTime+=accumulateDeltaTime;
        timeSinceLastHit+=accumulateDeltaTime;
        if (animationTime*24>1){
            ++spriteCounterCol;
            if(spriteCounterCol>4){
                spriteCounterCol=0;
                ++spriteCounterRow;
            }
            animationTime-=  1.0/24;
        }

        if(spriteCounterRow==3 && spriteCounterCol==3){
                spriteCounterCol = 1;
        }

        this.setMapX(this.getMapX() + (dx * speed * accumulateDeltaTime));
        this.setMapY(this.getMapY() + (dy * speed * accumulateDeltaTime));

        if(timeSinceUse<duration) {
            if( timeSinceLastHit > hitInterval){
                timeSinceLastHit=0;
                for(Enemy hitableenemy : hittableEnemyList) {
                    if(Math.random()>0.45)hitableenemy.takeDamage(damage, 0, 0, Color.SKYBLUE);
                }
            }
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                double distanceX  = enemy.getMapX()-this.getMapX();
                double distanceY  = enemy.getMapY()-this.getMapY();
                double distance = Math.hypot(distanceX, distanceY);
                if (distance < radius){
                    if(enemy.isKnockBack()){
                        enemy.setDx(distanceX/distance);
                        enemy.setDy(distanceY/distance);
                    }
                    else{
                        enemy.setDx(-distanceX/distance);
                        enemy.setDy(-distanceY/distance);
                    }

                    if(!hittableEnemyList.contains(enemy))hittableEnemyList.add(enemy);
                }
                else {
                    hittableEnemyList.remove(enemy);
                }
            }
        }
        hittableEnemyList.removeIf(Enemy::isDead);
    }

    @Override
    public void render(GraphicsContext gc) {

        if (timeSinceUse < duration) {
            gc.drawImage(sprite, spriteCounterCol * sourceSize, spriteCounterRow * sourceSize, sourceSize, sourceSize, gameManager.getScreenX(this.getMapX()) - radius, gameManager.getScreenY(this.getMapY()) - radius, radius*2, radius*2);
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
        radius*=multiplier;
    }
    public void increaseDuration(double multiplier){
        duration*=multiplier;
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

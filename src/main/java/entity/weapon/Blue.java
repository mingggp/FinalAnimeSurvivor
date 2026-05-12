package entity.weapon;

import core.GameManager;
import entity.accessory.Accessory;
import entity.accessory.GojoGlasses;
import entity.enemy.Enemy;
import entity.weapon.evolvedWeapon.MaximumBlue;
import entityInterface.GameObject;
import entityInterface.weaponInterface.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Blue extends Weapon implements Evolvable, DamageIncreasable, SizeIncreasable , DurationIncreasable , CooldownDecreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private double timeSinceUse;
    private double animationTime;
    private double damage;
    private double radius;
    private double duration;
    private int spriteCounter;
    private final int sourceSize;
    private double stuntTime;
    private double timeSinceLastHit;
    private double hitInterval;

    public Blue(GameManager gameManager){
        super("Lapse Blue", 8, 4);
        this.gameManager = gameManager;
        this.sprite = new Image("weapon/asset/lapse.png");
        this.setIcon(new Image("weapon/icon/blue.png"));
        timeSinceUse = this.getCooldown();
        animationTime=0;
        damage = 4;
        duration = 6;
        spriteCounter=0;
        radius=192;
        sourceSize=192;
        stuntTime=0.5;
        hitInterval=0.5;
        timeSinceLastHit=hitInterval;
    }

    public Blue(Blue blue){
        super(blue.getName(), blue.getMaxLevel(), blue.getCooldown());
        this.gameManager = blue.gameManager;
        this.sprite = blue.sprite;
        this.setIcon(blue.getIcon());
        timeSinceUse = 0;
        animationTime=0;
        damage = blue.damage;
        duration = blue.duration;
        spriteCounter=0;
        radius= blue.radius;
        sourceSize=192;
        stuntTime= blue.stuntTime;
        hitInterval=blue.hitInterval;
        timeSinceLastHit= blue.hitInterval;;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        if(timeSinceUse >= this.getCooldown()){
            timeSinceUse =0;
            Blue blue = new Blue(this);
            blue.setMapX(gameManager.getClosestTarget().getMapX());
            blue.setMapY(gameManager.getClosestTarget().getMapY());
            gameManager.getUsingWeaponList().add(blue);
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        animationTime+=accumulateDeltaTime;
        timeSinceLastHit+=accumulateDeltaTime;
        if (animationTime*24>1){
            ++spriteCounter;
            animationTime-=  1.0/24;
        }
        if(spriteCounter>6)spriteCounter=4;

        if(timeSinceUse<duration && timeSinceLastHit > hitInterval) {
            timeSinceLastHit=0;
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                double distanceX  = enemy.getMapX()-this.getMapX();
                double distanceY  = enemy.getMapY()-this.getMapY();
                double distance   = Math.hypot(distanceX, distanceY);
                if (distance < radius){
                    enemy.takeDamage(damage,stuntTime,0,Color.TEAL);
                }
            }
        }


    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(sprite, spriteCounter * sourceSize, 0, sourceSize, sourceSize, gameManager.getScreenX(this.getMapX()) - radius, gameManager.getScreenY(this.getMapY()) - radius, radius*2, radius*2);
    }
    @Override
    public void upgrade(){
        this.setLevel(Integer.parseInt(getLevel())+1);
        this.damage*=1.1;
        this.stuntTime *=1.05;
        radius*=1.04;
        setCooldown(getCooldown()*0.9);
    }

    @Override
    public boolean isExpired() {
        return timeSinceUse>=duration;
    }

    @Override
    public GameObject copy() {
        return new Blue(this);
    }

    @Override
    public boolean isEvolvable() {
        for(Accessory accessory : gameManager.getAccessoryList()){
            if(accessory instanceof GojoGlasses){
                return Objects.equals(this.getLevel(), "Max");
            }
        }
        return false;
    }

    @Override
    public void Evolve(int index) {
        gameManager.getWeaponList()[index] = new MaximumBlue(this.gameManager);
    }
    @Override
    public void increaseSize(double multiplier) {
        this.radius*=multiplier;

    }
    @Override
    public void increaseDuration(double multiplier){
        duration*=multiplier;
    }
    @Override
    public void decreaseCooldown(double multiplier){
        setCooldown(this.getCooldown()*multiplier);
    }

    @Override
    public void increaseDamage(double multiplier) {
        damage*=multiplier;
    }
}

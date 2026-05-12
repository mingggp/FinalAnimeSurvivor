package entity.weapon;

import core.GameManager;
import entity.accessory.Accessory;
import entity.accessory.Blindfold;
import entity.enemy.Enemy;
import entity.weapon.evolvedWeapon.MaximumRed;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.Evolvable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Red extends Weapon implements Evolvable, DamageIncreasable, SizeIncreasable , CooldownDecreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private double timeSinceUse;
    private double animationTime;
    private double damage;
    private double radius;
    private double ballSize;
    private BoundingBox hitbox;
    private double duration;
    private int spriteCounter;
    private final int sourceSize;
    private double dx;
    private double dy;
    private double speed;
    private boolean isExploded;
    private double distanceTraveled;

    public Red(GameManager gameManager){
        super("Reversal Red", 8, 3);
        this.gameManager = gameManager;
        this.sprite = utils.SpriteManager.loadImage("weapon/asset/reversal.png");
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/red.png"));
        timeSinceUse = this.getCooldown();
        animationTime=0;
        damage = 20;
        duration = 2;
        spriteCounter=0;
        speed = 600;
        radius=128;
        ballSize=radius/6;
        isExploded=false;
        sourceSize=192;
    }
    public Red(Red red){
        super(red.getName(), red.getMaxLevel(),red.getCooldown());
        this.gameManager = red.gameManager;
        this.sprite = red.sprite;
        this.setIcon(red.getIcon());
        timeSinceUse = 0;
        animationTime=0;
        damage = red.damage;
        duration = red.duration;
        spriteCounter=0;
        speed = red.speed;
        radius= red.radius;
        ballSize=red.ballSize;
        isExploded=false;
        sourceSize=192;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse =0;
            Red red =  new Red(this);
            red.setMapX(gameManager.getCharacter().getMapX());
            red.setMapY(gameManager.getCharacter().getMapY());
            if( gameManager.getClosestTarget()!=null){
                red.dx = -gameManager.getClosestTarget().getDx();
                red.dy = -gameManager.getClosestTarget().getDy();
            }
            else {
                red.dx = Math.random();
                red.dy = Math.sqrt(1-red.dx*red.dx);
                if (Math.random()>0.5)red.dx*=-1;
                if (Math.random()>0.5)red.dy*=-1;
            }

            gameManager.getUsingWeaponList().add(red);
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        animationTime+=accumulateDeltaTime;
        if (animationTime*24>1){
            ++spriteCounter;
            animationTime-=  1.0/24;
        }
        if(spriteCounter>2 && !isExploded){
            spriteCounter=0;
        }




        this.hitbox=new BoundingBox(this.getMapX()-ballSize/2,this.getMapY()-ballSize/2,ballSize,ballSize);

        if(!isExploded) {

            distanceTraveled+= Math.hypot(dx*speed*accumulateDeltaTime,dy*speed*accumulateDeltaTime);
            this.setMapX(this.getMapX() + (dx * speed * accumulateDeltaTime));
            this.setMapY(this.getMapY() + (dy * speed * accumulateDeltaTime));


            if (distanceTraveled <96*5) {
                for (Enemy enemy : gameManager.getInGameEnemyList()) {
                    if (hitbox.intersects(enemy.getHitbox())) {
                        spriteCounter=3;
                        isExploded = true;
                        for (Enemy enemy2 : gameManager.getInGameEnemyList()) {
                            double distanceX = enemy2.getMapX() - this.getMapX();
                            double distanceY = enemy2.getMapY() - this.getMapY();
                            if (distanceX * distanceX + distanceY * distanceY < radius * radius) {
                                enemy2.takeDamage(damage, 0, 0.25, Color.INDIANRED);
                            }
                        }
                        break;
                    }
                }
            } else {
                isExploded = true;
                spriteCounter=3;
                for (Enemy enemy2 : gameManager.getInGameEnemyList()) {
                    double distanceX = enemy2.getMapX() - this.getMapX();
                    double distanceY = enemy2.getMapY() - this.getMapY();
                    if (distanceX * distanceX + distanceY * distanceY < radius * radius) {
                        enemy2.takeDamage(damage, 0, 0.25, Color.INDIANRED);
                    }
                }
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if(spriteCounter>12)return;
        gc.drawImage(sprite, spriteCounter * sourceSize, 0, sourceSize, sourceSize, gameManager.getScreenX(this.getMapX()) - radius, gameManager.getScreenY(this.getMapY()) - radius, radius*2, radius*2);
    }
    @Override
    public void upgrade(){
        this.setLevel(Integer.parseInt(getLevel())+1);
        this.damage *=1.2 ;
        radius*=1.05;
        ballSize=radius/6;
        setCooldown(getCooldown()*0.9);
    }

    @Override
    public boolean isExpired() {
        return timeSinceUse>=duration;
    }

    @Override
    public GameObject copy() {
        return new Red(this);
    }

    @Override
    public boolean isEvolvable() {
        for(Accessory accessory : gameManager.getAccessoryList()){
            if(accessory instanceof Blindfold){
                return Objects.equals(this.getLevel(), "Max");
            }
        }
        return false;
    }

    @Override
    public void Evolve(int index) {
        gameManager.getWeaponList()[index] = new MaximumRed(this.gameManager);
    }

    @Override
    public void increaseSize(double multiplier) {
        this.radius*=multiplier;
        this.ballSize=radius/6;
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

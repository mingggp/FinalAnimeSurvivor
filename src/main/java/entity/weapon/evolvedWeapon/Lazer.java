package entity.weapon.evolvedWeapon;

import core.GameManager;
import entity.enemy.Enemy;
import entity.weapon.Standard;
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

public class Lazer extends Weapon implements SizeIncreasable , DurationIncreasable , CooldownDecreasable, DamageIncreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private double timeSinceUse;
    private double width;
    private double height;
    private double damage;
    private BoundingBox hitbox;
    private double duration;
    private double hitInterval;
    private double timeSinceLastHit;
    private int spriteCounterColumn;
    private int spriteCounterRow;
    private Standard standard;

    public Lazer(GameManager gameManager,Standard standard){
        super("Lazer",1,10);
        this.gameManager = gameManager;
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/Lazer.png"));
        this.sprite = utils.SpriteManager.loadImage("weapon/asset/mania3.png");
        timeSinceUse = this.getCooldown();
        width = 64*4;
        height = 64*7;
        damage = 40;
        duration = 15;
        hitInterval = 0.3;
        timeSinceLastHit =0.5;
        this.standard = standard;
    }
    //don't actually need this anymore
    public Lazer(Lazer lazer){
        super(lazer.getName(), lazer.getMaxLevel(), lazer.getCooldown());
        this.setIcon(lazer.getIcon());
        this.gameManager = lazer.gameManager;
        this.sprite = lazer.sprite;
        this.timeSinceUse = lazer.timeSinceUse;
        this.height = lazer.height;
        this.width = lazer.width;
        this.damage = lazer.damage;
        this.duration = lazer.duration;
        this.hitInterval = lazer.hitInterval;
        this.timeSinceLastHit = lazer.timeSinceLastHit;
        this.spriteCounterColumn = 0;
        this.spriteCounterRow = 0;
    }
    @Override
    public void use(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        if(timeSinceUse >= this.getCooldown()) {
            entity.enemy.Enemy target = gameManager.getClosestTarget();
            if (target != null) {
                timeSinceUse =0;
                Lazer lazer = new Lazer(this);
                lazer.setMapX(target.getMapX());
                lazer.setMapY(target.getMapY());
                lazer.hitbox = new BoundingBox(lazer.getMapX()-width/2,lazer.getMapY()-height/2,width,height);
                gameManager.getUsingWeaponList().add(lazer);
            }
        }
        standard.use(accumulateDeltaTime);
    }

    @Override
    public void update(double accumulateDeltaTime) {


        if(timeSinceUse<duration && timeSinceLastHit > hitInterval) {
            timeSinceLastHit =0;
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                if (enemy.getHitbox().intersects(this.hitbox)) {
                    enemy.takeDamage(damage, 0.25, 0, Color.PINK);
                }
            }
        }
        timeSinceUse+=accumulateDeltaTime;
        timeSinceLastHit+=accumulateDeltaTime;
        spriteCounterColumn ++;
        if(spriteCounterColumn>8){
            spriteCounterColumn=0;
            spriteCounterRow++;
        }
        if(timeSinceUse<duration-0.66){
            if(spriteCounterRow ==4 && spriteCounterColumn ==5){
                spriteCounterRow=3;
                spriteCounterColumn =2;
            }
        }
        if(spriteCounterRow ==7 && spriteCounterColumn >4){
            spriteCounterColumn=4;
        }
        if(timeSinceUse>=duration) {
            spriteCounterColumn = 0;
            spriteCounterRow = 0;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.BLACK);
        gc.fillRect(gameManager.getScreenX(this.getMapX())-(width+20)/2,gameManager.getScreenY(this.getMapY())-(height)/2,width+20,height+10);
        gc.setGlobalAlpha(1.0);
        gc.drawImage(sprite, spriteCounterColumn*256,spriteCounterRow*448 ,256,448,gameManager.getScreenX(this.getMapX())-width/2,gameManager.getScreenY(this.getMapY())-height/2,width,height);

    }
    @Override
    public void upgrade(){

    }

    @Override
    public boolean isExpired() {
        return timeSinceUse>duration;
    }

    @Override
    public GameObject copy() {
        return new Lazer(this);
    }

    @Override
    public void increaseSize(double multiplier) {
        this.width*=multiplier;
        this.height*=multiplier;
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

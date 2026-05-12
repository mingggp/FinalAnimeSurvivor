package entity.weapon.evolvedWeapon;

import core.GameManager;
import entity.enemy.Enemy;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MaximumDismantle extends Weapon implements SizeIncreasable, CooldownDecreasable, DamageIncreasable {

    private GameManager gameManager;
    private Image sprite;
    private ArrayList<Enemy> hittedEnemyList;
    private int speed;
    private double dx;
    private double dy;
    private BoundingBox hitbox;
    private double damage;


    private double width;
    private double height;

    public MaximumDismantle(GameManager gameManager){
        super("Maximum Dismantle",1,2);
        sprite = utils.SpriteManager.loadImage("weapon/asset/maximumDismantle.png");
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/maximumDismantleIcon.png"));
        this.gameManager = gameManager;
        this.speed = 2000;
        timeSinceUse = 0;
        width = 400;
        height = 400;
        damage = 100;
    }
    public MaximumDismantle(MaximumDismantle maximumDismantle){
        super(maximumDismantle.getName(), maximumDismantle.getMaxLevel(), maximumDismantle.getCooldown());
        this.speed = maximumDismantle.speed;
        this.gameManager = maximumDismantle.gameManager;
        this.sprite = maximumDismantle.sprite;
        hittedEnemyList = new ArrayList<>();
        width = maximumDismantle.width;
        height = maximumDismantle.height;
        damage = maximumDismantle.damage;
        timeSinceUse = 0;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse = 0;
            MaximumDismantle maximumDismantle = new MaximumDismantle(this);
            maximumDismantle.setMapX(gameManager.getCharacter().getMapX());
            maximumDismantle.setMapY(gameManager.getCharacter().getMapY());
            if(gameManager.getClosestTarget()!=null) {
                maximumDismantle.dx = -gameManager.getClosestTarget().getDx();
                maximumDismantle.dy = -gameManager.getClosestTarget().getDy();
            }
            else {
                maximumDismantle.dx = Math.random();
                maximumDismantle.dy = Math.sqrt(1 - maximumDismantle.dx * maximumDismantle.dx);
                if (Math.random() > 0.5) maximumDismantle.dx *= -1;
                if (Math.random() > 0.5) maximumDismantle.dy *= -1;
            }
            gameManager.getUsingWeaponList().add(maximumDismantle);
        }
        timeSinceUse += accumulateDeltaTime;
    }

    @Override
    public void update(double accumulateDeltaTime) {
        if(timeSinceUse > 0) {
            for(Enemy enemy : gameManager.getInGameEnemyList()){
                if(!hittedEnemyList.contains(enemy) && enemy.getHitbox().intersects(hitbox)){
                    enemy.takeDamage(damage,0,0.2, Color.RED);
                    hittedEnemyList.add(enemy);
                }
            }
        }





        timeSinceUse += accumulateDeltaTime;

        this.setMapX(this.getMapX() + (dx * speed * accumulateDeltaTime));
        this.setMapY(this.getMapY() + (dy * speed * accumulateDeltaTime));
        this.hitbox = new BoundingBox(this.getMapX() -(width/2.0), this.getMapY() -(height/2.0), width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if(gameManager.isCloseEnoughToRender(mapX,mapY)) {

            gc.save();

            gc.translate(gameManager.getScreenX(mapX), gameManager.getScreenY(mapY));
            gc.rotate(Math.toDegrees(Math.atan2(dy, dx)));
            gc.drawImage(sprite, -width/2, -height/2,width,height);

            gc.restore();
        }
    }

    @Override
    public boolean isExpired() {
        return timeSinceUse > 1;
    }

    @Override
    public void upgrade() {

    }

    @Override
    public GameObject copy() {
        return null;
    }
    @Override
    public void decreaseCooldown(double multiplier){
        setCooldown(this.getCooldown()*multiplier);
    }
    @Override
    public void increaseSize(double multiplier) {
        this.width*=multiplier;
        this.height*=multiplier;
    }
    @Override
    public void increaseDamage(double multiplier) {
        damage*=1.1;
    }
}

package entity.weapon;

import core.GameManager;
import entity.accessory.Accessory;
import entity.accessory.SukunaArm;
import entity.enemy.Enemy;
import entity.weapon.evolvedWeapon.MaximumDismantle;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.Evolvable;
import entityInterface.weaponInterface.SizeIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Objects;

public class Dismantle extends Weapon implements Evolvable, DamageIncreasable, SizeIncreasable , CooldownDecreasable {

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

    //for all weapon list
    public Dismantle(GameManager gameManager){
        super("Dismantle",8,3);
        sprite = utils.SpriteManager.loadImage("weapon/asset/dismantle.png");
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/dismantle.png"));
        this.gameManager = gameManager;
        this.speed = 1000;
        timeSinceUse = 0;
        width = 100;
        height = 100;
        damage = 10;
    }
    //for copy
    public Dismantle(Dismantle dismantle){
        super(dismantle.getName(), dismantle.getMaxLevel(), dismantle.getCooldown());
        sprite = dismantle.sprite;
        this.setIcon(dismantle.getIcon());
        this.gameManager = dismantle.gameManager;
        this.speed = dismantle.speed;
        timeSinceUse = 0;
        width = dismantle.width;
        height = dismantle.height;
        damage = dismantle.damage;
        hittedEnemyList = new ArrayList<>();
    }
    @Override
    public void use(double accumulateDeltaTime){
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse = 0;
            Dismantle dismantle = new Dismantle(this);
            dismantle.setMapX(gameManager.getCharacter().getMapX());
            dismantle.setMapY(gameManager.getCharacter().getMapY());
            if(gameManager.getClosestTarget()!=null) {
                dismantle.dx = -gameManager.getClosestTarget().getDx();
                dismantle.dy = -gameManager.getClosestTarget().getDy();
            }
            else {
                dismantle.dx = Math.random();
                dismantle.dy = Math.sqrt(1 - dismantle.dx * dismantle.dx);
                if (Math.random() > 0.5) dismantle.dx *= -1;
                if (Math.random() > 0.5) dismantle.dy *= -1;
            }
            gameManager.getUsingWeaponList().add(dismantle);
        }
        timeSinceUse += accumulateDeltaTime;
    }
    @Override
    public void update(double accumulateDeltaTime){



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
    public void render(GraphicsContext gc){
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
    public boolean isExpired(){
        return timeSinceUse > 1;
    }
    @Override
    public GameObject copy(){
        return new Dismantle(this);
    }

    @Override
    public void upgrade(){
        this.setLevel(Integer.parseInt(getLevel())+1);
        width *= 1.1;
        height *= 1.1;
        setCooldown(getCooldown()*0.95);
        damage *=1.1;
    }


    @Override
    public boolean isEvolvable() {

        for(Accessory accessory : gameManager.getAccessoryList()){
            if(accessory instanceof SukunaArm){
                return Objects.equals(this.getLevel(), "Max");
            }
        }
        return false;
    }

    @Override
    public void Evolve(int index) {
        gameManager.getWeaponList()[index] = new MaximumDismantle(this.gameManager);
    }
    @Override
    public void increaseSize(double multiplier) {
        this.width*=multiplier;
        this.height*=multiplier;
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

package entity.weapon;

import core.GameManager;
import entity.accessory.Accessory;
import entity.accessory.KeyPad;
import entity.enemy.Enemy;
import entity.weapon.evolvedWeapon.Lazer;
import entityInterface.GameObject;
import entityInterface.weaponInterface.*;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Standard extends Weapon implements Evolvable , DamageIncreasable,SizeIncreasable , DurationIncreasable , CooldownDecreasable {

    private final GameManager gameManager;
    private final Image cursor;
    private final Image trail;
    private double timeSinceUse;
    private double width;
    private double height;
    private double damage;
    private double speed;
    private BoundingBox hitbox;
    private double duration;
    private double dx;
    private double dy;
    private double hitInterval;
    private double timeSinceLastHit;
    private ArrayList<Double> trailX;
    private ArrayList<Double> trailY;

    public Standard(GameManager gameManager){
        super("OSU!",8,2);
        this.gameManager = gameManager;
        this.setIcon(new Image("weapon/icon/osu.png"));
        this.cursor = new Image("weapon/asset/cursor.png");
        this.trail = new Image("weapon/asset/cursorTrail.png");
        timeSinceUse = this.getCooldown();
        this.width =64;
        this.height = 64;
        this.damage = 10;
        this.duration = 10;
        this.speed = 1000;
        this.hitInterval = 0.15;
        this.timeSinceLastHit = 0.16;

    }
    public Standard(Standard standard){
        super(standard.getName(),standard.getMaxLevel(), standard.getCooldown());
        this.gameManager = standard.gameManager;
        this.setIcon(standard.getIcon());
        this.cursor = standard.cursor;
        this.trail = standard.trail;
        timeSinceUse = this.getCooldown();
        this.width = standard.width;
        this.height = standard.height;
        this.damage = standard.damage;
        this.duration = standard.duration;
        this.speed = standard.speed;
        this.hitInterval = 0.15;
        this.timeSinceLastHit = 0.16;
        this.trailX = new ArrayList<>();
        this.trailY = new ArrayList<>();
    }

    @Override
    public void use(double accumulateDeltaTime) {
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse =0;
            //Standard standard = (Standard) this.copy();
            Standard standard = new Standard(this);
            standard.setMapX(gameManager.getCharacter().getMapX());
            standard.setMapY(gameManager.getCharacter().getMapY());
            if(gameManager.getClosestTarget()!=null) {
                standard.dx = -gameManager.getClosestTarget().getDx();
                standard.dy = -gameManager.getClosestTarget().getDy();
            }
            else {
                standard.dx =Math.random();
                standard.dy = Math.sqrt(1-standard.dx*standard.dx);
                if (Math.random()>0.5)standard.dx*=-1;
                if (Math.random()>0.5)standard.dy*=-1;
            }
            Collections.addAll(standard.trailX,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
            Collections.addAll(standard.trailY,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
            this.gameManager.getUsingWeaponList().add(standard);
        }
        timeSinceUse+=accumulateDeltaTime;
    }

    @Override
    public void update(double accumulateDeltaTime) {

        timeSinceUse += accumulateDeltaTime;
        timeSinceLastHit += accumulateDeltaTime;


        if(this.getMapX()-gameManager.getCharacter().getMapX()>=960-width/2.0){
            dx = -Math.abs(dx);
        }
        else if(this.getMapX()-gameManager.getCharacter().getMapX()<=-960+width/2.0){
            dx = Math.abs(dx);
        }
        if(this.getMapY()-gameManager.getCharacter().getMapY()>=540-height/2.0){
            dy = -Math.abs(dy);
        }
        else if(this.getMapY()-gameManager.getCharacter().getMapY()<=-540+height/2.0){
            dy = Math.abs(dy);
        }

        this.setMapX(this.getMapX() + (dx * speed * accumulateDeltaTime));
        this.setMapY(this.getMapY() + (dy * speed * accumulateDeltaTime));

        trailX.removeFirst();
        trailX.add(this.getMapX());
        trailY.removeFirst();
        trailY.add(this.getMapY());

        this.hitbox = new BoundingBox(this.getMapX() -(width/2.0), this.getMapY() -(height/2.0), width, height);


        if(timeSinceUse<duration && timeSinceLastHit > hitInterval) {
            timeSinceLastHit =0;
            for (Enemy enemy : gameManager.getInGameEnemyList()) {
                if (enemy.getHitbox().intersects(this.hitbox)) {
                    enemy.takeDamage(damage, 0.1, 0, Color.DARKCYAN);
                }
            }
        }



    }

    @Override
    public void render(GraphicsContext gc) {
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        for(int i =0 ;i < 10;i++){
            gc.drawImage(this.trail,gameManager.getScreenX(trailX.get(i))-2*i,gameManager.getScreenY(trailY.get(i))-2*i,4*i,4*i);
        }

        if(gameManager.isCloseEnoughToRender(mapX,mapY)) {
            gc.drawImage(this.cursor, gameManager.getScreenX(mapX)-width/2, gameManager.getScreenY(mapY)-height/2,width,height);
        }

    }

    @Override
    public boolean isExpired() {
        return timeSinceUse>=duration;
    }

    @Override
    public void upgrade() {
        this.setLevel(Integer.parseInt(getLevel())+1);
        width *= 1.026;
        height *= 1.026;
        setCooldown(getCooldown()*0.9);
        damage *=1.1;
    }

    @Override
    public GameObject copy() {
        return new Standard(this);
    }

    @Override
    public boolean isEvolvable() {
        for(Accessory accessory : gameManager.getAccessoryList()){
            if(accessory instanceof KeyPad){
                return Objects.equals(this.getLevel(), "Max");
            }
        }
        return false;
    }

    @Override
    public void Evolve(int index) {
        gameManager.getWeaponList()[index] = new Lazer(this.gameManager,this);
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
        damage*=multiplier;
    }
}

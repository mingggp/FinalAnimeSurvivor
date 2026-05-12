package entity.character;

import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.CollisionChecker;
import utils.InputManager;
import entity.Entity;

public class Character extends Entity {


    private double currentHP;
    private double maxHP;
    private double speed;
    private int width,height;
    private Image icon; //100x100
    private Image sprite; //96x96
    private double magnetRadius;
    private Weapon starterWeapon;
    private BoundingBox hitbox;
    private double dx,dy;
    private double iFrameTime;
    private double healInterval;

    public Character( String name, int speed,  double maxHP,int magnetRadius,Weapon weapon,Image icon,Image sprite) {
        this.width = 32;
        this.height = 48;
        this.setName(name);
        this.speed = speed;
        this.setMaxHP(maxHP);
        this.setCurrentHP(maxHP);
        this.icon = icon;
        this.sprite = sprite;
        this.magnetRadius = magnetRadius;
        this.starterWeapon = weapon;

    }
    public Character(Character character){
        this.width = character.getWidth();
        this.height = character.getHeight();
        this.setName(character.getName());
        this.setSpeed(character.getSpeed());
        this.setCurrentHP(character.getCurrentHP());
        this.setMaxHP(character.getMaxHP());
        this.icon = character.icon;
        this.sprite = character.sprite;
        this.magnetRadius = character.magnetRadius;
        this.starterWeapon = character.starterWeapon;
    }
    public void update(InputManager inputManager, double accumulateDeltaTime) {

        iFrameTime -= accumulateDeltaTime;
        healInterval -= accumulateDeltaTime;

        if(healInterval<0 && currentHP<maxHP && !isDead()){
            currentHP += maxHP/100;
            healInterval = 1;
            if(currentHP>maxHP) currentHP=maxHP;
        }

        dx = 0;
        dy = 0;
        if (inputManager.isKeyPressed("W")) dy -= 1;
        if (inputManager.isKeyPressed("S")) dy += 1;
        if (inputManager.isKeyPressed("A")) dx -= 1;
        if (inputManager.isKeyPressed("D")) dx += 1;

        if (dx != 0 && dy != 0) {
            double length = Math.hypot(dx, dy);
            dx /= length; dy /= length;
        }

        CollisionChecker.checkTileCollision(this,accumulateDeltaTime);

        this.setMapX(this.getMapX() + (dx * this.getSpeed() * accumulateDeltaTime));
        this.setMapY(this.getMapY() + (dy * this.getSpeed() * accumulateDeltaTime));

        this.hitbox = new BoundingBox(this.getMapX() - ( this.getWidth() / 2.0), this.getMapY() , this.getWidth(), this.getHeight());

    }
    public void render(GraphicsContext gc){
        gc.drawImage(sprite, 960-48, 540-48);
        gc.setFill(Color.BLACK);
        gc.fillRect(960-48,540+60,96,20);
        gc.setFill(Color.RED);
        gc.fillRect(960-45,540+63,90*(currentHP/maxHP),14);

    }
    public void receiveDamage(double amount){
        if(iFrameTime <= 0 && currentHP>0) {
            currentHP -= amount;
            iFrameTime = 0.2;
        }
    }

    public void heal(double amount){
        this.currentHP += amount;
        if(currentHP>maxHP)currentHP=maxHP;
    }
    public double getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(double currentHP) {
        this.currentHP = currentHP;
    }
    public double getMaxHP() {
        return maxHP;
    }
    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }
    public Image getIcon() {
        return icon;
    }

    public double getMagnetRadius() {
        return magnetRadius;
    }

    public void setMagnetRadius(double magnetRadius) {
        this.magnetRadius = magnetRadius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public BoundingBox getHitbox() {
        return hitbox;
    }

    public Weapon getStarterWeapon() {
        return starterWeapon;
    }
    public boolean isDead(){
        return currentHP<=0;
    }

    @Override
    public GameObject copy() {
        return null;
    }
}

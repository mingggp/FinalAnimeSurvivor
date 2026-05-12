package entity.enemy;

import entity.Entity;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utils.CollisionChecker;
import vfx.VFXManager;

public class Enemy extends Entity {



    private Image sprite;
    private double currentHP;
    private double maxHP;
    private double speed;
    private int width,height;;
    private BoundingBox hitbox;
    private boolean collisionOn,movable;
    private double dx,dy;
    private double stuntTimer;
    private double knockbackTimer;

    // normal construct for allEnemyList
    public Enemy( String name, int speed, double maxHP,boolean collisionOn,boolean movable) {
        this.width=96;
        this.height=96;
        this.setName(name);
        this.speed = speed;
        this.setMaxHP(maxHP);
        this.setCurrentHP(maxHP);
        this.collisionOn = true;
        this.movable = true;
        this.setSprite( utils.SpriteManager.loadImage(name + ".png"));
    }
    // Overloaded Constructor for spawner
    public Enemy(Enemy enemy,double mapX,double mapY){
        this.width= enemy.getWidth();
        this.height= enemy.getHeight();
        this.setMapX(mapX);
        this.setMapY(mapY);
        this.setName(enemy.getName());
        this.speed = enemy.getSpeed();
        this.setCurrentHP(enemy.getCurrentHP());
        this.setMaxHP(enemy.getMaxHP());
        this.collisionOn = enemy.isCollisionOn();
        this.movable = enemy.isMovable();
        this.sprite = enemy.sprite;
        this.hitbox = new BoundingBox(this.getMapX() - ( this.getWidth() / 2.0), this.getMapY() - ( this.getHeight() / 2.0) , this.getWidth(), this.getHeight());
    }
    public void update( double accumulateDeltaTime,double characterMapX,double characterMapY) {
        stuntTimer -= accumulateDeltaTime;
        knockbackTimer -= accumulateDeltaTime;

        CollisionChecker.checkTileCollision(this,accumulateDeltaTime);
        double ex = this.getMapX();
        double ey = this.getMapY();

        if(knockbackTimer>=0){
            this.setMapX(ex - (dx * speed * accumulateDeltaTime));
            this.setMapY(ey - (dy * speed * accumulateDeltaTime));
        }
        else if(stuntTimer <=0) {
            this.setMapX(ex + (dx * speed * accumulateDeltaTime));
            this.setMapY(ey + (dy * speed * accumulateDeltaTime));
        }

        this.hitbox = new BoundingBox(this.getMapX() - ( this.getWidth() / 2.0), this.getMapY() - ( this.getHeight() / 2.0) , this.getWidth(), this.getHeight());

        dx = characterMapX - ex;
        dy = characterMapY - ey;
        double distance = Math.hypot(dx, dy);

        if (distance !=0) {
            dx /= distance;
            dy /= distance;
        }

    }
    public void render(GraphicsContext gc, double characterMapX, double characterMapY){
        double screenX = this.getMapX() - characterMapX + 960;
        double screenY = this.getMapY() - characterMapY + 540;
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if(mapX > characterMapX-960-96 && mapX < characterMapX+960+96 && mapY > characterMapY-540-96 && mapY < characterMapY+540+96) {
            gc.drawImage(this.getSprite(), screenX-48, screenY-48);
        }
    }

    public void takeDamage(double amount, double stuntDuration, double knockbackDuration, Color color){
        this.setCurrentHP(this.getCurrentHP()-amount);
        if(stuntTimer<stuntDuration)stuntTimer = stuntDuration;
        if(knockbackTimer<knockbackDuration)knockbackTimer = knockbackDuration;
        VFXManager.spawnDamageText(this.getMapX(),this.getMapY(),amount,color);
    }
    public boolean isDead(){
        return currentHP<=0;
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
    public Image getSprite() {
        return sprite;
    }
    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public double getSpeed() {
        return speed;
    }
    public boolean isKnockBack(){
        return knockbackTimer>0;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public boolean isMovable() {
        return movable;
    }

    public BoundingBox getHitbox() {
        return hitbox;
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

    @Override
    public GameObject copy() {
        return null;
    }
}

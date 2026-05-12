package entity.misc;

import core.GameManager;
import entity.Entity;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExpOrb extends Entity {

    private final int xpAmount;
    private Image red;
    //private final Image green;
    //private final Image yellow;
    //private final Image blue;
    private final int speed = 600;
    private final int width = 32;
    private final int height = 32;
    private BoundingBox hitbox;
    private GameManager gameManager;
    private boolean taggedByHarvest;

    public ExpOrb(int xpAmount, String name,GameManager gameManager){
        this.setName(name);
        this.xpAmount = xpAmount;
        this.gameManager = gameManager;
        try {
            this.red = new Image("item/redorb.png");
        } catch (Exception e) {
            this.red = null;
        }
    }
    public ExpOrb(ExpOrb expOrb , double mapX ,double mapY){
        this.setMapX(mapX);
        this.setMapY(mapY);
        this.gameManager = expOrb.gameManager;
        this.xpAmount = expOrb.xpAmount;
        this.red = expOrb.red;
        this.hitbox = new BoundingBox(mapX-16,mapY-16,width,height);
    }
    public void update( double accumulateDeltaTime){
        double ex = this.getMapX();
        double ey = this.getMapY();
        double dx = gameManager.getCharacter().getMapX() - ex;
        double dy = gameManager.getCharacter().getMapY() - ey;
        double distance = Math.hypot(dx, dy);


        if (distance < gameManager.getCharacter().getMagnetRadius() || taggedByHarvest) {
            dx /= distance;
            dy /= distance;
            this.setMapX(ex + (dx * speed * accumulateDeltaTime));
            this.setMapY(ey + (dy * speed * accumulateDeltaTime));
            this.hitbox = new BoundingBox(this.getMapX()-16,this.getMapY()-16,width,height);
        }
    }
    public void render(GraphicsContext gc){
        //double screenX = this.getMapX() - characterMapX + 960;
        //double screenY = this.getMapY() - characterMapY + 540;
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if(gameManager.isCloseEnoughToRender(mapX,mapY)) {
            gc.drawImage(this.red, gameManager.getScreenX(mapX)-16, gameManager.getScreenY(mapY)-16);
        }
    }
    public int getXpAmount(){
        return xpAmount;
    }

    public BoundingBox getHitbox() {
        return hitbox;
    }
    public void tag(){
        taggedByHarvest=true;
    }
    @Override
    public GameObject copy() {
        return null;
    }
}

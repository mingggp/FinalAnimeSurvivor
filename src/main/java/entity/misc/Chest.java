package entity.misc;

import core.GameManager;
import entity.Entity;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Chest extends Entity {

    private final GameManager gameManager;
    private Image sprite;
    private Image sprite2;
    private BoundingBox hitbox;

    public Chest(GameManager gameManager){
        this.gameManager=gameManager;
        try {
            this.sprite = utils.SpriteManager.loadImage("item/chest.png");
            this.sprite2 = utils.SpriteManager.loadImage("arrow.png");
        } catch (Exception e) {
            this.sprite = null;
            this.sprite2 = null;
        }
    }
    public Chest(Chest chest,double mapX,double mapY){
        this.gameManager = chest.gameManager;
        this.sprite = chest.sprite;
        this.sprite2 = chest.sprite2;
        this.setMapX(mapX);
        this.setMapY(mapY);
        this.hitbox = new BoundingBox(this.getMapX()-48,this.getMapY()-48,96,96);
    }

    public void render(GraphicsContext gc){
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if(gameManager.isCloseEnoughToRender(mapX,mapY)) {
            gc.drawImage(this.sprite, gameManager.getScreenX(mapX)-48, gameManager.getScreenY(mapY)-48);
            //gc.drawImage(sprite2, 960-144,540-144);
        }
        gc.save();
        gc.translate(960, 540);
        gc.rotate(Math.toDegrees(Math.atan2(mapY-gameManager.getCharacter().getMapY(),mapX-gameManager.getCharacter().getMapX())));
        gc.drawImage(sprite2, -144,-144);
        gc.restore();

    }

    @Override
    public GameObject copy() {
        return null;
    }
    public BoundingBox getHitbox(){
        return hitbox;
    }
}

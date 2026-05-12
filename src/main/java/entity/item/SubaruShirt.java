package entity.item;

import core.GameManager;
import entityInterface.GameObject;
import entityInterface.itemInterface.Droppable;
import entityInterface.itemInterface.Unique;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SubaruShirt extends Item implements Droppable , Unique {

    private GameManager gameManager;
    private BoundingBox itemHitBox;

    public SubaruShirt(GameManager gameManager) {
        super("subaruShirt");
        this.setAmount(1);
        this.gameManager = gameManager;
        this.setIcon( utils.SpriteManager.loadImage("item/icon/subaruShirt.png"));
    }
    public SubaruShirt(SubaruShirt subaruShirt){
        super(subaruShirt.getName());
        this.setAmount(1);
        this.gameManager = subaruShirt.gameManager;
        this.setIcon(subaruShirt.getIcon());
    }
    @Override
    public GameObject copy() {
        return new SubaruShirt(this);
    }

    @Override
    public void updateAsDroppedItem(double accumulateDeltaTime) {
        double ex = this.getMapX();
        double ey = this.getMapY();
        double dx = gameManager.getCharacter().getMapX() - ex;
        double dy = gameManager.getCharacter().getMapY() - ey;
        double distance = Math.hypot(dx, dy);


        if (taggedByMagnet ||distance < gameManager.getCharacter().getMagnetRadius()) {
            dx /= distance;
            dy /= distance;
            this.setMapX(ex + (dx * 400 * accumulateDeltaTime));
            this.setMapY(ey + (dy * 400 * accumulateDeltaTime));
            this.setItemHitBox(new BoundingBox(this.getMapX()-25,this.getMapY()-25,50,50));
        }
    }

    @Override
    public void renderAsDroppedItem(GraphicsContext gc) {
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if(gameManager.isCloseEnoughToRender(mapX,mapY)) {
            gc.drawImage(this.getIcon(), gameManager.getScreenX(mapX)-25, gameManager.getScreenY(mapY)-25);
        }
    }

    @Override
    public BoundingBox getItemHitBox() {
        return itemHitBox;
    }

    @Override
    public void setItemHitBox(BoundingBox itemHitBox) {
        this.itemHitBox =itemHitBox;
    }
}

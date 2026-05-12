package entity.item;

import core.GameManager;
import entityInterface.Updatable;
import entityInterface.itemInterface.Droppable;
import entityInterface.GameObject;
import entityInterface.itemInterface.Usable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.SceneManager;

/**
 * Item — Soda.
 *
 * <p>A consumable healing item that restores 1 000 HP when used.  Soda is
 * subject to a 3-second per-item cooldown (tracked by {@link #usageTime}
 * against the game timer) to prevent instant full-healing from a stack.
 * Only usable while the character's HP is below maximum.
 *
 * <p>Implements {@link Droppable} so it can appear on the ground after
 * an enemy dies, and moves toward the player when tagged by a magnet accessory.
 */
public class Soda extends Item implements Usable, Droppable, Updatable {

    /** Game manager — provides character HP, magnet radius, and game timer. */
    private GameManager gameManager;

    /** Game-timer value at the last successful use (used to enforce cooldown). */
    private double usageTime;

    /** Minimum seconds between uses of the same Soda stack. */
    private double cooldown;

    /** Axis-aligned bounding box used for pickup-collision detection on the map. */
    private BoundingBox itemHitBox;

    public Soda(GameManager gameManager){
        super("soda");
        this.setAmount(1);
        this.gameManager = gameManager;
        this.setIcon( utils.SpriteManager.loadImage("item/icon/" + this.getName() +".png"));
        usageTime = -10.0;
        this.cooldown = 3;
    }
    public Soda(Soda soda){
        super(soda.getName());
        this.setAmount(1);
        this.gameManager = soda.gameManager;
        this.setIcon(soda.getIcon());
        usageTime = -10;
        this.cooldown = soda.cooldown;
    }

    @Override
    public void use(int slot) {
        if(this.getAmount() > 0 && gameManager.getGameTimer() - usageTime > cooldown && gameManager.getCharacter().getCurrentHP()<gameManager.getCharacter().getMaxHP()){
            this.setAmount(this.getAmount()-1);
            usageTime = gameManager.getGameTimer();
            gameManager.getCharacter().heal(1000);
            SceneManager.updateBackPack(gameManager.getBackpack());

        }
    }
    @Override
    public void update(double accumulateDeltaTime) {

    }

    @Override
    public boolean isExpired() {
        return false;
    }


    @Override
    public void updateAsDroppedItem(double accumulateDeltaTime) {
        double ex = this.getMapX();
        double ey = this.getMapY();
        double dx = gameManager.getCharacter().getMapX() - ex;
        double dy = gameManager.getCharacter().getMapY() - ey;
        double distance = Math.hypot(dx, dy);


        if (taggedByMagnet || distance < gameManager.getCharacter().getMagnetRadius()) {
            dx /= distance;
            dy /= distance;
            this.setMapX(ex + (dx * 400 * accumulateDeltaTime));
            this.setMapY(ey + (dy * 400 * accumulateDeltaTime));
            this.setItemHitBox(new BoundingBox(this.getMapX()-25,this.getMapY()-25,50,50));
        }
    }

    @Override
    public void renderAsDroppedItem(GraphicsContext gc) {
        //double screenX = this.getMapX() - gameManager.getCharacter().getMapX() + 960;
        //double screenY = this.getMapY() - gameManager.getCharacter().getMapY() + 540;
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
        this.itemHitBox = itemHitBox;
    }

    @Override
    public GameObject copy() {
        return new Soda(this);
    }



}

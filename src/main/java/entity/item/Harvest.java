package entity.item;

import core.GameManager;
import entity.misc.ExpOrb;
import entityInterface.itemInterface.Droppable;
import entityInterface.GameObject;
import entityInterface.Updatable;
import entityInterface.itemInterface.Usable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.SceneManager;


/**
 * Item — Harvest (magnet / vacuum effect).
 *
 * <p>When used, spawns an active {@code Harvest} effect in the
 * {@code usingItemList} that, for {@link #duration} seconds, tags every
 * dropped exp-orb and item on the map so they are magnetically pulled toward
 * the player at 400 px/s regardless of their distance.
 *
 * <p>Subject to a 5-second cooldown between activations.
 *
 * <p>Implements {@link Droppable} so it can appear on the ground after
 * enemy drops; implements {@link Updatable} for the active-effect lifecycle.
 */
public class Harvest extends Item implements Usable, Updatable, Droppable {

    /** Game manager — provides the item and exp-orb lists and magnet radius. */
    private GameManager gameManager;

    /** Internal timer for the active-effect duration. */
    private double time;

    /** Game-timer value at the last successful use (used to enforce cooldown). */
    private double usageTime;

    /** Duration of the vacuum effect in seconds. */
    private double duration;

    /** Minimum seconds between uses. */
    private double cooldown;

    /** Axis-aligned bounding box for pickup-collision detection on the map. */
    private BoundingBox itemHitBox;

    /**
     * Master-list constructor — creates a Harvest with full stats.
     *
     * @param gameManager the game manager (source of the item / exp-orb lists)
     */
    public Harvest(GameManager gameManager) {
        super("harvest");
        this.setAmount(1);
        this.gameManager = gameManager;
        this.setIcon( utils.SpriteManager.loadImage("item/icon/" + this.getName() +".png"));
        usageTime = -10.0;
        this.duration = 5;
        this.cooldown = 5;
    }
    /**
     * Copy constructor used when spawning an active effect instance.
     *
     * @param harvest the template Harvest to copy settings from
     */
    public Harvest(Harvest harvest) {
        super(harvest.getName());
        this.setAmount(1);
        this.setIcon( harvest.getIcon());
        this.gameManager = harvest.gameManager;
        this.duration = harvest.duration;
        this.cooldown = 5;
        usageTime = -10.0;
        time = 0.0;
    }

    @Override
    public void use(int slot) {
        if(this.getAmount() > 0 && gameManager.getGameTimer() - usageTime > cooldown){
            this.setAmount(this.getAmount()-1);
            usageTime = gameManager.getGameTimer();
            gameManager.getUsingItemList().add(new Harvest(this));
            SceneManager.updateBackPack(gameManager.getBackpack());

        }/*
        else if (this.getAmount() == 1 && gameManager.getGameTimer() - usageTime > cooldown){
            gameManager.backpack[slot] = null;
            gameManager.usingItemList.add(new Harvest(this));
            SceneManager.updateBackPack(gameManager.backpack);
        }*/
    }
    @Override
    public void update(double accumulateDeltaTime){
        if(time <=duration){
            for (ExpOrb expOrb : gameManager.getDroppedExpOrbList()){
                expOrb.tag();
            }
            for (Item item:gameManager.getDroppedItemList()){
                item.tag();
            }
        }
        time += accumulateDeltaTime;
    }
    @Override
    public boolean isExpired(){
        return time >= duration;
    }
    @Override
    public void updateAsDroppedItem(double accumulateDeltaTime){
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
    public void renderAsDroppedItem(GraphicsContext gc){
        //double screenX = this.getMapX() - gameManager.getCharacter().getMapX() + 960;
        //double screenY = this.getMapY() - gameManager.getCharacter().getMapY() + 540;
        double mapX = this.getMapX();
        double mapY = this.getMapY();

        if(gameManager.isCloseEnoughToRender(mapX,mapY)) {
            gc.drawImage(this.getIcon(), gameManager.getScreenX(mapX)-25, gameManager.getScreenY(mapY)-25);
        }
    }
    @Override
    public BoundingBox getItemHitBox(){
        return itemHitBox;
    }
    @Override
    public void setItemHitBox(BoundingBox itemHitBox){
        this.itemHitBox = itemHitBox;
    }
    public GameObject copy(){
        return new Harvest(this);
    }
}

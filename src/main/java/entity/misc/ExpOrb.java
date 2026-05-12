package entity.misc;

import core.GameManager;
import entity.Entity;
import entityInterface.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * A small collectible orb that grants experience when the player walks over it.
 *
 * <p>Exp orbs are dropped by enemies when they die (with a 0.8 probability).
 * They sit on the map until the player walks within {@link entity.character.Character#getMagnetRadius()}
 * pixels, at which point they accelerate toward the player.  The
 * {@link entity.item.Harvest} item can also "tag" orbs via {@link #tag()} to
 * force them to move even if the player is far away.
 *
 * <p>Creation uses a two-step Prototype pattern:
 * a template orb is created with {@link #ExpOrb(int, String, GameManager)} and
 * stored in the master list; spawned copies are created with
 * {@link #ExpOrb(ExpOrb, double, double)} at the enemy's death position.
 */
public class ExpOrb extends Entity {

    /** XP reward granted to the player on collection. */
    private final int xpAmount;

    /** Sprite image for the red (small) orb variant. */
    private Image red;

    /** Attraction speed toward the player in pixels per second. */
    private final int speed = 600;

    /** Sprite / hitbox width in pixels. */
    private final int width = 32;

    /** Sprite / hitbox height in pixels. */
    private final int height = 32;

    /** Axis-aligned bounding box for player-pickup collision detection. */
    private BoundingBox hitbox;

    /** Game manager — provides the player position and magnet radius. */
    private GameManager gameManager;

    /**
     * Whether this orb has been tagged by the {@link entity.item.Harvest} effect,
     * causing it to move toward the player regardless of distance.
     */
    private boolean taggedByHarvest;

    public ExpOrb(int xpAmount, String name,GameManager gameManager){
        this.setName(name);
        this.xpAmount = xpAmount;
        this.gameManager = gameManager;
        try {
            this.red = utils.SpriteManager.loadImage("item/redorb.png");
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

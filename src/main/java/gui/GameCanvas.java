package gui;

import core.GameManager;
import core.GameState;
import entity.enemy.Enemy;
import entity.misc.Chest;
import entity.misc.ExpOrb;
import entity.item.Item;
import entity.misc.RenderOnTheGround;
import entity.weapon.Weapon;
import entityInterface.itemInterface.Droppable;
import entityInterface.Renderable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import tile.TileManager;
import vfx.VFXManager;


/**
 * JavaFX {@link Canvas} that renders the entire game world each frame.
 *
 * <p>{@code GameCanvas} is driven by a JavaFX {@link javafx.animation.AnimationTimer}
 * defined in the FXML controller.  Each tick:
 * <ol>
 *   <li>{@link core.GameManager#update(double)} advances the simulation.</li>
 *   <li>{@link #render(GameManager)} draws the current frame.</li>
 * </ol>
 *
 * <h2>Render order (back to front)</h2>
 * <ol>
 *   <li>Tile map (via {@link TileManager})</li>
 *   <li>Ground VFX (world-space effects beneath entities)</li>
 *   <li>Dropped items and exp-orbs</li>
 *   <li>Chests</li>
 *   <li>"Render on the ground" weapon effects (e.g. Infinity field)</li>
 *   <li>Enemies</li>
 *   <li>Player character</li>
 *   <li>Active weapon projectiles</li>
 *   <li>Damage text and slash VFX</li>
 *   <li>Screen overlay VFX (on top of everything)</li>
 *   <li>Skill pop-ups</li>
 * </ol>
 *
 * <h2>Tile sizing</h2>
 * <p>Base tile size is 64 px scaled by 1.5 → 96 px per tile.
 */
public class GameCanvas extends Canvas {

    /** 2D rendering context for all draw calls. */
    private GraphicsContext gc;

    /** Owns and renders the tile map. */
    private final TileManager tileManager;

    /** Prevents the death/result screen from being drawn more than once. */
    private boolean isResultDrawn;

    /** Base tile size before scaling. */
    private static final int originalTileSize = 64;

    /** Scaling factor applied to tile size (1.5× → 96 px tiles). */
    private static final double scale = 1.5;

    /** Final rendered tile size in pixels (96). */
    private static final int tileSize = (int) (originalTileSize * scale);

    public GameCanvas(double width, double height,GameManager gamemanager) {
        super(width, height);
        //this.gameManager = gamemanager;
        this.gc = getGraphicsContext2D();
        this.tileManager = new TileManager(gc,tileSize);

        gamemanager.setTileManager( this.tileManager);


    }
    public void render(GameManager gameManager) {

        GameState state = gameManager.getCurrentState();



        if (state == GameState.PAUSED) {

            return;
        }
        else if(state == GameState.DEATH && !isResultDrawn){

            gc.drawImage(utils.SpriteManager.loadImage("STAGECOMPLETE.png"),0,0);
            isResultDrawn = true;
            return;
        }
        else if (state != GameState.PLAYING) {
            return;
        }
        isResultDrawn=false;
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());



        tileManager.render(gameManager.getCharacter().getMapX(),gameManager.getCharacter().getMapY());

        //vfx.render ground effect here;
        VFXManager.renderGround(gc,gameManager.getCharacter().getMapX(),gameManager.getCharacter().getMapY());

        for(Item item : gameManager.getUsingItemList()){
            if( item instanceof RenderOnTheGround && item instanceof Renderable renderable){
                renderable.render(gc);
            }
        }

        for(Chest chest : gameManager.getExistingChestList()){
            chest.render(gc);
        }
        for(ExpOrb expOrb : gameManager.getDroppedExpOrbList()){
            expOrb.render(gc);
        }
        for(Item item : gameManager.getDroppedItemList()){
            ((Droppable) item).renderAsDroppedItem(gc);
        }
        for(Enemy enemy : gameManager.getInGameEnemyList()){
            enemy.render(gc,gameManager.getCharacter().getMapX(),gameManager.getCharacter().getMapY());
        }


        gameManager.getCharacter().render(gc);

        for(Weapon weapon : gameManager.getUsingWeaponList()){
            weapon.render(gc);
        }
        for(Item item : gameManager.getUsingItemList()){
            if( !(item instanceof RenderOnTheGround) && item instanceof Renderable renderable){
                renderable.render(gc);
            }
        }
        /*gc.setStroke(Color.RED);
        gc.setLineWidth(1.0);
        double hbX = 960-16; // Including your padding
        double hbY = 540 ;
        double hbW = 32;
        double hbH = 48;*/

        // hitbox debugging
        //gc.strokeRect(hbX, hbY, hbW, hbH);
        VFXManager.render(gc, gameManager.getCharacter().getMapX(),gameManager.getCharacter().getMapY());
        VFXManager.renderScreen(gc,gameManager.getCharacter().getMapX(),gameManager.getCharacter().getMapY());
        VFXManager.renderPopup(gc);
    }

}

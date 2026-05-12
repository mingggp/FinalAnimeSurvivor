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


public class GameCanvas extends Canvas{

    //private GameManager gameManager;
    //private Image menuBG;
    private GraphicsContext gc;
    private final TileManager tileManager;
    private boolean isResultDrawn;

    private static final int originalTileSize = 64;
    private static final double scale = 1.5;
    private static final int tileSize = (int) (originalTileSize*scale);

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

            gc.drawImage(new Image("STAGECOMPLETE.png"),0,0);
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

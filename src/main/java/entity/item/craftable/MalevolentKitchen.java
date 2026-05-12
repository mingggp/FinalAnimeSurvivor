package entity.item.craftable;

import core.GameManager;
import entity.enemy.Enemy;
import entity.item.Item;
import entity.item.SukunaFinger;
import entity.misc.RenderOnTheGround;
import entity.weapon.Weapon;
import entity.weapon.evolvedWeapon.MaximumCleave;
import entity.weapon.evolvedWeapon.MaximumDismantle;
import entityInterface.GameObject;
import entityInterface.Renderable;
import entityInterface.Updatable;
import entityInterface.itemInterface.Craftable;
import entityInterface.itemInterface.Unique;
import entityInterface.itemInterface.Usable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vfx.VFXManager;

public class MalevolentKitchen extends Item implements Usable , Renderable , Updatable , Craftable , Unique , RenderOnTheGround {

    private GameManager gameManager;
    private double timeSinceUse;
    private double hitInterval;
    private double timeSinceLastHit;
    private double duration;
    private double cooldown;
    private Image sprite;
    private BoundingBox hitbox;
    private int spriteCounter;
    private Image popUp;


    public MalevolentKitchen(GameManager gameManager) {
        super("Malevolent Kitchen");
        this.setIcon( new Image("item/icon/malevolentKitchen.png"));
        this.popUp = new Image("SHRINE4.png");
        this.gameManager = gameManager;
        this.duration = 30;
        this.cooldown = 60;
        this.sprite = new Image("shrine.png");
    }
   @Override
    public void use(int slot) {

        if(!gameManager.getUsingItemList().contains(this)) {
            System.out.println("Malevolent Shrine");
            double mapX = gameManager.getCharacter().getMapX();
            double mapY = gameManager.getCharacter().getMapY();
            this.setMapX(mapX);
            this.setMapY(mapY-96);
            VFXManager.setGroundEffects(Color.color(33.0/256,0,4.0/256),mapX,mapY,1920,1080,duration,true,1.0,0);
            VFXManager.spawnSkillPopUp(popUp);
            //gameManager.getBackpack()[slot] = null;
            gameManager.getUsingItemList().add(this);
            timeSinceUse = 0.0;
            hitInterval = 0.0;
            spriteCounter = 0;
            hitbox = new BoundingBox(mapX-960,mapY-540,1920,1080);
        }
        //SceneManager.updateBackPack(gameManager.getBackpack());
    }
    @Override
    public void update(double accumulateDeltaTime){
        timeSinceUse += accumulateDeltaTime;
        timeSinceLastHit += accumulateDeltaTime;
        spriteCounter ++;
        if(timeSinceUse <duration && timeSinceUse >3) {
            VFXManager.spawnSlash(gameManager.getCharacter().getMapX() - 960 + Math.random() * 1920,
                    gameManager.getCharacter().getMapY() - 540 + Math.random() * 1080, (int) (1024 + Math.random() * 1024), false);

            if (timeSinceLastHit >= hitInterval) {
                //Thread dealDamageThread = new Thread(() -> {
                for (Enemy enemy : gameManager.getInGameEnemyList()) {
                    //if (this.hitbox.intersects(enemy.getHitbox())) {
                        if(Math.random()<0.3) {
                            enemy.takeDamage(enemy.getMaxHP() , 0.05, 0, Color.DARKRED);
                        }//}
                }
                hitInterval = 0;
            }
        }
        if (spriteCounter>64)spriteCounter=64;
    }
    @Override
    public void render(GraphicsContext gc){
        if(duration- timeSinceUse <2){
            gc.setGlobalAlpha((duration- timeSinceUse)/2);
        }
        if(timeSinceUse <duration) {
            double screenX = this.getMapX() - gameManager.getCharacter().getMapX() + 960;
            double screenY = this.getMapY() - gameManager.getCharacter().getMapY() + 540;

            //if(mapX > characterMapX-960-96 && mapX < characterMapX+960 && mapY > characterMapY-540-96 && mapY < characterMapY+540) {
            gc.drawImage(sprite, 0,512-8*spriteCounter,512,8*spriteCounter,screenX-256,screenY+256-8*spriteCounter,512,8*spriteCounter);
            //}
        }
            gc.setGlobalAlpha(1.0);
    }
    @Override
    public boolean isExpired(){
        return timeSinceUse >= cooldown;
    }
    public GameObject copy(){
        return this;
    }

    @Override
    public boolean isCraftable() {
        boolean maximumDismantleFound = false;
        boolean maximumCleaveFound = false;
        boolean sukunaFingerIsEnough = false;

        for(Weapon weapon : gameManager.getWeaponList()){
            if(weapon instanceof MaximumDismantle && weapon.getLevel().equals("Max")) maximumDismantleFound = true;
            if(weapon instanceof MaximumCleave && weapon.getLevel().equals("Max"))maximumCleaveFound = true;
        }

        for(Item item : gameManager.getBackpack()){
            if(item instanceof SukunaFinger && item.getAmount() >= 20){
                sukunaFingerIsEnough = true;
                break;
            }
        }


        return maximumCleaveFound && maximumDismantleFound && sukunaFingerIsEnough;
    }

    @Override
    public void deductMaterial() {
        for(Item item : gameManager.getBackpack()){
            if(item instanceof SukunaFinger ){
                item.setAmount(item.getAmount()-20);
            }
        }
    }
}


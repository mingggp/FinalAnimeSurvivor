package entity.item.craftable;

import core.GameManager;
import entity.item.Item;
import entity.weapon.Weapon;
import entity.weapon.evolvedWeapon.MaximumBlue;
import entity.weapon.evolvedWeapon.MaximumOutputInfinity;
import entity.weapon.evolvedWeapon.MaximumRed;
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

public class UnlimitedHollowPurple extends Item implements Usable, Renderable, Updatable, Craftable, Unique {

    private final GameManager gameManager;
    private final Image sprite;
    private double timeSinceUse;
    private double animationTime;
    private double damage;
    private BoundingBox hitbox;
    private double duration;
    private int spriteCounterRow;
    private int spriteCounterCol;
    private int sourceSize = 512;
    private double actualSize;
    private boolean isEffectSpawn;
    private boolean isExploded;
    private double cooldown;





    public UnlimitedHollowPurple(GameManager gameManager) {
        super("Unlimited Hollow Purple");
        this.gameManager=gameManager;
        this.sprite = new Image("weapon/asset/MaximumRed.png");
        this.setIcon(new Image("weapon/icon/unlimitedHollowPurple.png"));
        cooldown = 12;
        timeSinceUse = cooldown;
        damage = 15;
        duration = 10;
        spriteCounterRow=0;
        spriteCounterCol=0;
        actualSize = 512;
    }

    @Override
    public void use(int slot) {
        if(!gameManager.getUsingItemList().contains(this)) {
            isEffectSpawn=false;
            isExploded=false;
            timeSinceUse =0;
            spriteCounterCol=0;
            spriteCounterRow=0;
            animationTime=0;
            actualSize=1.5*sourceSize;
            gameManager.getUsingItemList().add(this);
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {
        timeSinceUse+=accumulateDeltaTime;
        animationTime+=accumulateDeltaTime;
        this.setMapX(gameManager.getCharacter().getMapX());
        this.setMapY(gameManager.getCharacter().getMapY());
        if (animationTime*24>1){
            ++spriteCounterCol;
            if(spriteCounterCol>5){
                spriteCounterCol=0;
                ++spriteCounterRow;
            }
            animationTime-=  1.0/24;
        }
        if(timeSinceUse<=1.67 ){
            if ( spriteCounterCol>1 && spriteCounterRow==0) {
                spriteCounterCol = 0;
            }
        }
        else if(timeSinceUse<=5.4){
            //actualSize+=3;
            if(spriteCounterRow==2 && spriteCounterCol==0){
                spriteCounterCol = 2;
                --spriteCounterRow;
            }
        }
        else {
            //actualSize+=10;
        }
        if (spriteCounterRow==4 && spriteCounterCol>2)spriteCounterCol=1;
        if(duration-timeSinceUse<2 && !isEffectSpawn){
            isEffectSpawn=true;
            VFXManager.setGroundEffects(Color.BLACK,this.getMapX(),this.getMapY(),1920,1080,6,true,1,0);
            VFXManager.setScreenEffects(Color.PURPLE,this.getMapX(),this.getMapY(),1920,1080,3,true,1,0);
            VFXManager.setScreenEffects(Color.WHITE,this.getMapX(),this.getMapY(),1920,1080,4,true,1,0);
        }
        if(timeSinceUse>duration && !isExploded){

            isExploded = true;
            gameManager.getInGameEnemyList().clear();
            gameManager.getDroppedItemList().clear();
            gameManager.getExistingChestList().clear();
            gameManager.getDroppedExpOrbList().clear();
        }
    }
    @Override
    public void render(GraphicsContext gc) {
        if(timeSinceUse<1.67){
            gc.setGlobalAlpha(timeSinceUse/1.67);
        }
        if (timeSinceUse < duration) {
            gc.drawImage(sprite, spriteCounterCol * sourceSize, spriteCounterRow * sourceSize, sourceSize, sourceSize, gameManager.getScreenX(this.getMapX()) - actualSize / 2, gameManager.getScreenY(this.getMapY()) - actualSize / 2, actualSize, actualSize);

        }
        if (timeSinceUse<1.67){
            gc.setGlobalAlpha(1.0);
        }
    }
    @Override
    public boolean isExpired() {
        return timeSinceUse>cooldown;
    }

    @Override
    public boolean isCraftable() {
        boolean maximumRedFound = false;
        boolean maximumBlueFound = false;
        boolean maximumInfinityFound = false;

        for(Weapon weapon : gameManager.getWeaponList()){
            if(weapon instanceof MaximumBlue ) maximumBlueFound = true;
            if(weapon instanceof MaximumRed ) maximumRedFound = true;
            if(weapon instanceof MaximumOutputInfinity) maximumInfinityFound = true;
        }

        return maximumBlueFound && maximumRedFound && maximumInfinityFound;
    }

    @Override
    public void deductMaterial() {

    }

    @Override
    public GameObject copy(){
        return null;
    }
}

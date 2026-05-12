package entity.weapon.evolvedWeapon;

import core.GameManager;
import entity.enemy.Enemy;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import entityInterface.weaponInterface.CooldownDecreasable;
import entityInterface.weaponInterface.DamageIncreasable;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vfx.VFXManager;

public class MaximumRed extends Weapon implements CooldownDecreasable, DamageIncreasable {

    private final GameManager gameManager;
    private final Image sprite;
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

    public MaximumRed(GameManager gameManager) {
        super("Maximum Red", 1, 45);
        this.gameManager = gameManager;
        this.sprite = utils.SpriteManager.loadImage("weapon/asset/MaximumRed.png");
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/maximumRedIcon.png"));
        timeSinceUse = this.getCooldown();
        damage = 15;
        duration = 10;
        spriteCounterRow=0;
        spriteCounterCol=0;
        actualSize = 512;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        if(timeSinceUse >= this.getCooldown()) {
            isEffectSpawn=false;
            isExploded=false;
            timeSinceUse =0;
            spriteCounterCol=0;
            spriteCounterRow=0;
            animationTime=0;
            actualSize=1.5*sourceSize;
            if(!gameManager.getUsingWeaponList().contains(this))gameManager.getUsingWeaponList().add(this);
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
            actualSize+=3;
            if(spriteCounterRow==2 && spriteCounterCol==0){
            spriteCounterCol = 2;
            --spriteCounterRow;
            }
        }
        else {
            actualSize+=10;
        }
        if (spriteCounterRow==4 && spriteCounterCol>2)spriteCounterCol=1;
        if(duration-timeSinceUse<2 && !isEffectSpawn){
            isEffectSpawn=true;
            VFXManager.setScreenEffects(Color.WHITE,this.getMapX(),this.getMapY(),1920,1080,4,true,1.0,0);
        }
        if(timeSinceUse>duration && !isExploded){
            isExploded = true;
            this.hitbox = new BoundingBox(this.getMapX()-1060,this.getMapY()-640,2120,1280);
            for (Enemy enemy: gameManager.getInGameEnemyList()){
                if (hitbox.contains(enemy.getMapX(), enemy.getMapY())){
                    enemy.takeDamage(200,0,0, Color.RED);
                }
            }
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
        return timeSinceUse>getCooldown();
    }

    @Override
    public void upgrade() {

    }

    @Override
    public GameObject copy() {
        return null;
    }
    @Override
    public void decreaseCooldown(double multiplier){
        setCooldown(this.getCooldown()*multiplier);
    }

    @Override
    public void increaseDamage(double multiplier) {
        damage*=1.1;
    }
}

package entity.weapon;

import core.GameManager;
import entity.accessory.Accessory;
import entity.accessory.Blindfold;
import entity.accessory.SukunaCloak;
import entity.character.Character;
import entity.enemy.Enemy;
import entity.weapon.evolvedWeapon.MaximumCleave;
import entity.weapon.evolvedWeapon.MaximumRed;
import entityInterface.GameObject;
import entityInterface.weaponInterface.DamageIncreasable;
import entityInterface.weaponInterface.Evolvable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import vfx.VFXManager;

import java.util.Objects;

public class Cleave extends Weapon implements Evolvable , DamageIncreasable {

    private final GameManager gameManager;
    private final Image sprite;
    private int size;
    private double damage;
    private double hitInterval;
    private double timeSinceLastHit;
    private double range;
    private boolean haveTarget;

    public Cleave(GameManager gameManager){
        super("Cleave",8,0);
        this.setIcon(utils.SpriteManager.loadImage("weapon/icon/cleave.png"));
        this.sprite = (utils.SpriteManager.loadImage("weapon/asset/blackSlash.png"));
        this.gameManager = gameManager;
        timeSinceUse = 10;
        size = 288;
        damage = 10;
        hitInterval = 0.15;
        timeSinceLastHit =0.5;
        range = 192;
    }
    public Cleave(Cleave cleave){
        super(cleave.getName(), cleave.getMaxLevel(), cleave.getCooldown());
        this.setIcon(cleave.getIcon());
        this.sprite = (cleave.sprite);
        this.gameManager = cleave.gameManager;
        this.timeSinceUse = this.getCooldown();
        this.size = cleave.size;
        this.damage = cleave.damage;
        this.hitInterval = cleave.hitInterval;
        this.timeSinceLastHit = cleave.timeSinceLastHit;
        this.range = cleave.range;
    }

    @Override
    public void use(double accumulateDeltaTime) {
        if(timeSinceUse >= this.getCooldown()) {
            timeSinceUse =0;
            if(!gameManager.getUsingWeaponList().contains(this))gameManager.getUsingWeaponList().add(this);
        }
    }

    @Override
    public void update(double accumulateDeltaTime) {
        timeSinceLastHit += accumulateDeltaTime;
        haveTarget = false;
        if( timeSinceLastHit >=hitInterval) {
            timeSinceLastHit = 0;
            if(gameManager.getClosestTarget()==null)return;
            Enemy enemy = gameManager.getClosestTarget();
            Character character = gameManager.getCharacter();
                if (((enemy.getMapX() - character.getMapX()) *
                        (enemy.getMapX() - character.getMapX()) +
                        ((enemy.getMapY() - character.getMapY()) *
                                (enemy.getMapY() - character.getMapY()))) < range * range) {

                    enemy.takeDamage(damage, 0.02, 0, Color.BLACK);
                    VFXManager.spawnSlash(enemy.getMapX(), enemy.getMapY(), size,true);
                    haveTarget = true;
                }

        }
    }

    @Override
    public void render(GraphicsContext gc) {

    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void upgrade(){
        this.setLevel(Integer.parseInt(getLevel())+1);
        damage *=1.1;
        hitInterval-=0.01;
        range += 10;
    }

    @Override
    public GameObject copy() {
        return new Cleave(this);
    }

    @Override
    public boolean isEvolvable() {
        for(Accessory accessory : gameManager.getAccessoryList()){
            if(accessory instanceof SukunaCloak){
                return Objects.equals(this.getLevel(), "Max");
            }
        }
        return false;
    }

    @Override
    public void Evolve(int index) {
        gameManager.getWeaponList()[index] = new MaximumCleave(this.gameManager);
    }
    @Override
    public void increaseDamage(double multiplier) {
        damage*=multiplier;
    }
}

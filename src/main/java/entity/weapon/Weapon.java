package entity.weapon;

import entity.Entity;
import entityInterface.Renderable;
import entityInterface.Updatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Weapon extends Entity implements Updatable, Renderable {
    private Image Icon;
    protected int level;
    private int maxLevel;
    private double cooldown;
    /**
     * Time elapsed since this weapon was last fired. Each concrete weapon
     * is responsible for incrementing this value in its own tick. Stored
     * here (instead of in every subclass) so the UI can read it through
     * {@link #getCooldownProgress()}.
     */
    protected double timeSinceUse;

    public Weapon(String name,int maxLevel,double cooldown){
        this.setName(name);
        this.setLevel(1);
        this.maxLevel=maxLevel;
        this.setCooldown(cooldown);
    }

    /**
     * Returns a value in [0, 1] representing how close the weapon is to its
     * next available use. 1 means "ready to fire", values less than 1 mean
     * the weapon is currently cooling down. Weapons with cooldown {@code <= 0}
     * always return 1.
     */
    public double getCooldownProgress(){
        if (cooldown <= 0) return 1.0;
        double p = timeSinceUse / cooldown;
        if (p < 0) return 0;
        if (p > 1) return 1;
        return p;
    }

    public abstract void use(double accumulateDeltaTime);
    public abstract void update(double accumulateDeltaTime);
    public abstract void render(GraphicsContext gc);
    public abstract boolean isExpired();

    public Image getIcon() {
        return Icon;
    }
    public void setIcon(Image icon) {
        Icon = icon;
    }
    public String getLevel() {
        if(level<maxLevel){
            return String.valueOf(level);
        }
        return "Max";

    }
    public abstract void upgrade();

    public void setLevel(int level) {
        this.level = level;
    }
    public int getMaxLevel() {
        return maxLevel;
    }
    public double getCooldown() {
        return cooldown;
    }
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}

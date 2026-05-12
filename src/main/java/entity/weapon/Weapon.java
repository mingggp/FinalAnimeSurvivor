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

    public Weapon(String name,int maxLevel,double cooldown){
        this.setName(name);
        this.setLevel(1);
        this.maxLevel=maxLevel;
        this.setCooldown(cooldown);
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

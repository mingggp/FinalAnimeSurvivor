package entity.accessory;

import entity.Entity;
import javafx.scene.image.Image;

public abstract class Accessory extends Entity  {
    private Image Icon;
    protected int level;
    private int maxLevel;


    public Accessory(String name,int maxLevel){
        this.setName(name);
        this.level = 1;
        this.maxLevel = maxLevel;
    }

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
    public void upgrade(){
        this.level = level +1;
        if (level>maxLevel)level=maxLevel;
    }
    public abstract void procEffect();
}

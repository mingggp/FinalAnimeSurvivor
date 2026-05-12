package entity.item;

import entity.Entity;
import entityInterface.GameObject;
import javafx.scene.image.Image;

public abstract class Item extends Entity implements GameObject {

    private int amount = 0;
    private Image Icon;
    protected boolean taggedByMagnet;

    public Item(String name) {
        this.setName(name);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Image getIcon() {
        return Icon;
    }

    public void setIcon(Image icon) {
        Icon = icon;
    }

    public void tag(){
        taggedByMagnet = true;
    }
}

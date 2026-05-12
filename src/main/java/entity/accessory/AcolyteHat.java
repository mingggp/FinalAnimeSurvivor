package entity.accessory;

import core.GameManager;
import entityInterface.GameObject;
import javafx.scene.image.Image;

public class AcolyteHat extends Accessory{
    public AcolyteHat(GameManager gameManager){
        super("acolytehat",8);
        this.setIcon(new Image("accessory/icon/acolytehat.png"));
    }

    @Override
    public GameObject copy() {
        return null;
    }

    @Override
    public void procEffect() {

    }
}

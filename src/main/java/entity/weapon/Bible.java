package entity.weapon;

import core.GameManager;
import entityInterface.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bible extends Weapon{

    public Bible(GameManager gameManager){
        super("bible",8,1);
        this.setIcon(new Image("weapon/icon/bible.png"));
    }

    @Override
    public void use(double accumulateDeltaTime) {

    }

    @Override
    public void update(double accumulateDeltaTime) {

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
    }

    @Override
    public GameObject copy() {
        return null;
    }
}

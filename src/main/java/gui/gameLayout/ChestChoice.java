package gui.gameLayout;

import core.GameManager;
import core.GameState;
import entity.accessory.Accessory;
import entity.item.Item;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import utils.SceneManager;

public class ChestChoice extends VBox {

    private final Text title;
    private final GameManager gameManager;
    private final ImageView image;

    public ChestChoice(GameManager gameManager){
        this.gameManager = gameManager;
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        this.setMaxSize(500,750);
        BackgroundFill bgFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};
        this.setBackground(new Background(bgFillA));
        this.setBorder(new Border(new BorderStroke( Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,new BorderWidths(5))));
        this.setVisible(false);
        this.title = new Text();
        this.title.setText( "Chest Found!");
        this.title.setStyle("-fx-font-size :70px;");
        image = new ImageView();
        this.getChildren().add(title);
        this.getChildren().add(image);
        this.setOnMouseClicked(e -> onClickHandler());
    }
    private void onClickHandler(){
        SceneManager.hideChestChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
    public void update(GameObject gameObject){
        if(gameObject instanceof Weapon weapon){
            image.setImage(weapon.getIcon());
        }
        else if(gameObject instanceof Item item){
            image.setImage(item.getIcon());
        }
        else if(gameObject instanceof Accessory accessory){
            image.setImage(accessory.getIcon());
        }
    }
}

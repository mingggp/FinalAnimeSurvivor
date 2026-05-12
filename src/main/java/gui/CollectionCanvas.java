package gui;

import core.GameManager;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class CollectionCanvas extends GridPane {

    private Text collectionTitleTemp;
    private GameManager gameManager;

    public CollectionCanvas(GameManager gameManager){
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);
        this.initializeCollectionTitleTemp();
        this.getChildren().add(collectionTitleTemp);
    }
    private void initializeCollectionTitleTemp() {
        this.collectionTitleTemp = new Text();
        this.collectionTitleTemp.setText("Collection");
        this.collectionTitleTemp.setStyle("-fx-font-size :70px;");
    }

}

package gui;

import core.GameManager;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UpgradeMenuCanvas extends VBox {

    private Text upgradeMenuTitleTemp;
    private GameManager gameManager;

    public UpgradeMenuCanvas(GameManager gameManager){
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);
        this.initializeUpgradeMenuTitleTempText();
        this.getChildren().add(upgradeMenuTitleTemp);

    }
    private void initializeUpgradeMenuTitleTempText(){
        this.upgradeMenuTitleTemp = new Text();
        this.upgradeMenuTitleTemp.setText( "Upgrade");
        this.upgradeMenuTitleTemp.setStyle("-fx-font-size :70px;");
    }
}

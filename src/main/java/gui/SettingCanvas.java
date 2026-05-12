package gui;

import core.GameManager;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingCanvas extends VBox {

    private Text settingTitleTemp;
    private GameManager gameManager;

    public SettingCanvas(GameManager gameManager){
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);
        this.initializeSettingTitleTempText();
        this.getChildren().add(settingTitleTemp);
    }
    private void initializeSettingTitleTempText() {
        this.settingTitleTemp = new Text();
        this.settingTitleTemp.setText("Setting");
        this.settingTitleTemp.setStyle("-fx-font-size :70px;");
    }

}

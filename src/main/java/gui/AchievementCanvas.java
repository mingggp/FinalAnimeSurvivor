package gui;

import core.GameManager;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AchievementCanvas extends VBox {

    private Text achievementTitleTemp;
    private GameManager gameManager;

    public AchievementCanvas(GameManager gameManager){
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);
        this.initializeAchievementTitleTemp();
        this.getChildren().add(achievementTitleTemp);
    }
    private void initializeAchievementTitleTemp() {
        this.achievementTitleTemp = new Text();
        this.achievementTitleTemp.setText("Achievement");
        this.achievementTitleTemp.setStyle("-fx-font-size :70px;");
    }

}

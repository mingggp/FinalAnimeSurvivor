package gui;

import core.GameManager;
import core.GameState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.SceneManager;

public class MenuCanvas extends VBox {

    private Text gameTitleTemp;
    private Button playButton;
    private Button upgradeButton;
    private Button settingButton;
    private Button achievementButton;
    private Button collectionButton;
    private Button quitButton;
    private GameManager gameManager;

    public MenuCanvas(GameManager gameManager){
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(300);
        this.setSpacing(20);
        this.initializeGameText();
        this.initializePlayButton();
        this.initializeUpgradeButton();
        this.initializeSettingButton();
        this.initializeAchievementButton();
        this.initializeCollectionButton();
        this.initializeQuitButton();
        this.getChildren().add(gameTitleTemp);
        this.getChildren().add(playButton);
        //this.getChildren().add(upgradeButton);
        this.getChildren().add(settingButton);
        //this.getChildren().add(achievementButton);
        //this.getChildren().add(collectionButton);
        this.getChildren().add(quitButton);
    }

    private void initializeGameText(){
        this.gameTitleTemp = new Text();
        this.gameTitleTemp.setText( "ANIME SURVIVOR");
        this.gameTitleTemp.setStyle("-fx-font-size :70px;");
    }
    private void initializePlayButton(){
        this.playButton = new Button();
        this.playButton.setText("Play");
        this.playButton.setPrefWidth(300);
        this.playButton.setPrefHeight(75);
        this.playButton.setOnMouseClicked( event -> playButtonHandler());
    }
    private void initializeUpgradeButton(){
        this.upgradeButton = new Button();
        this.upgradeButton.setText("Upgrade");
        this.upgradeButton.setPrefWidth(300);
        this.upgradeButton.setPrefHeight(75);
        this.upgradeButton.setOnMouseClicked( event -> upgradeButtonHandler());
    }
    private void initializeSettingButton(){
        this.settingButton = new Button();
        this.settingButton.setText("Setting");
        this.settingButton.setPrefWidth(300);
        this.settingButton.setPrefHeight(75);
        this.settingButton.setOnMouseClicked( event -> settingButtonHandler());
    }
    private void initializeAchievementButton(){
        this.achievementButton = new Button();
        this.achievementButton.setText("Achievement");
        this.achievementButton.setPrefWidth(300);
        this.achievementButton.setPrefHeight(75);
        this.achievementButton.setOnMouseClicked( event -> achievementButtonHandler());
    }
    private void initializeCollectionButton(){
        this.collectionButton = new Button();
        this.collectionButton.setText("Collection");
        this.collectionButton.setPrefWidth(300);
        this.collectionButton.setPrefHeight(75);
        this.collectionButton.setOnMouseClicked( event -> collectionButtonHandler());
    }
    private void initializeQuitButton(){
        this.quitButton = new Button();
        this.quitButton.setText("Quit");
        this.quitButton.setPrefWidth(300);
        this.quitButton.setPrefHeight(75);
        this.quitButton.setOnMouseClicked( event -> quitButtonHandler());
    }
    private void playButtonHandler(){
        SceneManager.switchToCharacterMenu();
        gameManager.setCurrentState(GameState.CHARACTER_MENU);
    }
    private void upgradeButtonHandler(){
        SceneManager.switchToUpgrade();
        gameManager.setCurrentState(GameState.UPGRADE_MENU);
    }
    private void settingButtonHandler(){
        SceneManager.switchToSetting();
        gameManager.setCurrentState(GameState.SETTINGS);
    }
    private void achievementButtonHandler(){
        SceneManager.switchToAchievement();
        gameManager.setCurrentState(GameState.ACHIEVEMENT);
    }
    private void collectionButtonHandler(){
        SceneManager.switchToCollection();
        gameManager.setCurrentState(GameState.COLLECTION);
    }
    private void quitButtonHandler(){
        System.exit(0);
    }
}

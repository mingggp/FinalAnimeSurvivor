package gui.gameLayout;

import core.GameManager;
import core.GameState;
import entity.accessory.Accessory;
import entity.weapon.Weapon;
import entityInterface.GameObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import utils.SceneManager;

public class LevelUpChoice extends VBox {
    private final Text title;
    private final Button choice0 = new Button();
    private final Button choice1 = new Button();
    private final Button choice2 = new Button();
    private final GameManager gameManager;
    private final ImageView choice0Image = new ImageView();
    private final ImageView choice1Image = new ImageView();
    private final ImageView choice2Image = new ImageView();

    public LevelUpChoice(GameManager gameManager){
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
        this.title.setText( "Level Up!");
        this.title.setStyle("-fx-font-size :70px;");

        this.choice0.setPrefWidth(478);
        this.choice0.setPrefHeight(150);
        choice0.setAlignment(Pos.TOP_LEFT);
        this.choice1.setPrefWidth(478);
        this.choice1.setPrefHeight(150);
        choice1.setAlignment(Pos.TOP_LEFT);
        this.choice2.setPrefWidth(478);
        this.choice2.setPrefHeight(150);
        choice2.setAlignment(Pos.TOP_LEFT);


        this.getChildren().add(title);
        choice0.setStyle("-fx-focus-color: black; -fx-faint-focus-color: transparent; -fx-background-insets: -6, -1, 0, 0;");
        choice1.setStyle("-fx-focus-color: black; -fx-faint-focus-color: transparent; -fx-background-insets: -6, -1, 0, 0;");
        choice2.setStyle("-fx-focus-color: black; -fx-faint-focus-color: transparent; -fx-background-insets: -6, -1, 0, 0;");
        this.choice0.setOnMouseClicked(e -> choice0Handler());
        this.choice0.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE) choice0Handler();
            if(e.getCode() == KeyCode.W)  choice2.requestFocus();
            if(e.getCode() == KeyCode.S)  choice1.requestFocus();
        });
        this.getChildren().add(choice0);
        this.choice1.setOnMouseClicked(e -> choice1Handler());
        this.choice1.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE) choice1Handler();
            if(e.getCode() == KeyCode.W)  choice0.requestFocus();
            if(e.getCode() == KeyCode.S)  choice2.requestFocus();
        });
        this.getChildren().add(choice1);
        this.choice2.setOnMouseClicked(e -> choice2Handler());
        this.choice2.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE) choice2Handler();
            if(e.getCode() == KeyCode.W)  choice1.requestFocus();
            if(e.getCode() == KeyCode.S)  choice0.requestFocus();
        });
        this.getChildren().add(choice2);

    }

    private void setupChoice0(){
        GameObject objectForChoice0 = gameManager.getLevelUpChoice().get("choice0");
        if(objectForChoice0 == null){
            choice0.setVisible(false);
        }
        else {
            choice0.setVisible(true);
            if (objectForChoice0 instanceof Weapon weapon) {
                choice0Image.setImage(weapon.getIcon());
                this.choice0.setText(("                   " + weapon.getName() + "                   " + ( gameManager.getLevelUpChoiceType().get("choice0Type").equals("upgrade") ? "level:"+(Integer.parseInt(weapon.getLevel())+1) : "New!")));
            } else if (objectForChoice0 instanceof Accessory accessory) {
                choice0Image.setImage(accessory.getIcon());
                this.choice0.setText(("                   " + accessory.getName() + "                   " + ( gameManager.getLevelUpChoiceType().get("choice0Type").equals("upgrade") ? "level:"+(Integer.parseInt(accessory.getLevel())+1) : "New!")));
            }
            choice0.setGraphic(choice0Image);
        }
    }
    private void setupChoice1(){
        GameObject objectForChoice1 = gameManager.getLevelUpChoice().get("choice1");
        if(objectForChoice1 == null){
            choice1.setVisible(false);
        }
        else {
            choice1.setVisible(true);
            if (objectForChoice1 instanceof Weapon weapon) {
                choice1Image.setImage(weapon.getIcon());
                this.choice1.setText(("                   " + weapon.getName() + "                   " + ( gameManager.getLevelUpChoiceType().get("choice1Type").equals("upgrade") ? "level:"+(Integer.parseInt(weapon.getLevel())+1) : "New!")));
            } else if (objectForChoice1 instanceof Accessory accessory) {
                choice1Image.setImage(accessory.getIcon());
                this.choice1.setText(("                   " + accessory.getName() + "                   " + ( gameManager.getLevelUpChoiceType().get("choice1Type").equals("upgrade") ? "level:"+(Integer.parseInt(accessory.getLevel())+1) : "New!")));
            }
            choice1.setGraphic(choice1Image);
        }
    }
    private void setupChoice2(){
        GameObject objectForChoice2 = gameManager.getLevelUpChoice().get("choice2");
        if(objectForChoice2 == null){
            choice2.setVisible(false);
        }
        else {
            choice2.setVisible(true);
            if (objectForChoice2 instanceof Weapon weapon) {
                choice2Image.setImage(weapon.getIcon());
                this.choice2.setText(("                   " + weapon.getName() + "                   " + ( gameManager.getLevelUpChoiceType().get("choice2Type").equals("upgrade") ? "level:"+(Integer.parseInt(weapon.getLevel())+1 ): "New!")));
            } else if (objectForChoice2 instanceof Accessory accessory) {
                choice2Image.setImage(accessory.getIcon());
                this.choice2.setText(("                   " + accessory.getName() + "                   " + ( gameManager.getLevelUpChoiceType().get("choice2Type").equals("upgrade") ? "level:"+(Integer.parseInt(accessory.getLevel())+1) : "New!")));
            }
            choice2.setGraphic(choice2Image);
        }
    }

    public void update(){
        setupChoice0();
        setupChoice1();
        setupChoice2();
    }
    private void choice0Handler(){
        gameManager.putLevelUpChoice("choice0");
        gameManager.returnChoice("choice1");
        gameManager.returnChoice("choice2");
        SceneManager.hideLevelUpChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
    private void choice1Handler(){
        gameManager.putLevelUpChoice("choice1");
        gameManager.returnChoice("choice0");
        gameManager.returnChoice("choice2");
        SceneManager.hideLevelUpChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
    private void choice2Handler(){
        gameManager.putLevelUpChoice("choice2");
        gameManager.returnChoice("choice1");
        gameManager.returnChoice("choice0");
        SceneManager.hideLevelUpChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
}

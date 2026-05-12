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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SceneManager;
import utils.SoundManager;

public class LevelUpChoice extends VBox {

    private static final String CARD_NORMAL =
        "-fx-background-color: #1a1a2e;" +
        "-fx-border-color: #e94560; -fx-border-width: 2; -fx-border-radius: 10;" +
        "-fx-background-radius: 10; -fx-cursor: hand;";
    private static final String CARD_HOVER =
        "-fx-background-color: #e94560;" +
        "-fx-border-color: #ff8fab; -fx-border-width: 2; -fx-border-radius: 10;" +
        "-fx-background-radius: 10; -fx-cursor: hand;";

    private final Text title;
    private final Button choice0 = new Button();
    private final Button choice1 = new Button();
    private final Button choice2 = new Button();
    private final GameManager gameManager;
    private final ImageView icon0 = new ImageView();
    private final ImageView icon1 = new ImageView();
    private final ImageView icon2 = new ImageView();

    public LevelUpChoice(GameManager gameManager) {
        this.gameManager = gameManager;
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(16);
        this.setPadding(new Insets(24, 20, 24, 20));
        this.setMaxSize(560, 800);

        this.setBackground(new Background(new BackgroundFill(
            Color.web("#0d0d1a", 0.96), new CornerRadii(16), Insets.EMPTY)));
        this.setBorder(new Border(new BorderStroke(
            Color.web("#e94560"), BorderStrokeStyle.SOLID, new CornerRadii(16), new BorderWidths(3))));
        this.setVisible(false);

        title = new Text("⬆  LEVEL UP!");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 52));
        title.setFill(Color.web("#ffd700"));

        Text hint = new Text("Choose one upgrade");
        hint.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        hint.setFill(Color.web("#aaaacc"));

        styleCard(choice0, icon0);
        styleCard(choice1, icon1);
        styleCard(choice2, icon2);

        choice0.setOnMouseClicked(e -> { SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK); choice0Handler(); });
        choice0.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) choice0Handler();
            if (e.getCode() == KeyCode.W) choice2.requestFocus();
            if (e.getCode() == KeyCode.S) choice1.requestFocus();
        });
        choice1.setOnMouseClicked(e -> { SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK); choice1Handler(); });
        choice1.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) choice1Handler();
            if (e.getCode() == KeyCode.W) choice0.requestFocus();
            if (e.getCode() == KeyCode.S) choice2.requestFocus();
        });
        choice2.setOnMouseClicked(e -> { SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK); choice2Handler(); });
        choice2.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) choice2Handler();
            if (e.getCode() == KeyCode.W) choice1.requestFocus();
            if (e.getCode() == KeyCode.S) choice0.requestFocus();
        });

        this.getChildren().addAll(title, hint, choice0, choice1, choice2);
    }

    private void styleCard(Button btn, ImageView icon) {
        icon.setFitWidth(72);
        icon.setFitHeight(72);
        icon.setPreserveRatio(true);
        btn.setGraphic(icon);
        btn.setGraphicTextGap(20);
        btn.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        btn.setPrefWidth(516);
        btn.setPrefHeight(110);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(CARD_NORMAL);
        btn.setOnMouseEntered(e -> btn.setStyle(CARD_HOVER));
        btn.setOnMouseExited(e -> btn.setStyle(CARD_NORMAL));
    }

    private void applyChoice(Button btn, ImageView icon, String key) {
        GameObject obj = gameManager.getLevelUpChoice().get(key);
        if (obj == null) { btn.setVisible(false); return; }
        btn.setVisible(true);
        String typeKey = key + "Type";
        boolean isUpgrade = "upgrade".equals(gameManager.getLevelUpChoiceType().get(typeKey));
        String name, levelStr;
        if (obj instanceof Weapon w) {
            icon.setImage(w.getIcon());
            name = w.getName();
            levelStr = isUpgrade ? "Level → " + (Integer.parseInt(w.getLevel()) + 1) : "✨ New!";
        } else if (obj instanceof Accessory a) {
            icon.setImage(a.getIcon());
            name = a.getName();
            levelStr = isUpgrade ? "Level → " + (Integer.parseInt(a.getLevel()) + 1) : "✨ New!";
        } else { return; }

        btn.setText("  " + name + "\n  " + levelStr);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    }

    public void update() {
        applyChoice(choice0, icon0, "choice0");
        applyChoice(choice1, icon1, "choice1");
        applyChoice(choice2, icon2, "choice2");
    }

    private void choice0Handler() {
        gameManager.putLevelUpChoice("choice0");
        gameManager.returnChoice("choice1");
        gameManager.returnChoice("choice2");
        SceneManager.hideLevelUpChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
    private void choice1Handler() {
        gameManager.putLevelUpChoice("choice1");
        gameManager.returnChoice("choice0");
        gameManager.returnChoice("choice2");
        SceneManager.hideLevelUpChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
    private void choice2Handler() {
        gameManager.putLevelUpChoice("choice2");
        gameManager.returnChoice("choice1");
        gameManager.returnChoice("choice0");
        SceneManager.hideLevelUpChoice();
        gameManager.setCurrentState(GameState.PLAYING);
    }
}

package gui;

import core.GameManager;
import core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SceneManager;

public class MenuCanvas extends VBox {

    private static final String BTN_NORMAL =
        "-fx-background-color: #2c2c54;" +
        "-fx-text-fill: #f5f5f5;" +
        "-fx-font-size: 26px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #e94560;" +
        "-fx-border-width: 2;" +
        "-fx-border-radius: 10;" +
        "-fx-cursor: hand;";

    private static final String BTN_HOVER =
        "-fx-background-color: #e94560;" +
        "-fx-text-fill: #ffffff;" +
        "-fx-font-size: 26px;" +
        "-fx-font-weight: bold;" +
        "-fx-background-radius: 10;" +
        "-fx-border-color: #e94560;" +
        "-fx-border-width: 2;" +
        "-fx-border-radius: 10;" +
        "-fx-cursor: hand;";

    private final GameManager gameManager;

    public MenuCanvas(GameManager gameManager) {
        this.gameManager = gameManager;

        LinearGradient bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#0f0c29")),
            new Stop(0.5, Color.web("#302b63")),
            new Stop(1, Color.web("#24243e")));
        this.setBackground(new Background(new BackgroundFill(bg, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        Text title = new Text("ANIME SURVIVOR");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80));
        title.setFill(Color.web("#e94560"));
        DropShadow glow = new DropShadow(20, Color.web("#e94560"));
        title.setEffect(glow);

        Text subtitle = new Text("— Top-Down Survivor —");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 22));
        subtitle.setFill(Color.web("#aaaacc"));

        VBox titleBox = new VBox(6, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 30, 0));

        Button playBtn    = makeButton("▶  Play");
        Button settingBtn = makeButton("⚙  Settings");
        Button quitBtn    = makeButton("✕  Quit");

        playBtn.setOnMouseClicked(e -> {
            SceneManager.switchToCharacterMenu();
            gameManager.setCurrentState(GameState.CHARACTER_MENU);
        });
        settingBtn.setOnMouseClicked(e -> {
            SceneManager.switchToSetting();
            gameManager.setCurrentState(GameState.SETTINGS);
        });
        quitBtn.setOnMouseClicked(e -> System.exit(0));

        this.getChildren().addAll(titleBox, playBtn, settingBtn, quitBtn);
    }

    private Button makeButton(String label) {
        Button b = new Button(label);
        b.setPrefWidth(340);
        b.setPrefHeight(70);
        b.setStyle(BTN_NORMAL);
        b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
        b.setOnMouseExited(e -> b.setStyle(BTN_NORMAL));
        return b;
    }
}

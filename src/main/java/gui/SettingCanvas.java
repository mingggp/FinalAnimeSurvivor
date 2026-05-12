package gui;

import core.GameManager;
import core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SceneManager;
import utils.SoundManager;

public class SettingCanvas extends VBox {

    private static final String BTN_NORMAL =
        "-fx-background-color: #2c2c54; -fx-text-fill: #f5f5f5; -fx-font-size: 22px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";
    private static final String BTN_HOVER =
        "-fx-background-color: #e94560; -fx-text-fill: #ffffff; -fx-font-size: 22px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";

    private final GameManager gameManager;
    private final SettingsSliderPanel sliderPanel;

    public SettingCanvas(GameManager gameManager) {
        this.gameManager = gameManager;

        LinearGradient bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#0f0c29")),
            new Stop(0.5, Color.web("#302b63")),
            new Stop(1, Color.web("#24243e")));
        this.setBackground(new Background(new BackgroundFill(bg, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(40);

        Text title = new Text("⚙  Settings");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 64));
        title.setFill(Color.web("#e94560"));
        title.setEffect(new DropShadow(18, Color.web("#e94560")));

        sliderPanel = new SettingsSliderPanel();

        Button backButton = makeButton("← Back");
        backButton.setOnMouseClicked(e -> {
            gameManager.setCurrentState(GameState.MAIN_MENU);
            SceneManager.switchToMenu();
        });

        this.getChildren().addAll(title, sliderPanel, backButton);

        // Refresh sliders from SoundManager each time this screen becomes the scene root
        sceneProperty().addListener((obs, o, n) -> {
            if (n != null) sliderPanel.refresh();
        });
    }

    /** Returns a fresh SettingsSliderPanel; used by InGameSettingPanel. */
    public static SettingsSliderPanel buildPanel() {
        return new SettingsSliderPanel();
    }

    static Button makeButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(300);
        b.setPrefHeight(60);
        b.setStyle(BTN_NORMAL);
        b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
        b.setOnMouseExited(e -> b.setStyle(BTN_NORMAL));
        b.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
            e -> SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK));
        return b;
    }

    static String pct(double v) { return (int) Math.round(v * 100) + "%"; }
}

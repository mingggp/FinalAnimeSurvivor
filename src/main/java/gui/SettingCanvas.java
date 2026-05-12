package gui;

import core.GameManager;
import core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

        VBox panel = buildPanel();

        Button backButton = makeButton("← Back");
        backButton.setOnMouseClicked(e -> {
            gameManager.setCurrentState(GameState.MAIN_MENU);
            SceneManager.switchToMenu();
        });

        this.getChildren().addAll(title, panel, backButton);
    }

    public static VBox buildPanel() {
        VBox panel = new VBox(24);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setPadding(new Insets(36, 48, 36, 48));
        panel.setMaxWidth(560);
        panel.setStyle(
            "-fx-background-color: rgba(13,13,26,0.95); -fx-background-radius: 16;" +
            "-fx-border-color: #e94560; -fx-border-width: 2; -fx-border-radius: 16;");

        panel.getChildren().addAll(
            buildVolumeRow("♪  BGM Volume",
                SoundManager.getInstance().getBgmVolume(),
                v -> SoundManager.getInstance().setBgmVolume(v)),
            buildVolumeRow("✦  SFX Volume",
                SoundManager.getInstance().getSfxVolume(),
                v -> SoundManager.getInstance().setSfxVolume(v))
        );
        return panel;
    }

    private static VBox buildVolumeRow(String labelText, double initialValue,
                                       java.util.function.DoubleConsumer onChange) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        label.setTextFill(Color.web("#f5f5f5"));

        Slider slider = new Slider(0, 1, initialValue);
        slider.setPrefWidth(380);
        // No tick labels — they show confusing decimals; our own label covers it
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.25);
        slider.setBlockIncrement(0.05);
        slider.setStyle("-fx-control-inner-background: #2c2c54; -fx-accent: #e94560;");

        Label valueLabel = new Label(pct(initialValue));
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        valueLabel.setTextFill(Color.web("#ffd700"));
        valueLabel.setMinWidth(55);

        slider.valueProperty().addListener((obs, o, n) -> {
            onChange.accept(n.doubleValue());
            valueLabel.setText(pct(n.doubleValue()));
        });

        HBox row = new HBox(16, slider, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(8, label, row);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
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

package gui;

import core.GameManager;
import core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.SceneManager;
import utils.SoundManager;

/**
 * Settings screen — shown when the player clicks "Setting" from the main menu
 * or presses ESC from the settings state.
 *
 * <p>Controls provided:
 * <ul>
 *   <li>Master volume slider (applies to BGM and SFX)</li>
 *   <li>Back button (returns to main menu)</li>
 * </ul>
 */
public class SettingCanvas extends VBox {

    private final GameManager gameManager;

    public SettingCanvas(GameManager gameManager) {
        this.gameManager = gameManager;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setPadding(new Insets(40));

        Text title = new Text("Settings");
        title.setStyle("-fx-font-size: 70px; -fx-font-weight: bold;");

        VBox volumeRow = buildVolumeControl();

        Button backButton = new Button("Back");
        backButton.setPrefWidth(300);
        backButton.setPrefHeight(60);
        backButton.setStyle("-fx-font-size: 22px;");
        backButton.setOnMouseClicked(e -> {
            gameManager.setCurrentState(GameState.MAIN_MENU);
            SceneManager.switchToMenu();
        });

        this.getChildren().addAll(title, volumeRow, backButton);
    }

    private VBox buildVolumeControl() {
        Label label = new Label("Master Volume");
        label.setStyle("-fx-font-size: 24px;");

        Slider slider = new Slider(0, 1, SoundManager.getInstance().getVolume());
        slider.setPrefWidth(400);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.25);
        slider.setBlockIncrement(0.05);

        Label valueLabel = new Label(formatPercent(slider.getValue()));
        valueLabel.setStyle("-fx-font-size: 20px;");
        valueLabel.setPrefWidth(60);

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double v = newVal.doubleValue();
            SoundManager.getInstance().setVolume(v);
            valueLabel.setText(formatPercent(v));
        });

        HBox row = new HBox(16, slider, valueLabel);
        row.setAlignment(Pos.CENTER);

        VBox box = new VBox(8, label, row);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private String formatPercent(double v) {
        return (int) Math.round(v * 100) + "%";
    }
}

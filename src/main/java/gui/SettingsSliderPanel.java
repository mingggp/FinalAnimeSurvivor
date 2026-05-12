package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.SoundManager;

/** Volume-slider panel shared by the main-menu Settings screen and the in-game Settings overlay. */
public class SettingsSliderPanel extends VBox {

    private final Slider bgmSlider;
    private final Slider sfxSlider;

    public SettingsSliderPanel() {
        super(24);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(36, 48, 36, 48));
        setMaxWidth(560);
        setStyle(
            "-fx-background-color: rgba(13,13,26,0.95); -fx-background-radius: 16;" +
            "-fx-border-color: #e94560; -fx-border-width: 2; -fx-border-radius: 16;");

        bgmSlider = makeSlider();
        sfxSlider = makeSlider();

        getChildren().addAll(
            buildRow("♪  BGM Volume", bgmSlider, v -> SoundManager.getInstance().setBgmVolume(v)),
            buildRow("✦  SFX Volume", sfxSlider, v -> SoundManager.getInstance().setSfxVolume(v))
        );

        refresh();
    }

    /** Sync slider positions to the current SoundManager volumes. */
    public void refresh() {
        bgmSlider.setValue(SoundManager.getInstance().getBgmVolume());
        sfxSlider.setValue(SoundManager.getInstance().getSfxVolume());
    }

    private static Slider makeSlider() {
        Slider s = new Slider(0, 1, 0.5);
        s.setPrefWidth(380);
        s.setShowTickLabels(false);
        s.setShowTickMarks(true);
        s.setMajorTickUnit(0.25);
        s.setBlockIncrement(0.05);
        s.setStyle("-fx-control-inner-background: #2c2c54; -fx-accent: #e94560;");
        return s;
    }

    private static VBox buildRow(String labelText, Slider slider,
                                  java.util.function.DoubleConsumer onChange) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        label.setTextFill(Color.web("#f5f5f5"));

        Label valueLabel = new Label(pct(slider.getValue()));
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

    static String pct(double v) { return (int) Math.round(v * 100) + "%"; }
}

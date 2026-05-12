package gui.gameLayout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SoundManager;

/** In-game settings overlay (accessible from the pause screen). */
public class InGameSettingPanel extends VBox {

    private static final String BTN_NORMAL =
        "-fx-background-color: #2c2c54; -fx-text-fill: #f5f5f5; -fx-font-size: 20px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";
    private static final String BTN_HOVER =
        "-fx-background-color: #e94560; -fx-text-fill: #fff; -fx-font-size: 20px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";

    public InGameSettingPanel(Runnable onBack) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(40, 50, 40, 50));
        this.setMaxSize(500, 420);
        this.setStyle(
            "-fx-background-color: rgba(13,13,26,0.97);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: #e94560; -fx-border-width: 3; -fx-border-radius: 18;");

        Text title = new Text("⚙  Settings");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 44));
        title.setFill(Color.web("#e94560"));
        title.setEffect(new DropShadow(14, Color.web("#e94560")));

        VBox sliders = gui.SettingCanvas.buildPanel();

        Button backBtn = makeBtn("← Back");
        backBtn.setOnMouseClicked(e -> onBack.run());

        this.getChildren().addAll(title, sliders, backBtn);
        this.setVisible(false);
    }

    private Button makeBtn(String label) {
        Button b = new Button(label);
        b.setPrefWidth(260); b.setPrefHeight(54);
        b.setStyle(BTN_NORMAL);
        b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
        b.setOnMouseExited(e -> b.setStyle(BTN_NORMAL));
        b.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
            e -> SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK));
        return b;
    }
}

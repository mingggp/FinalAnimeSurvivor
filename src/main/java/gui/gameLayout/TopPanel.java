package gui.gameLayout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.Duration;

/** HUD bar pinned to the top of the screen showing elapsed time and player level. */
public class TopPanel extends Pane {

    private final Text clock;
    private final Text levelText;

    public TopPanel() {
        // ── Timer pill (centre-top) ──────────────────────────────────────────
        clock = new Text("00:00");
        clock.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 36));
        clock.setFill(Color.web("#ffd700"));
        clock.setEffect(new DropShadow(8, Color.web("#00000088")));

        HBox timerBox = new HBox(clock);
        timerBox.setAlignment(Pos.CENTER);
        timerBox.setPadding(new Insets(6, 24, 6, 24));
        timerBox.setStyle(
            "-fx-background-color: rgba(13,13,26,0.82);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: #e94560; -fx-border-width: 2; -fx-border-radius: 20;");
        timerBox.setPrefWidth(160);
        timerBox.setLayoutX(880);   // ~centre of 1920
        timerBox.setLayoutY(12);

        // ── Level badge (top-right) ──────────────────────────────────────────
        levelText = new Text("LV 1");
        levelText.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 30));
        levelText.setFill(Color.web("#f5f5f5"));
        levelText.setEffect(new DropShadow(8, Color.web("#00000088")));

        HBox levelBox = new HBox(levelText);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setPadding(new Insets(6, 20, 6, 20));
        levelBox.setStyle(
            "-fx-background-color: rgba(44,44,84,0.90);" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #aaaacc; -fx-border-width: 1.5; -fx-border-radius: 16;");
        levelBox.setLayoutX(1800);
        levelBox.setLayoutY(12);

        this.getChildren().addAll(timerBox, levelBox);
    }

    public void update(double gameTimer, int level) {
        Duration d = Duration.ofSeconds((long) gameTimer);
        clock.setText(String.format("%02d:%02d", d.toMinutesPart(), d.toSecondsPart()));
        levelText.setText("LV " + level);
    }
}

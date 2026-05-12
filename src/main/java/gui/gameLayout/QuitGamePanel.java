package gui.gameLayout;

import core.GameManager;
import core.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SceneManager;
import utils.SoundManager;

/**
 * Dual-mode overlay panel:
 * <ul>
 *   <li><b>Pause mode</b> — "PAUSED" title, Resume / Settings / Main Menu</li>
 *   <li><b>Death mode</b> — two buttons at the bottom so they don't cover
 *       the result image drawn on the canvas underneath</li>
 * </ul>
 */
public class QuitGamePanel extends VBox {

    private static final String BTN_NORMAL =
        "-fx-background-color: #2c2c54; -fx-text-fill: #f5f5f5; -fx-font-size: 22px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";
    private static final String BTN_HOVER =
        "-fx-background-color: #e94560; -fx-text-fill: #ffffff; -fx-font-size: 22px;" +
        "-fx-font-weight: bold; -fx-background-radius: 10; -fx-border-color: #e94560;" +
        "-fx-border-width: 2; -fx-border-radius: 10; -fx-cursor: hand;";

    private final VBox pauseContent;
    private final VBox deathContent;
    private InGameSettingPanel settingPanel;

    public QuitGamePanel(GameManager gameManager) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(0);
        this.setMaxSize(460, 500);
        this.setStyle(
            "-fx-background-color: rgba(13,13,26,0.93);" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: #e94560; -fx-border-width: 3; -fx-border-radius: 18;");
        this.setVisible(false);

        // ── Pause content ────────────────────────────────────────────────────
        Text pauseTitle = new Text("⏸  PAUSED");
        pauseTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 52));
        pauseTitle.setFill(Color.web("#ffd700"));
        pauseTitle.setEffect(new DropShadow(14, Color.web("#ffd700")));

        Text pauseHint = new Text("Press ESC to resume");
        pauseHint.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        pauseHint.setFill(Color.web("#aaaacc"));

        Button resumeBtn = makeButton("▶  Resume");
        resumeBtn.setOnMouseClicked(e -> {
            gameManager.setCurrentState(GameState.PLAYING);
            SceneManager.unpause();
        });

        Button settingsBtn = makeButton("⚙  Settings");
        settingsBtn.setOnMouseClicked(e -> {
            this.setVisible(false);
            if (settingPanel != null) settingPanel.setVisible(true);
        });

        Button menuBtn = makeButton("⏹  Main Menu");
        menuBtn.setOnMouseClicked(e -> gameManager.resetGame());

        pauseContent = new VBox(16, pauseTitle, pauseHint, resumeBtn, settingsBtn, menuBtn);
        pauseContent.setAlignment(Pos.CENTER);
        pauseContent.setPadding(new Insets(36, 40, 36, 40));

        // ── Death content ────────────────────────────────────────────────────
        Button restartBtn = makeButton("↺  Restart");
        restartBtn.setPrefWidth(260);
        restartBtn.setOnMouseClicked(e -> {
            SceneManager.switchToCharacterMenu();
            gameManager.setCurrentState(GameState.CHARACTER_MENU);
        });

        Button homeBtn = makeButton("⏹  Main Menu");
        homeBtn.setPrefWidth(260);
        homeBtn.setOnMouseClicked(e -> gameManager.resetGame());

        HBox deathBtns = new HBox(20, restartBtn, homeBtn);
        deathBtns.setAlignment(Pos.CENTER);

        deathContent = new VBox(deathBtns);
        deathContent.setAlignment(Pos.CENTER);
        deathContent.setPadding(new Insets(20, 30, 20, 30));

        this.getChildren().add(pauseContent);
    }

    /** Call before setVisible(true) to select which mode to display. */
    public void setDeathMode(boolean death) {
        this.getChildren().clear();
        if (death) {
            // Transparent background — buttons float over the result image
            this.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            this.setTranslateY(360);   // push buttons to bottom of screen
            this.setMaxSize(700, 120);
            this.getChildren().add(deathContent);
        } else {
            this.setStyle(
                "-fx-background-color: rgba(13,13,26,0.93);" +
                "-fx-background-radius: 18;" +
                "-fx-border-color: #e94560; -fx-border-width: 3; -fx-border-radius: 18;");
            this.setTranslateY(0);
            this.setMaxSize(460, 500);
            this.getChildren().add(pauseContent);
        }
    }

    /** Inject the in-game settings panel so the Settings button can show it. */
    public void setSettingPanel(InGameSettingPanel panel) {
        this.settingPanel = panel;
    }

    private Button makeButton(String label) {
        Button b = new Button(label);
        b.setPrefWidth(300);
        b.setPrefHeight(60);
        b.setStyle(BTN_NORMAL);
        b.setOnMouseEntered(e -> b.setStyle(BTN_HOVER));
        b.setOnMouseExited(e -> b.setStyle(BTN_NORMAL));
        b.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
            e -> SoundManager.getInstance().playSFX(SoundManager.SFX_UI_CLICK));
        return b;
    }
}

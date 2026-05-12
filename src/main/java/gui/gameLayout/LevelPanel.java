package gui.gameLayout;

import javafx.scene.control.ProgressBar;

/** XP progress bar pinned to the top edge of the screen. */
public class LevelPanel extends ProgressBar {

    public LevelPanel() {
        this.setPrefWidth(1920);
        this.setPrefHeight(22);
        this.setTranslateY(-529);   // sits just below the very top edge
        this.setStyle(
            "-fx-accent: #ffd700;" +                        // gold fill
            "-fx-control-inner-background: #1a1a2e;" +      // dark trough
            "-fx-background-color: #0d0d1a;" +
            "-fx-border-color: #302b63; -fx-border-width: 0 0 1 0;");
    }
}

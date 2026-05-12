package gui.gameLayout;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.time.Duration;

public class TopPanel extends Pane {

    private final Text clock;
    private final Text level;

    public TopPanel(){

        this.clock = new Text();
        this.clock.setText("TIME");
        this.clock.setStyle("-fx-font-size :35px;");
        this.clock.setLayoutX(918.5);//960-41.5
        this.clock.setLayoutY(75);

        this.level = new Text();
        this.level.setText("LV 1");
        this.level.setStyle("-fx-font-size :35px;");
        this.level.setLayoutX(1800);
        this.level.setLayoutY(32);




        this.getChildren().add(clock);
        this.getChildren().add(level);

    }
    public void update(double gameTimer,int level){
        Duration duration = Duration.ofSeconds((long)gameTimer);
        clock.setText(String.format("%02d:%02d",duration.toMinutesPart(), duration.toSecondsPart()));
        this.level.setText("LV " + level);
    }
}

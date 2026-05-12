package gui.gameLayout;

import entity.character.Character;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StatPanel extends VBox  {

    public StatPanel(){
        this.setMaxSize(340,750);
        this.setTranslateX(-780);
        this.setTranslateY(145);
        BackgroundFill bgFill = new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};
        this.setBackground(new Background(bgFillA));
        this.setBorder(new Border(new BorderStroke( Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,BorderWidths.DEFAULT)));
        this.setVisible(false);
    }
    public void update(Character character){

    }
}

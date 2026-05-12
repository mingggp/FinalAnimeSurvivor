package gui;

import core.GameManager;
import core.GameState;
import entity.character.Character;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.SceneManager;

public class CharacterMenuCanvas extends VBox {

    private static final String CARD_NORMAL =
        "-fx-background-color: #1a1a2e; -fx-border-color: #444477;" +
        "-fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";
    private static final String CARD_HOVER =
        "-fx-background-color: #2c2c54; -fx-border-color: #e94560;" +
        "-fx-border-width: 3; -fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand;";

    private final GameManager gameManager;

    public CharacterMenuCanvas(GameManager gameManager) {
        this.gameManager = gameManager;

        LinearGradient bg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#0f0c29")),
            new Stop(0.5, Color.web("#302b63")),
            new Stop(1, Color.web("#24243e")));
        this.setBackground(new Background(new BackgroundFill(bg, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(40);

        Text title = new Text("Select Character");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 64));
        title.setFill(Color.web("#e94560"));
        title.setEffect(new DropShadow(18, Color.web("#e94560")));

        Text hint = new Text("Click to start your run");
        hint.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        hint.setFill(Color.web("#aaaacc"));

        HBox cards = new HBox(40);
        cards.setAlignment(Pos.CENTER);

        Character[] list = gameManager.getAllCharacterList();
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null) continue;
            cards.getChildren().add(buildCard(list[i], i));
        }

        VBox header = new VBox(8, title, hint);
        header.setAlignment(Pos.CENTER);
        this.getChildren().addAll(header, cards);
    }

    private VBox buildCard(Character character, int slot) {
        ImageView icon = new ImageView(character.getIcon());
        icon.setFitWidth(160);
        icon.setFitHeight(160);
        icon.setPreserveRatio(true);

        String displayName = character.getName().substring(0, 1).toUpperCase()
            + character.getName().substring(1);
        Text nameText = new Text(displayName);
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        nameText.setFill(Color.web("#f5f5f5"));

        Text weaponText = new Text(character.getStarterWeapon().getName());
        weaponText.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        weaponText.setFill(Color.web("#aaaacc"));

        VBox card = new VBox(12, icon, nameText, weaponText);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(220, 280);
        card.setPadding(new Insets(20));
        card.setStyle(CARD_NORMAL);

        card.setOnMouseEntered(e -> {
            card.setStyle(CARD_HOVER);
            card.setEffect(new DropShadow(24, Color.web("#e94560")));
        });
        card.setOnMouseExited(e -> {
            card.setStyle(CARD_NORMAL);
            card.setEffect(null);
        });
        card.setOnMouseClicked(e -> {
            gameManager.setCharacter(new Character(character));
            SceneManager.switchToGame();
            gameManager.startGame();
            gameManager.setCurrentState(GameState.PLAYING);
        });
        return card;
    }
}

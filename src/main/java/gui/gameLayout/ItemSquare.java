package gui.gameLayout;

import core.GameManager;
import entity.accessory.Accessory;
import entity.weapon.Weapon;
import entity.item.Item;
import entityInterface.itemInterface.Unique;
import entityInterface.itemInterface.Usable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ItemSquare extends Pane {

    private static final double SQUARE_SIZE = 50;
    private boolean isDrawn;
    private Color baseColor;
    private int slot;
    private Item item;
    private final Label amountOrLevel = new Label();
    private final GameManager gameManager;
    /**
     * Translucent overlay drawn from the bottom up while the weapon is on
     * cooldown. Height shrinks toward 0 as the cooldown completes.
     */
    private final Rectangle cooldownOverlay = new Rectangle(SQUARE_SIZE, 0);
    private boolean cooldownVisible = false;

    //for weapon and accessory
    public ItemSquare(GameManager gameManager) {
        this.gameManager=gameManager;
        this.setPrefHeight(50);
        this.setPrefWidth(50);
        this.setMinHeight(50);
        this.setMinWidth(50);
        this.setBaseColor(Color.GRAY);
        this.initializeCellColor();
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        amountOrLevel.setTranslateX(3);
        amountOrLevel.setTranslateY(-2);
        amountOrLevel.setStyle("-fx-text-fill: white;  -fx-font-size :20px; -fx-effect: dropshadow(three-pass-box, black, 2, 1.0, 0, 0);" );
        cooldownOverlay.setFill(Color.rgb(0, 0, 0, 0.55));
        cooldownOverlay.setMouseTransparent(true);
        cooldownOverlay.setVisible(false);
        this.getChildren().add(cooldownOverlay);
        this.getChildren().add(amountOrLevel);

    }
    //for item
    public ItemSquare(int slot, GameManager gameManager) {
        this.gameManager = gameManager;
        this.slot = slot;
        this.setPrefHeight(50);
        this.setPrefWidth(50);
        this.setMinHeight(50);
        this.setMinWidth(50);
        this.setBaseColor(Color.GRAY);
        this.initializeCellColor();
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.setOnMouseClicked(event -> onClickHandler());
        amountOrLevel.setTranslateX(3);
        amountOrLevel.setTranslateY(-2);
        amountOrLevel.setStyle("-fx-text-fill: white;  -fx-font-size :20px; -fx-effect: dropshadow(three-pass-box, black, 2, 1.0, 0, 0);" );
        this.getChildren().add(amountOrLevel);
    }


    public void initializeCellColor() {
        this.item = null;
        amountOrLevel.setText("");
        cooldownVisible = false;
        cooldownOverlay.setVisible(false);
        cooldownOverlay.setHeight(0);
        //this.getChildren().clear();
        BackgroundFill bgFill = new BackgroundFill(this.getBaseColor(), CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};
        this.setBackground(new Background(bgFillA));
    }

    private void onClickHandler() {
        if(item instanceof Usable && !gameManager.getCharacter().isDead()) ((Usable) item).use(slot);
    }

    public void update(Item item){
        this.item = item;
        BackgroundFill bgFill = new BackgroundFill(this.getBaseColor(), CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};
        BackgroundSize bgSize = new BackgroundSize(50,50,false,false,false,false);
        BackgroundImage bgImg = new BackgroundImage(item.getIcon(), null, null, null, bgSize);
        BackgroundImage[] bgImgA = {bgImg};
        this.setBackground(new Background(bgFillA,bgImgA));
        amountOrLevel.setText( String.valueOf(item.getAmount()));
        if(item instanceof Unique)amountOrLevel.setText("");
    }
    public void updateWeaponSquare(Weapon weapon){
        BackgroundFill bgFill = new BackgroundFill(this.getBaseColor(), CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};
        BackgroundSize bgSize = new BackgroundSize(50,50,false,false,false,false);
        BackgroundImage bgImg = new BackgroundImage(weapon.getIcon(), null, null, null, bgSize);
        BackgroundImage[] bgImgA = {bgImg};
        this.setBackground(new Background(bgFillA,bgImgA));
        amountOrLevel.setText(weapon.getLevel());
        cooldownVisible = true;
        cooldownOverlay.setVisible(true);
        applyCooldownProgress(weapon.getCooldownProgress());
    }

    /**
     * Per-frame cooldown bar update. Called by the panel from the game loop.
     * No-op when this square is not currently displaying a weapon.
     */
    public void tickCooldown(Weapon weapon){
        if (!cooldownVisible || weapon == null) return;
        applyCooldownProgress(weapon.getCooldownProgress());
    }

    private void applyCooldownProgress(double progress){
        if (progress < 0) progress = 0;
        if (progress > 1) progress = 1;
        double remainingFraction = 1.0 - progress;
        double height = SQUARE_SIZE * remainingFraction;
        cooldownOverlay.setHeight(height);
        cooldownOverlay.setY(SQUARE_SIZE - height);
    }
    public void updateAccessorySquare(Accessory accessory){
        BackgroundFill bgFill = new BackgroundFill(this.getBaseColor(), CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill[] bgFillA = {bgFill};
        BackgroundSize bgSize = new BackgroundSize(50,50,false,false,false,false);
        BackgroundImage bgImg = new BackgroundImage(accessory.getIcon(), null, null, null, bgSize);
        BackgroundImage[] bgImgA = {bgImg};
        this.setBackground(new Background(bgFillA,bgImgA));
        amountOrLevel.setText(accessory.getLevel());
    }

    public Color getBaseColor() {
        return baseColor;
    }
    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }
}

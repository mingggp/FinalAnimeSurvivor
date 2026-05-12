package utils;

import entity.accessory.Accessory;
import entity.item.Item;
import entity.weapon.Weapon;
import gui.gameLayout.*;
import entityInterface.GameObject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

public class SceneManager {

    private static Scene scene;
    private static Parent menuRoot;
    private static StackPane gameRoot;
    private static Parent characterMenuRoot;
    private static Parent upgradeMenuRoot;
    private static Parent settingRoot;
    private static Parent collectionRoot;
    private static Parent achievementRoot;

    private static BackpackPanel backpackPanel;
    private static LevelPanel levelPanel;
    private static Parent statPanel;
    private static WeaponAndAccessoryPanel weaponAndAccessoryPanel;
    private static TopPanel topPanel;
    private static Parent quitGamePanel;
    private static LevelUpChoice levelUpChoice;
    private static ChestChoice chestChoice;

    private static boolean wasBackpackVisible;

    // For normal scene



    public static void setInitialScene(Scene scene,StackPane gameRoot,Parent menuRoot,Parent characterMenuRoot,Parent upgradeMenuRoot,Parent settingRoot,Parent collectionRoot,Parent achievementRoot) {
        SceneManager.scene = scene;
        SceneManager.gameRoot = gameRoot;
        SceneManager.menuRoot = menuRoot;
        SceneManager.characterMenuRoot = characterMenuRoot;
        SceneManager.upgradeMenuRoot = upgradeMenuRoot;
        SceneManager.settingRoot = settingRoot;
        SceneManager.collectionRoot = collectionRoot;
        SceneManager.achievementRoot = achievementRoot;

    }

    public static void switchToGame(){
        scene.setRoot(gameRoot);
        backpackPanel.setVisible(true);
        wasBackpackVisible=true;
    }
    public static void switchToMenu(){
        statPanel.setVisible(false);
        quitGamePanel.setVisible(false);
        scene.setRoot(menuRoot);
    }
    public static void switchToCharacterMenu(){
        scene.setRoot(characterMenuRoot);
    }
    public static void switchToUpgrade(){
        scene.setRoot(upgradeMenuRoot);
    }
    public static void switchToSetting(){
        scene.setRoot(settingRoot);
    }
    public static void switchToCollection(){
        scene.setRoot(collectionRoot);
    }
    public static void switchToAchievement(){
        scene.setRoot(achievementRoot);
    }

    // For game layout

    public static void setInitialGameLayout(BackpackPanel backpackPanel, LevelPanel levelPanel, Parent statPanel, WeaponAndAccessoryPanel weaponAndAccessoryPanel, TopPanel topPanel, Parent quitGamePanel, LevelUpChoice levelUpChoice,ChestChoice chestChoice){
        SceneManager.backpackPanel = backpackPanel;
        SceneManager.levelPanel = levelPanel;
        SceneManager.statPanel = statPanel;
        SceneManager.weaponAndAccessoryPanel = weaponAndAccessoryPanel;
        SceneManager.topPanel = topPanel;
        SceneManager.quitGamePanel = quitGamePanel;
        SceneManager.levelUpChoice = levelUpChoice;
        SceneManager.chestChoice = chestChoice;
        SceneManager.gameRoot.getChildren().add(levelPanel);
        SceneManager.gameRoot.getChildren().add(statPanel);
        SceneManager.gameRoot.getChildren().add(weaponAndAccessoryPanel);
        SceneManager.gameRoot.getChildren().add(topPanel);
        SceneManager.gameRoot.getChildren().add(quitGamePanel);
        SceneManager.gameRoot.getChildren().add(backpackPanel);
        SceneManager.gameRoot.getChildren().add(levelUpChoice);
        SceneManager.gameRoot.getChildren().add(chestChoice);

    }
    public static void pause(){
        SoundManager.getInstance().pauseBGM();
        backpackPanel.setVisible(false);
        statPanel.setVisible(true);
        quitGamePanel.setVisible(true);
    }
    public static void unpause(){
        SoundManager.getInstance().playBGM();
        statPanel.setVisible(false);
        quitGamePanel.setVisible(false);
        backpackPanel.setVisible(wasBackpackVisible);
    }
    public static void showQuit(){
        backpackPanel.setVisible(false);
        quitGamePanel.setVisible(true);
    }
    public static void showLevelUpChoice(){
        levelUpChoice.setVisible(true);
        backpackPanel.setVisible(false);
    }
    public static void hideLevelUpChoice(){
        levelUpChoice.setVisible(false);
        backpackPanel.setVisible(wasBackpackVisible);
    }

    public static void updateLevelUpChoice(){
        levelUpChoice.update();
    }
    public static void showChestChoice(){
        chestChoice.setVisible(true);
        backpackPanel.setVisible(false);
    }
    public static void hideChestChoice(){
        chestChoice.setVisible(false);
        backpackPanel.setVisible(wasBackpackVisible);
    }

    public static void updateChestChoice(GameObject gameObject){
        chestChoice.update(gameObject);
    }
    public static void updateClock(double gameTimer,int level){
        topPanel.update(gameTimer,level);
    }
    public static void toggleBackPack(){
        backpackPanel.setVisible(!backpackPanel.isVisible());
        wasBackpackVisible = backpackPanel.isVisible();
    }
    public static void updateLevelBar(int currentExperience,int ExperienceForNextLevel){
        levelPanel.setProgress((double) currentExperience /ExperienceForNextLevel);
    }
    public static void updateBackPack(Item[] backpack){
        backpackPanel.updateBackPack(backpack);
    }
    public static void updateWeaponAndAccessoryPanel(Weapon[] weaponList, Accessory[] accessoryList){
        weaponAndAccessoryPanel.updateWeaponAndAccessoryPanel(weaponList, accessoryList);
    }
    public static void tickWeaponCooldowns(Weapon[] weaponList){
        weaponAndAccessoryPanel.tickCooldowns(weaponList);
    }
}

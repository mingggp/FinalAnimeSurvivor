package utils;

import entity.accessory.Accessory;
import entity.item.Item;
import entity.weapon.Weapon;
import gui.gameLayout.*;
import gui.gameLayout.InGameSettingPanel;
import entityInterface.GameObject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * Static façade that manages all JavaFX scene-root swaps and in-game UI updates.
 *
 * <p>{@code SceneManager} acts as the View layer in an MVC-like structure.
 * {@link core.GameManager} never touches JavaFX nodes directly; instead it calls
 * static methods on {@code SceneManager} which delegate to the appropriate
 * JavaFX UI components.
 *
 * <h2>Initialisation</h2>
 * <p>Before any method can be used, two one-time registration calls must be made:
 * <ol>
 *   <li>{@link #setInitialScene} — registers the primary {@link Scene} and all
 *       non-game {@link Parent} roots (menu, character-select, settings, …).</li>
 *   <li>{@link #setInitialGameLayout} — registers the in-game overlay panels
 *       (backpack, top bar, level progress, weapon/accessory grid, …).</li>
 * </ol>
 *
 * <h2>Thread safety</h2>
 * <p>All methods must be called on the JavaFX Application Thread.
 */
public class SceneManager {

    /** The primary JavaFX scene; its root is swapped to change "screens". */
    private static Scene scene;

    /** FXML root for the main menu screen. */
    private static Parent menuRoot;

    /** StackPane that hosts the game canvas and all in-game overlay panels. */
    private static StackPane gameRoot;

    /** FXML root for the character-selection screen. */
    private static Parent characterMenuRoot;

    /** FXML root for the passive upgrade / shop screen. */
    private static Parent upgradeMenuRoot;

    /** FXML root for the settings screen. */
    private static Parent settingRoot;

    /** FXML root for the collection browser screen. */
    private static Parent collectionRoot;

    /** FXML root for the achievement gallery screen. */
    private static Parent achievementRoot;

    /** In-game backpack / inventory overlay panel. */
    private static BackpackPanel backpackPanel;

    /** Experience progress-bar panel displayed at the bottom of the screen. */
    private static LevelPanel levelPanel;

    /** Stats panel shown while the game is paused. */
    private static Parent statPanel;

    /** Weapon and accessory slot grid displayed during gameplay. */
    private static WeaponAndAccessoryPanel weaponAndAccessoryPanel;

    /** Top bar showing the game timer and current level. */
    private static TopPanel topPanel;

    /** Quit / return-to-menu panel shown on death or pause. */
    private static Parent quitGamePanel;

    /** Panel that presents the three level-up choices to the player. */
    private static LevelUpChoice levelUpChoice;

    /** Panel that shows the chest reward to the player. */
    private static ChestChoice chestChoice;

    /** In-game settings overlay, accessible from pause screen. */
    private static InGameSettingPanel inGameSettingPanel;

    /**
     * Tracks whether the backpack was visible before a temporary overlay
     * (level-up, chest, pause) hid it, so it can be restored on dismiss.
     */
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

    public static void setInitialGameLayout(BackpackPanel backpackPanel, LevelPanel levelPanel, Parent statPanel, WeaponAndAccessoryPanel weaponAndAccessoryPanel, TopPanel topPanel, Parent quitGamePanel, LevelUpChoice levelUpChoice, ChestChoice chestChoice){
        SceneManager.backpackPanel = backpackPanel;
        SceneManager.levelPanel = levelPanel;
        SceneManager.statPanel = statPanel;
        SceneManager.weaponAndAccessoryPanel = weaponAndAccessoryPanel;
        SceneManager.topPanel = topPanel;
        SceneManager.quitGamePanel = quitGamePanel;
        SceneManager.levelUpChoice = levelUpChoice;
        SceneManager.chestChoice = chestChoice;

        // Build in-game settings panel; Back returns to pause overlay
        inGameSettingPanel = new InGameSettingPanel(() -> {
            inGameSettingPanel.setVisible(false);
            quitGamePanel.setVisible(true);
        });
        ((QuitGamePanel) quitGamePanel).setSettingPanel(inGameSettingPanel);

        SceneManager.gameRoot.getChildren().add(levelPanel);
        SceneManager.gameRoot.getChildren().add(statPanel);
        SceneManager.gameRoot.getChildren().add(weaponAndAccessoryPanel);
        SceneManager.gameRoot.getChildren().add(topPanel);
        SceneManager.gameRoot.getChildren().add(quitGamePanel);
        SceneManager.gameRoot.getChildren().add(inGameSettingPanel);
        SceneManager.gameRoot.getChildren().add(backpackPanel);
        SceneManager.gameRoot.getChildren().add(levelUpChoice);
        SceneManager.gameRoot.getChildren().add(chestChoice);
    }
    public static void pause(){
        SoundManager.getInstance().pauseBGM();
        backpackPanel.setVisible(false);
        statPanel.setVisible(true);
        ((QuitGamePanel) quitGamePanel).setDeathMode(false);
        quitGamePanel.setVisible(true);
    }
    public static void unpause(){
        SoundManager.getInstance().playBGM();
        statPanel.setVisible(false);
        inGameSettingPanel.setVisible(false);
        quitGamePanel.setVisible(false);
        backpackPanel.setVisible(wasBackpackVisible);
    }
    public static void showQuit(){
        SoundManager.getInstance().stopBGM();
        backpackPanel.setVisible(false);
        ((QuitGamePanel) quitGamePanel).setDeathMode(true);
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

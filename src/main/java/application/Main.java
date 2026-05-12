package application;

import core.GameManager;
import core.GameState;
import gui.*;
import gui.gameLayout.*;
import javafx.scene.input.KeyCombination;
import utils.SceneManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.SoundManager;




public class Main extends Application {


    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;
    private static final int FPS = 60;

    private static GameManager gameManager;

    private long lastNanoTime;
    private MenuCanvas menuCanvas;
    private GameCanvas gameCanvas;
    private CharacterMenuCanvas characterMenuCanvas;
    private UpgradeMenuCanvas upgradeMenuCanvas;
    private SettingCanvas settingCanvas;
    private CollectionCanvas collectionCanvas;
    private AchievementCanvas achievementCanvas;
    private double frameTime;
    private double accumulateDeltaTime;
    private int frameCount;
    private double updateDeltaTime;
    private double time;

    private BackpackPanel backpackPanel;
    private TopPanel topPanel;
    private LevelPanel levelPanel;
    private QuitGamePanel quitGamePanel;
    private StatPanel statPanel;
    private WeaponAndAccessoryPanel weaponAndAccessoryPanel;
    private LevelUpChoice levelUpChoice;
    private ChestChoice chestChoice;

    @Override
    public void start(Stage primaryStage){
        SoundManager.getInstance().loadSounds();

        gameManager = new GameManager();

        gameCanvas = new GameCanvas(WINDOW_WIDTH, WINDOW_HEIGHT, gameManager);
        menuCanvas = new MenuCanvas(gameManager);
        characterMenuCanvas = new CharacterMenuCanvas(gameManager);
        upgradeMenuCanvas = new UpgradeMenuCanvas(gameManager);
        settingCanvas = new SettingCanvas(gameManager);
        collectionCanvas = new CollectionCanvas(gameManager);
        achievementCanvas = new AchievementCanvas(gameManager);

        StackPane gameRoot = new StackPane();
        gameRoot.getChildren().add(gameCanvas);

        StackPane menuRoot = new StackPane();
        menuRoot.getChildren().add(menuCanvas);

        StackPane characterMenuRoot = new StackPane();
        characterMenuRoot.getChildren().add(characterMenuCanvas);

        StackPane upgradeMenuRoot = new StackPane();
        upgradeMenuRoot.getChildren().add(upgradeMenuCanvas);

        StackPane settingRoot = new StackPane();
        settingRoot.getChildren().add(settingCanvas);

        StackPane collectionRoot = new StackPane();
        collectionRoot.getChildren().add(collectionCanvas);

        StackPane achievementRoot = new StackPane();
        achievementRoot.getChildren().add(achievementCanvas);

        Scene scene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

        SceneManager.setInitialScene(scene,gameRoot,menuRoot,characterMenuRoot,upgradeMenuRoot,settingRoot,collectionRoot,achievementRoot);

        backpackPanel = new BackpackPanel(gameManager);
        levelPanel = new LevelPanel();
        statPanel = new StatPanel();
        weaponAndAccessoryPanel = new WeaponAndAccessoryPanel(gameManager);
        topPanel = new TopPanel();
        quitGamePanel = new QuitGamePanel(gameManager);
        //level up choice
        levelUpChoice = new LevelUpChoice(gameManager);
        //chest choice
        chestChoice = new ChestChoice(gameManager);

        SceneManager.setInitialGameLayout(backpackPanel,levelPanel,statPanel,weaponAndAccessoryPanel,topPanel,quitGamePanel,levelUpChoice,chestChoice);


        scene.setOnKeyPressed(event -> {
            String code = event.getCode().toString();
            gameManager.getInputManager().addKey(code);


            if (code.equals("ESCAPE")) {
                GameState st = gameManager.getCurrentState();
                if (st == GameState.PLAYING) {
                    gameManager.setCurrentState(GameState.PAUSED);
                    SceneManager.pause();
                }
                else if (st == GameState.PAUSED) {
                    gameManager.setCurrentState(GameState.PLAYING);
                    SceneManager.unpause();
                }
                else if (st == GameState.CHARACTER_MENU || st == GameState.UPGRADE_MENU || st == GameState.SETTINGS || st == GameState.ACHIEVEMENT || st == GameState.COLLECTION){
                    gameManager.setCurrentState(GameState.MAIN_MENU);
                    SceneManager.switchToMenu();
                }
            }
            if (code.equals("TAB")) {
                GameState st = gameManager.getCurrentState();
                if (st == GameState.PLAYING) {
                    SceneManager.toggleBackPack();
                }
            }
            if (code.equals("SPACE")) {
                GameState st = gameManager.getCurrentState();
                if (st == GameState.CHEST) {
                    SceneManager.hideChestChoice();
                    gameManager.setCurrentState(GameState.PLAYING);
                }
            }

        });

        scene.setOnKeyReleased(event -> {
            gameManager.getInputManager().removeKey(event.getCode().toString());
        });




        primaryStage.setTitle("Anime Survivor");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.valueOf("F11"));
        primaryStage.show();

        primaryStage.iconifiedProperty().addListener((observable, wasMinimized, isNowMinimized) -> {
            if (isNowMinimized && (gameManager.getCurrentState() == GameState.PLAYING)) {
                gameManager.setCurrentState(GameState.PAUSED);
                SceneManager.pause();
            }
        });

        gameCanvas.requestFocus();

        frameTime = 1000000000.0 / FPS;
        accumulateDeltaTime = 0;
        frameCount = 0;
        time = 0;

        lastNanoTime = System.nanoTime();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double deltaTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                accumulateDeltaTime += deltaTime;
                updateDeltaTime += (currentNanoTime - lastNanoTime)/(frameTime);
                time += deltaTime;
                lastNanoTime = currentNanoTime;


                if(updateDeltaTime >= 1){
                    gameManager.update(accumulateDeltaTime);
                    frameCount++;
                    accumulateDeltaTime = 0;
                    updateDeltaTime %= 1;
                }
                gameCanvas.render(gameManager);

                if(time >= 1){
                    time--;
                    System.out.println(frameCount);
                    frameCount = 0;
                }

            }
        };
        timer.start();

    }


}

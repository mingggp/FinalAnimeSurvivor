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




/**
 * JavaFX {@link Application} entry point for Final Anime Survivor.
 *
 * <h2>Startup flow</h2>
 * <ol>
 *   <li>Load audio resources via {@link utils.SoundManager}.</li>
 *   <li>Instantiate {@link core.GameManager} (builds all master lists).</li>
 *   <li>Create one canvas per "screen" (menu, game, character select, …).</li>
 *   <li>Register canvases and UI panels with {@link utils.SceneManager}.</li>
 *   <li>Attach keyboard handlers to the {@link javafx.scene.Scene}.</li>
 *   <li>Start a capped-60-FPS {@link javafx.animation.AnimationTimer}.</li>
 * </ol>
 *
 * <h2>Game loop</h2>
 * <p>The {@link javafx.animation.AnimationTimer} accumulates elapsed time and
 * calls {@link core.GameManager#update(double)} exactly once per logical frame
 * (at 60 FPS).  {@link gui.GameCanvas#render(core.GameManager)} is called
 * every animation-timer tick (screen refresh rate) to keep rendering smooth.
 *
 * <h2>Keyboard bindings</h2>
 * <ul>
 *   <li>{@code WASD} — movement (forwarded to {@link utils.InputManager})</li>
 *   <li>{@code ESC} — toggle pause / return to menu</li>
 *   <li>{@code TAB} — toggle backpack visibility</li>
 *   <li>{@code SPACE} — dismiss chest reward panel</li>
 *   <li>{@code F11} — exit full-screen</li>
 * </ul>
 */
public class Main extends Application {

    /** Target canvas width in pixels. */
    private static final int WINDOW_WIDTH = 1920;

    /** Target canvas height in pixels. */
    private static final int WINDOW_HEIGHT = 1080;

    /** Target simulation update rate. */
    private static final int FPS = 60;

    /** The single shared game manager instance for this session. */
    private static GameManager gameManager;

    /** Timestamp of the previous animation-timer tick in nanoseconds. */
    private long lastNanoTime;

    private MenuCanvas menuCanvas;
    private GameCanvas gameCanvas;
    private CharacterMenuCanvas characterMenuCanvas;
    private UpgradeMenuCanvas upgradeMenuCanvas;
    private SettingCanvas settingCanvas;
    private CollectionCanvas collectionCanvas;
    private AchievementCanvas achievementCanvas;

    /** Duration of one logical frame in nanoseconds (1 000 000 000 / FPS). */
    private double frameTime;

    /** Accumulated real-world seconds since the last simulation update. */
    private double accumulateDeltaTime;

    /** FPS counter — incremented each simulation step, reset every real second. */
    private int frameCount;

    /** Accumulated fractional frames (drives 60-FPS capping logic). */
    private double updateDeltaTime;

    /** Wall-clock accumulator used to print FPS once per second. */
    private double time;

    private BackpackPanel backpackPanel;
    private TopPanel topPanel;
    private LevelPanel levelPanel;
    private QuitGamePanel quitGamePanel;
    private StatPanel statPanel;
    private WeaponAndAccessoryPanel weaponAndAccessoryPanel;
    private LevelUpChoice levelUpChoice;
    private ChestChoice chestChoice;

    /**
     * Fat-JAR entry point (non-modular / classpath deployment).
     * The {@link Launcher} shim is used for modular runs; this method
     * covers the shadow-JAR case where {@code Main} is the manifest class.
     */
    public static void main(String[] args) {
        launch(args);
    }

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

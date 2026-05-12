package core;

/**
 * Enumerates every distinct state the game can be in at any given moment.
 *
 * <p>{@link GameManager} holds the current state in its {@code currentState}
 * field and switches between them as the player navigates menus and plays.
 * {@link utils.SceneManager} and the JavaFX {@link javafx.animation.AnimationTimer}
 * in {@link gui.GameCanvas} check this value to decide which panels to show
 * and whether to advance the game simulation.
 *
 * <p>State transitions (simplified):
 * <pre>
 * MAIN_MENU ──► CHARACTER_MENU ──► PLAYING ──► LEVEL_UP ──► PLAYING
 *                                     │                │
 *                                     └──► CHEST ──────┘
 *                                     │
 *                                     └──► PAUSED ──► PLAYING / MAIN_MENU
 *                                     │
 *                                     └──► DEATH ──► GAME_OVER ──► MAIN_MENU
 * </pre>
 */
public enum GameState {

    /** The title / home screen shown at application start. */
    MAIN_MENU,

    /** Screen where the player picks a playable character. */
    CHARACTER_MENU,

    /** Stage / map selection screen (future expansion). */
    STAGE_SELECTION,

    /** Passive-upgrade purchase menu between runs. */
    UPGRADE_MENU,

    /** Achievement gallery screen. */
    ACHIEVEMENT,

    /** Item / weapon collection browser screen. */
    COLLECTION,

    /** Active gameplay — the game loop advances every frame. */
    PLAYING,

    /** Game is paused; the stat panel and quit panel are shown. */
    PAUSED,

    /** Settings / options menu. */
    SETTINGS,

    /** Level-up screen where the player chooses a new weapon or upgrade. */
    LEVEL_UP,

    /** Chest reward screen shown when the player steps on a chest. */
    CHEST,

    /** Player has died; a short delay plays before transitioning to GAME_OVER. */
    DEATH,

    /** Final score / game-over screen before returning to MAIN_MENU. */
    GAME_OVER
}

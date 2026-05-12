package core;

import entity.enemy.Enemy;

import java.util.ArrayList;
import java.util.Random;

/**
 * Controls when and where new enemies are spawned during a run.
 *
 * <p>Enemy templates are registered with {@link #addEnemyToSpawner(Enemy)}
 * before the run starts.  Each call to {@link #update(int, double, double)}
 * checks the current level and live-enemy count against scaling thresholds
 * and adds new enemies to the game via {@link GameManager#addEnemy(Enemy)}.
 *
 * <h2>Spawn position</h2>
 * <p>Enemies are placed just outside the visible screen boundary (≈960 px
 * left/right or ≈540 px up/down from the player) with a small random offset
 * so they appear to come from all directions.  Positions are clamped to keep
 * enemies inside the map border.
 *
 * <h2>Scaling tiers</h2>
 * <ul>
 *   <li>Level &lt;  50 → up to {@code level × 2} enemies, one per 0.5 s</li>
 *   <li>Level ≥  50 → cap 100 enemies, one per 0.3 s</li>
 *   <li>Level ≥ 100 → cap 175 enemies, one per 0.15 s</li>
 *   <li>Level ≥ 120 → cap 250 enemies, spawn every frame</li>
 * </ul>
 */
public class EnemySpawner {

    /** Reference to the game manager used to add enemies and read player position. */
    private final GameManager gameManager;

    /** Random source for computing spawn offsets. */
    private final Random random;

    /**
     * Template enemies added before the run.  Currently only the first element
     * is used; future work may randomise from the full list.
     */
    private final ArrayList<Enemy> spawnableEnemyList;

    /**
     * Internal accumulator that tracks how much time has elapsed since the
     * last spawn attempt.  Subtracted by the appropriate interval each time
     * an enemy is spawned.
     */
    private double time;

    /**
     * Constructs a spawner linked to the given {@link GameManager}.
     *
     * @param gameManager the game manager that owns the in-game enemy list
     */
    public EnemySpawner(GameManager gameManager) {
        this.gameManager = gameManager;
        this.random = new Random();
        this.spawnableEnemyList = new ArrayList<>();
        this.time = 0;
    }

    /**
     * Advances the spawner by one frame, adding new enemies as needed.
     *
     * @param level               current player level (drives the cap and rate)
     * @param gameTimer           total elapsed game time in seconds (reserved
     *                            for future wave / boss scripting)
     * @param accumulateDeltaTime seconds since the last frame
     */
    public void update(int level, double gameTimer, double accumulateDeltaTime) {
        time += accumulateDeltaTime;
        double characterMapX = gameManager.getCharacter().getMapX();
        double characterMapY = gameManager.getCharacter().getMapY();

        double ex, ey;

        // Choose a random edge of the screen and pick a random position along it
        if (random.nextBoolean()) {
            ex = random.nextBoolean()
                    ? characterMapX + 960 + Math.random() * 200
                    : characterMapX - 960 - Math.random() * 200;
            ey = characterMapY - 740 + Math.random() * 1480;
        } else {
            ex = characterMapX - 1160 + Math.random() * 2320;
            ey = random.nextBoolean()
                    ? characterMapY + 540 + Math.random() * 200
                    : characterMapY - 540 - Math.random() * 200;
        }

        // Clamp spawn position to the playable map area
        if (ex < 192) ex = 192;
        if (ey < 192) ey = 192;
        if (ex > 3648 * 2) ex = 3648 * 2;
        if (ey > 2688 * 2) ey = 2688 * 2;

        if (level < 50 && gameManager.getInGameEnemyList().size() < level * 2) {
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(), ex, ey));
            time -= 0.5;
        }
        if (level >= 50 && gameManager.getInGameEnemyList().size() < 100) {
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(), ex, ey));
            time -= 0.3;
        }
        if (level >= 100 && gameManager.getInGameEnemyList().size() < 175) {
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(), ex, ey));
            time -= 0.15;
        }
        if (level >= 120 && gameManager.getInGameEnemyList().size() < 250) {
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(), ex, ey));
            time = 0;
        }
    }

    /**
     * Registers an enemy template to be used as a spawn source.
     *
     * @param enemy the template enemy (stats only, no world position required)
     */
    public void addEnemyToSpawner(Enemy enemy) {
        spawnableEnemyList.add(enemy);
    }

    /**
     * Clears the spawn template list and resets the accumulator timer.
     * Called by {@link GameManager#resetGame()} when the player returns to the
     * main menu.
     */
    public void resetEnemyInSpawner() {
        spawnableEnemyList.clear();
        time = 0;
    }
}

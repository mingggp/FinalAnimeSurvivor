package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;

/**
 * Singleton manager for all background music (BGM) and sound effects (SFX).
 *
 * <h2>Singleton pattern</h2>
 * <p>Only one {@code SoundManager} is ever created; retrieve it via
 * {@link #getInstance()}.  This ensures only a single {@link MediaPlayer}
 * is playing BGM at any time.
 *
 * <h2>Resource loading</h2>
 * <p>Audio resources are loaded with {@link Class#getResource(String)}, which
 * returns a {@code jar:} URL that works both in the IDE and in a packaged JAR.
 * The old {@code new File(path)} approach only worked when the application
 * was run from an exploded directory.
 *
 * <h2>BGM vs SFX</h2>
 * <ul>
 *   <li><b>BGM</b>: a single long-running looping track managed by
 *       {@link #musicPlayer}.  Pre-loaded via {@link #loadMediaPlayer(String)}
 *       and controlled with {@link #startBGM(String)}, {@link #pauseBGM()}, and
 *       {@link #playBGM()}.</li>
 *   <li><b>SFX</b>: short one-shot clips played via {@link #playSFX(String)}.
 *       Each call creates a disposable {@link MediaPlayer} that destroys itself
 *       after playback ends.</li>
 * </ul>
 */
public class SoundManager {

    /** The single instance — lazily created on first {@link #getInstance()} call. */
    private static SoundManager instance;

    /** Currently active background-music player; {@code null} before first load. */
    private MediaPlayer musicPlayer;

    /** Name key of the song currently loaded into {@link #musicPlayer}. */
    private String currentSongName;

    /** Master volume applied to both BGM and SFX (0.0 – 1.0). */
    private double volume = 0.5;

    /** Pre-loaded media resources keyed by name (e.g. {@code "BGM1"}). */
    private HashMap<String, Media> soundLibrary = new HashMap<>();

    /** Private constructor — use {@link #getInstance()} instead. */
    private SoundManager() { }

    /**
     * Returns the single {@code SoundManager} instance, creating it if needed.
     *
     * @return the shared {@code SoundManager}
     */
    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    /**
     * Loads all known BGM tracks into {@link #soundLibrary}.
     *
     * <p>Must be called once during application startup (after the JavaFX
     * toolkit is initialised) before any BGM can be played.
     */
    // ── SFX path constants ───────────────────────────────────────────────────
    public static final String SFX_UI_CLICK     = "/sfx/ui_click.wav";
    public static final String SFX_EXP_PICKUP   = "/sfx/exp_pickup.wav";
    public static final String SFX_LEVEL_UP     = "/sfx/level_up.wav";
    public static final String SFX_SUKUNA_ATK   = "/sfx/sukuna_attack.wav";
    public static final String SFX_GOJO_ATK     = "/sfx/gojo_attack.wav";

    public void loadSounds() {
        Media bgm1 = loadMedia("/bgm/aonosumika.mp3");
        if (bgm1 != null) soundLibrary.put("BGM1", bgm1);
    }

    /**
     * Helper that loads a single {@link Media} from a classpath resource path.
     *
     * @param resourcePath absolute classpath path (e.g. {@code "/bgm/aonosumika.mp3"})
     * @return the loaded {@link Media}, or {@code null} if the resource was not found
     */
    private Media loadMedia(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("[SoundManager] Missing resource: " + resourcePath);
            return null;
        }
        return new Media(url.toExternalForm());
    }

    /**
     * Prepares the BGM {@link MediaPlayer} for the named song.
     *
     * <p>If the song is not in {@link #soundLibrary}, an error is printed and
     * the method returns without changing state.  The player is configured to
     * loop indefinitely and respects the current {@link #volume}.
     *
     * @param songName key into {@link #soundLibrary} (e.g. {@code "BGM1"})
     */
    public void loadMediaPlayer(String songName) {
        Media media = soundLibrary.get(songName);
        if (media == null) {
            System.err.println("[SoundManager] No song named: " + songName);
            return;
        }
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(volume);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.currentSongName = songName;
    }

    /**
     * Starts (or restarts from the beginning) the named BGM track.
     *
     * <p>If a different song is currently loaded, {@link #loadMediaPlayer(String)}
     * is called first to swap the track.  If no player is available (e.g. the
     * sound library was not loaded), the call is silently ignored.
     *
     * @param songName key into {@link #soundLibrary} (e.g. {@code "BGM1"})
     */
    public void startBGM(String songName) {
        if (!songName.equals(currentSongName)) {
            loadMediaPlayer(songName);
        }
        if (musicPlayer == null) return;
        musicPlayer.seek(Duration.ZERO);
        musicPlayer.play();
    }

    /**
     * Pauses the currently playing BGM without resetting its position.
     * Call {@link #playBGM()} to resume from the same point.
     */
    public void pauseBGM() {
        if (musicPlayer != null) musicPlayer.pause();
    }

    /**
     * Resumes playback of the currently loaded BGM from its paused position.
     */
    public void playBGM() {
        if (musicPlayer != null) musicPlayer.play();
    }

    /**
     * Plays a one-shot sound effect from a classpath resource path.
     *
     * <p>A disposable {@link MediaPlayer} is created for each call and
     * automatically disposed when playback ends, so multiple SFX can overlap.
     *
     * <p>This method is JAR-safe: it uses {@link Class#getResource(String)}
     * rather than {@code new File(path)}.
     *
     * @param resourcePath absolute classpath path to the audio file
     *                     (e.g. {@code "/sfx/hit.mp3"})
     */
    /**
     * Sets the master volume for BGM and future SFX calls.
     *
     * @param volume value in [0.0, 1.0]
     */
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume));
        if (musicPlayer != null) musicPlayer.setVolume(this.volume);
    }

    /** Returns the current master volume in [0.0, 1.0]. */
    public double getVolume() { return volume; }

    /**
     * Plays a one-shot sound effect from a classpath resource path.
     * A disposable {@link MediaPlayer} is created per call and disposed after playback.
     *
     * @param resourcePath absolute classpath path (e.g. {@code "/sfx/ui_click.wav"})
     */
    public void playSFX(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("[SoundManager] SFX resource not found: " + resourcePath);
            return;
        }
        MediaPlayer sfx = new MediaPlayer(new Media(url.toExternalForm()));
        sfx.setVolume(volume);
        sfx.play();
        sfx.setOnEndOfMedia(sfx::dispose);
    }
}

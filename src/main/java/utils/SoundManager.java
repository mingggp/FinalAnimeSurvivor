package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;

/** Singleton manager for all BGM and SFX. */
public class SoundManager {

    public static final String SFX_UI_CLICK   = "/sfx/ui_click.wav";
    public static final String SFX_EXP_PICKUP = "/sfx/exp_pickup.wav";
    public static final String SFX_LEVEL_UP   = "/sfx/level_up.wav";
    public static final String SFX_SUKUNA_ATK = "/sfx/sukuna_attack.wav";
    public static final String SFX_GOJO_ATK   = "/sfx/gojo_attack.wav";

    private static SoundManager instance;
    private MediaPlayer musicPlayer;
    private String currentSongName;

    /** BGM volume 0–1. */
    private double bgmVolume = 0.5;
    /** SFX volume 0–1. */
    private double sfxVolume = 0.5;

    private HashMap<String, Media> soundLibrary = new HashMap<>();

    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void loadSounds() {
        Media bgm1 = loadMedia("/bgm/aonosumika.mp3");
        if (bgm1 != null) soundLibrary.put("BGM1", bgm1);
    }

    private Media loadMedia(String path) {
        URL url = getClass().getResource(path);
        if (url == null) { System.err.println("[SoundManager] Missing: " + path); return null; }
        return new Media(url.toExternalForm());
    }

    public void loadMediaPlayer(String songName) {
        Media media = soundLibrary.get(songName);
        if (media == null) { System.err.println("[SoundManager] No song: " + songName); return; }
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(bgmVolume);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.currentSongName = songName;
    }

    public void startBGM(String songName) {
        if (!songName.equals(currentSongName)) loadMediaPlayer(songName);
        if (musicPlayer == null) return;
        musicPlayer.seek(Duration.ZERO);
        musicPlayer.play();
    }

    public void pauseBGM() { if (musicPlayer != null) musicPlayer.pause(); }
    public void playBGM()  { if (musicPlayer != null) musicPlayer.play();  }
    public void stopBGM()  { if (musicPlayer != null) { musicPlayer.stop(); currentSongName = null; } }

    // ── Volume controls ──────────────────────────────────────────────────────

    public void setBgmVolume(double v) {
        bgmVolume = clamp(v);
        if (musicPlayer != null) musicPlayer.setVolume(bgmVolume);
    }
    public void setSfxVolume(double v) { sfxVolume = clamp(v); }
    public double getBgmVolume() { return bgmVolume; }
    public double getSfxVolume() { return sfxVolume; }

    /** Legacy single-volume setter — sets both BGM and SFX. */
    public void setVolume(double v) { setBgmVolume(v); setSfxVolume(v); }
    /** Legacy getter — returns BGM volume. */
    public double getVolume() { return bgmVolume; }

    private double clamp(double v) { return Math.max(0.0, Math.min(1.0, v)); }

    public void playSFX(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) { System.err.println("[SoundManager] SFX not found: " + resourcePath); return; }
        MediaPlayer sfx = new MediaPlayer(new Media(url.toExternalForm()));
        sfx.setVolume(sfxVolume);
        sfx.play();
        sfx.setOnEndOfMedia(sfx::dispose);
    }
}

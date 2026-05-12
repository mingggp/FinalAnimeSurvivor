package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;

public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer musicPlayer;
    private String currentSongName;
    private double volume = 0.5;
    private HashMap<String, Media> soundLibrary = new HashMap<>();

    private SoundManager() { } // Private constructor

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void loadSounds() {
        Media bgm1 = loadMedia("/bgm/aonosumika.mp3");
        if (bgm1 != null) soundLibrary.put("BGM1", bgm1);
    }

    private Media loadMedia(String resourcePath) {
        URL url = getClass().getResource(resourcePath);
        if (url == null) {
            System.err.println("[SoundManager] Missing resource: " + resourcePath);
            return null;
        }
        return new Media(url.toExternalForm());
    }

    public void loadMediaPlayer(String songName){
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

    public void startBGM(String songName) {
        if (!songName.equals(currentSongName)) {
            loadMediaPlayer(songName);
        }
        if (musicPlayer == null) return;
        musicPlayer.seek(Duration.ZERO);
        musicPlayer.play();
    }
    public void pauseBGM(){
        if (musicPlayer != null) musicPlayer.pause();
    }
    public void playBGM(){
        if (musicPlayer != null) musicPlayer.play();
    }

    /**
     * Play a one-shot sound effect from a classpath resource path
     * (e.g. "/sfx/hit.mp3"). JAR-safe replacement for the old File-based API.
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
    /*public void warmupAudio() {
        // Use any tiny sound you already loaded
        Media silence = soundCache.get("click");
        MediaPlayer hum = new MediaPlayer(silence);

        hum.setVolume(0.0); // Make it silent
        hum.setMute(true);  // Extra safety

        hum.setOnReady(() -> {
            hum.play();
            hum.stop(); // Once it plays for 1ms, the engine is "warm"
        });
    }*/
}

package utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;

public class SoundManager { // FROM GEMINI

    private static SoundManager instance;
    private MediaPlayer musicPlayer;
    private double volume = 0.5;
    private HashMap<String, Media> soundLibrary = new HashMap<>();

    private SoundManager() { } // Private constructor

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void loadSounds() {
        soundLibrary.put("BGM1", new Media(getClass().getResource("/bgm/aonosumika.mp3" ).toExternalForm()));
    }
    public void loadMediaPlayer(String songName){
        musicPlayer = new MediaPlayer(soundLibrary.get(songName));
        musicPlayer.setVolume(volume);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }
    public void startBGM(String songName) {
        musicPlayer.seek(Duration.ZERO);
        musicPlayer.play();
    }
    public void pauseBGM(){
        musicPlayer.pause();
    }
    public void playBGM(){
        musicPlayer.play();
    }

    public void playSFX(String fileName) {
        // Create a temporary player for short sounds
        Media hit = new Media(new File(fileName).toURI().toString());
        MediaPlayer sfx = new MediaPlayer(hit);
        sfx.play();
        // Automatically clean up when done
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

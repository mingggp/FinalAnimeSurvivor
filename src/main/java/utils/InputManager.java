package utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Tracks the set of keyboard keys currently held down.
 *
 * <p>The application's key-pressed / key-released handlers call
 * {@link #addKey(String)} and {@link #removeKey(String)} respectively.
 * Game logic queries {@link #isKeyPressed(String)} each frame to read
 * the current input state.
 *
 * <p>All key names are stored and compared in upper case so callers can
 * use either {@code "w"} or {@code "W"} interchangeably.
 */
public class InputManager {

    /** Set of upper-cased key names that are currently pressed. */
    private final Set<String> activeKeys = new HashSet<>();

    /**
     * Registers a key as currently pressed.
     *
     * @param key the key name as returned by
     *            {@link javafx.scene.input.KeyEvent#getText()} or
     *            {@link javafx.scene.input.KeyCode#getName()} (case-insensitive)
     */
    public void addKey(String key) {
        activeKeys.add(key.toUpperCase());
    }

    /**
     * Removes a key from the pressed set when it is released.
     *
     * @param key the key name to deregister (case-insensitive)
     */
    public void removeKey(String key) {
        activeKeys.remove(key.toUpperCase());
    }

    /**
     * Returns {@code true} if the given key is currently held down.
     *
     * @param key the key name to query (case-insensitive)
     * @return {@code true} if the key is in the active-keys set
     */
    public boolean isKeyPressed(String key) {
        return activeKeys.contains(key.toUpperCase());
    }
}

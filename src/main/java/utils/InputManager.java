package utils;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private final Set<String> activeKeys = new HashSet<>();


    public void addKey(String key) {
        activeKeys.add(key.toUpperCase());
    }


    public void removeKey(String key) {
        activeKeys.remove(key.toUpperCase());
    }


    public boolean isKeyPressed(String key) {
        return activeKeys.contains(key.toUpperCase());
    }
}

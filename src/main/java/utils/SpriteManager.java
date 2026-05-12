package utils;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized image loader that works both in the IDE and inside a packaged JAR.
 *
 * Background: {@code new Image("path/to/file.png")} relies on JavaFX resolving
 * a relative URL through the thread context class loader. That behaviour is
 * fragile when the application is launched as a modular JAR — the call often
 * fails with a "Invalid URL" or "resource not found" error.
 *
 * This class loads images explicitly through the class loader using an
 * absolute resource path, which is reliable in every launch mode.
 */
public final class SpriteManager {

    private static final Map<String, Image> CACHE = new HashMap<>();

    private SpriteManager() { }

    public static Image loadImage(String path) {
        if (path == null) return null;
        Image cached = CACHE.get(path);
        if (cached != null) return cached;

        String absolute = path.startsWith("/") ? path : "/" + path;
        InputStream stream = SpriteManager.class.getResourceAsStream(absolute);
        if (stream == null) {
            System.err.println("[SpriteManager] Resource not found: " + absolute);
            return null;
        }
        Image img = new Image(stream);
        CACHE.put(path, img);
        return img;
    }
}

package utils;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralised, JAR-safe image loader with an in-memory cache.
 *
 * <h2>Why this class exists</h2>
 * <p>{@code new Image("path/to/file.png")} resolves the path through the
 * JavaFX thread context class loader using a relative URL.  This works in the
 * IDE but silently fails inside a packaged modular JAR because the classpath
 * root is no longer accessible via a file URL.
 *
 * <p>This class uses {@link Class#getResourceAsStream(String)} with an
 * absolute classpath path (prefixed with {@code /}) instead, which is
 * guaranteed to work in every launch mode — IDE run, fat JAR, modular JAR.
 *
 * <h2>Cache</h2>
 * <p>Loaded images are stored in a {@link HashMap} keyed by their resource
 * path.  Subsequent calls with the same path return the cached instance,
 * avoiding repeated disk / classpath I/O.
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * Image icon = SpriteManager.loadImage("character/icon/gojoIcon.png");
 * }</pre>
 */
public final class SpriteManager {

    /** In-memory cache mapping resource paths to their loaded {@link Image}s. */
    private static final Map<String, Image> CACHE = new HashMap<>();

    /** Non-instantiable utility class. */
    private SpriteManager() { }

    /**
     * Loads an image from the classpath, or returns the cached copy if it was
     * already loaded.
     *
     * <p>The {@code path} may be relative (e.g. {@code "enemy/slime.png"}) or
     * absolute (e.g. {@code "/enemy/slime.png"}); a leading {@code /} is added
     * automatically if missing.
     *
     * <p>If the resource cannot be found, an error is printed to {@code stderr}
     * and {@code null} is returned — callers that draw with a {@code null} image
     * will simply skip the draw call (JavaFX ignores null images).
     *
     * @param path classpath-relative path to the image resource
     * @return the loaded {@link Image}, or {@code null} if the resource was not found
     */
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

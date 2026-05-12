package utils;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

public class JavaFXInitializer {
    private static boolean initialized = false;

    @BeforeAll
    public static void initJFX() {
        if (!initialized) {
            try {
                Platform.startup(() -> {});
                initialized = true;
            } catch (IllegalStateException e) {
                // Toolkit already initialized
                initialized = true;
            }
        }
    }
}

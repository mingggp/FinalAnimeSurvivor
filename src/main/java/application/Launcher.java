package application;

import javafx.application.Application;

/**
 * Thin launcher shim required when running a modular JavaFX application from a
 * fat JAR.
 *
 * <p>JavaFX {@link javafx.application.Application} subclasses cannot be used
 * as the JAR entry point in a modular deployment because the JDK checks that
 * the main class does not extend {@code Application}.  This class sidesteps the
 * restriction by delegating to {@link javafx.application.Application#launch}.
 */
public class Launcher {

    /**
     * Application entry point — delegates to {@link Main}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}

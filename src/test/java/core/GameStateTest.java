package core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sanity tests on the {@link GameState} enum — guards against accidentally
 * deleting or renaming a state, which would silently break SceneManager
 * branches that compare against these names.
 */
class GameStateTest {

    @Test
    void allRequiredStatesArePresent(){
        // If any of these names changes, this test must be updated deliberately.
        assertNotNull(GameState.valueOf("MAIN_MENU"));
        assertNotNull(GameState.valueOf("CHARACTER_MENU"));
        assertNotNull(GameState.valueOf("PLAYING"));
        assertNotNull(GameState.valueOf("PAUSED"));
        assertNotNull(GameState.valueOf("LEVEL_UP"));
        assertNotNull(GameState.valueOf("CHEST"));
        assertNotNull(GameState.valueOf("DEATH"));
    }

    @Test
    void valuesAreUnique(){
        GameState[] all = GameState.values();
        for (int i = 0; i < all.length; i++) {
            for (int j = i + 1; j < all.length; j++) {
                assertNotEquals(all[i], all[j]);
            }
        }
    }

    @Test
    void enumHasReasonableSize(){
        // We have ~13 states. If this drops below 10 something is very wrong.
        assertTrue(GameState.values().length >= 10,
                "Too few game states defined: " + GameState.values().length);
    }
}

package entityInterface;

import javafx.scene.canvas.GraphicsContext;

/**
 * Implemented by every game object that draws itself on the main game canvas.
 *
 * <p>{@link gui.GameCanvas} calls {@link #render(GraphicsContext)} on all
 * renderable objects each frame, passing the canvas's {@link GraphicsContext}
 * so the object can draw its sprite, shape, or text.
 *
 * <p>Rendering coordinates should be converted from world-space to screen-space
 * using {@link core.GameManager#getScreenX(double)} and
 * {@link core.GameManager#getScreenY(double)} before drawing.
 */
public interface Renderable {

    /**
     * Draws this object to the game canvas.
     *
     * <p>Called once per frame by the game loop.  Implementations must not
     * modify game state inside this method — only issue draw calls to {@code gc}.
     *
     * @param gc the {@link GraphicsContext} of the game canvas
     */
    void render(GraphicsContext gc);
}

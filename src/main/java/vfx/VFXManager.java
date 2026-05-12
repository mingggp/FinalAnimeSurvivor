package vfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Static manager for all visual effects (VFX) rendered on top of the game world.
 *
 * <p>VFX are grouped into five independent lists, each updated and rendered in a
 * specific layer of the draw stack:
 * <ol>
 *   <li><b>Ground effects</b> ({@link WorldEffect}) — drawn beneath enemies/player;
 *       follow or are fixed in world space.</li>
 *   <li><b>Damage texts</b> ({@link DamageText}) — floating numbers that drift upward
 *       and fade out after a hit.</li>
 *   <li><b>Slashes</b> ({@link Slash}) — brief directional slash animations.</li>
 *   <li><b>Screen effects</b> ({@link WorldEffect}) — full-screen overlays such as
 *       flash effects; rendered last so they cover everything.</li>
 *   <li><b>Skill pop-ups</b> ({@link SkillPopUp}) — skill activation icons briefly
 *       shown at the top of the screen.</li>
 * </ol>
 *
 * <p>Effects are added via the {@code spawn*} / {@code set*Effects} factory methods
 * and removed automatically when {@link vfx.DamageText#isExpired()} (or the
 * equivalent for other types) returns {@code true}.
 *
 * <p>All methods are static — this class is never instantiated.
 */
public class VFXManager {

    /** Active floating damage numbers. */
    private static final ArrayList<DamageText> damageTexts = new ArrayList<>();

    /** Active slash / hit animations. */
    private static final ArrayList<Slash> slashs = new ArrayList<>();

    /** Active world-space ground-layer effects. */
    private static final ArrayList<WorldEffect> groundEffects = new ArrayList<>();

    /** Active full-screen overlay effects. */
    private static final ArrayList<WorldEffect> screenEffects = new ArrayList<>();

    /** Active skill icon pop-ups. */
    private static final ArrayList<SkillPopUp> skillPopUps = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Update methods (called every frame by GameManager)
    // -------------------------------------------------------------------------

    /**
     * Advances all ground-layer effects and removes expired ones.
     *
     * @param accumulateDeltaTime seconds since the last frame
     * @param playerX             player's current world X (used by camera-following effects)
     * @param playerY             player's current world Y
     */
    public static void updateGround(double accumulateDeltaTime, double playerX, double playerY) {
        for (WorldEffect worldEffect : groundEffects) {
            worldEffect.update(accumulateDeltaTime, playerX, playerY);
        }
        groundEffects.removeIf(WorldEffect::isExpired);
    }

    /**
     * Advances damage texts, slashes, and skill pop-ups, then removes expired ones.
     *
     * @param accumulateDeltaTime seconds since the last frame
     */
    public static void update(double accumulateDeltaTime) {
        for (DamageText dt : damageTexts) {
            dt.update(accumulateDeltaTime);
        }
        for (SkillPopUp skillPopUp : skillPopUps) {
            skillPopUp.update(accumulateDeltaTime);
        }
        for (Slash slash : slashs) {
            slash.update(accumulateDeltaTime);
        }
        damageTexts.removeIf(DamageText::isExpired);
        slashs.removeIf(Slash::isExpired);
        skillPopUps.removeIf(SkillPopUp::isExpired);
    }

    /**
     * Advances all screen-overlay effects and removes expired ones.
     *
     * @param accumulateDeltaTime seconds since the last frame
     * @param playerX             player's current world X
     * @param playerY             player's current world Y
     */
    public static void updateScreenEffect(double accumulateDeltaTime, double playerX, double playerY) {
        for (WorldEffect worldEffect : screenEffects) {
            worldEffect.update(accumulateDeltaTime, playerX, playerY);
        }
        screenEffects.removeIf(WorldEffect::isExpired);
    }

    // -------------------------------------------------------------------------
    // Render methods (called every frame by GameCanvas in draw order)
    // -------------------------------------------------------------------------

    /**
     * Renders ground-layer world effects (beneath entities).
     *
     * @param gc      game canvas's {@link GraphicsContext}
     * @param playerX player's world X
     * @param playerY player's world Y
     */
    public static void renderGround(GraphicsContext gc, double playerX, double playerY) {
        for (WorldEffect worldEffect : groundEffects) {
            worldEffect.render(gc, playerX, playerY);
        }
    }

    /**
     * Renders damage texts and slash animations (above entities, below UI).
     *
     * @param gc             game canvas's {@link GraphicsContext}
     * @param characterMapX  player's world X (for screen-space conversion)
     * @param characterMapY  player's world Y
     */
    public static void render(GraphicsContext gc, double characterMapX, double characterMapY) {
        for (DamageText dt : damageTexts) {
            dt.render(gc, characterMapX, characterMapY);
        }
        for (Slash slash : slashs) {
            slash.render(gc, characterMapX, characterMapY);
        }
    }

    /**
     * Renders full-screen overlay effects (drawn last, on top of everything).
     *
     * @param gc      game canvas's {@link GraphicsContext}
     * @param playerX player's world X
     * @param playerY player's world Y
     */
    public static void renderScreen(GraphicsContext gc, double playerX, double playerY) {
        for (WorldEffect worldEffect : screenEffects) {
            worldEffect.render(gc, playerX, playerY);
        }
    }

    /**
     * Renders skill pop-up icons (drawn on the HUD layer).
     *
     * @param gc game canvas's {@link GraphicsContext}
     */
    public static void renderPopup(GraphicsContext gc) {
        for (SkillPopUp skillPopUp : skillPopUps) {
            skillPopUp.render(gc);
        }
    }

    // -------------------------------------------------------------------------
    // Factory / spawn methods
    // -------------------------------------------------------------------------

    /**
     * Spawns a floating damage number at the given world position.
     *
     * <p>A small random jitter is applied to X and Y so that numbers spawned on
     * the same enemy in the same frame do not perfectly overlap.
     *
     * @param x      world X of the hit entity
     * @param y      world Y of the hit entity
     * @param amount damage value to display
     * @param color  text colour (e.g. {@link Color#WHITE} for physical, {@link Color#YELLOW} for burn)
     */
    public static void spawnDamageText(double x, double y, double amount, Color color) {
        double offsetX = (Math.random() - 0.5) * 30;
        double offsetY = (Math.random() - 0.5) * 15;
        damageTexts.add(new DamageText(x + offsetX, y + offsetY, amount, color));
    }

    /**
     * Spawns a slash VFX at the given world position.
     *
     * @param x      world X centre of the slash
     * @param y      world Y centre of the slash
     * @param size   size multiplier for the slash sprite
     * @param cleave {@code true} to use the Cleave variant, {@code false} for the standard variant
     */
    public static void spawnSlash(double x, double y, int size, boolean cleave) {
        slashs.add(new Slash(x, y, size, Math.random() * 360, cleave));
    }

    /**
     * Adds a ground-layer image effect at the given world position.
     *
     * @param image      sprite to render
     * @param x          world X
     * @param y          world Y
     * @param width      render width in pixels
     * @param height     render height in pixels
     * @param lifespan   seconds until the effect expires
     * @param followCam  if {@code true} the effect scrolls with the camera
     * @param maxAlpha   starting opacity (0.0–1.0)
     * @param minAlpha   ending opacity when the effect fades out
     */
    public static void setGroundEffects(Image image, double x, double y, double width, double height,
                                        double lifespan, boolean followCam, double maxAlpha, double minAlpha) {
        groundEffects.add(new WorldEffect(image, x, y, width, height, lifespan, followCam, maxAlpha, minAlpha));
    }

    /**
     * Adds a ground-layer colour-fill effect at the given world position.
     *
     * @param color     fill colour
     * @param x         world X
     * @param y         world Y
     * @param width     render width in pixels
     * @param height    render height in pixels
     * @param lifespan  seconds until the effect expires
     * @param followCam if {@code true} the effect scrolls with the camera
     * @param maxAlpha  starting opacity (0.0–1.0)
     * @param minAlpha  ending opacity when the effect fades out
     */
    public static void setGroundEffects(Color color, double x, double y, double width, double height,
                                        double lifespan, boolean followCam, double maxAlpha, double minAlpha) {
        groundEffects.add(new WorldEffect(color, x, y, width, height, lifespan, followCam, maxAlpha, minAlpha));
    }

    /**
     * Adds a screen-overlay image effect.
     *
     * @param image      sprite to render
     * @param x          screen X (or world X if {@code followCam} is {@code false})
     * @param y          screen Y
     * @param width      render width
     * @param height     render height
     * @param lifespan   seconds until the effect expires
     * @param followCam  if {@code true} the position is treated as screen-space
     * @param maxAlpha   starting opacity
     * @param minAlpha   ending opacity
     */
    public static void setScreenEffects(Image image, double x, double y, double width, double height,
                                        double lifespan, boolean followCam, double maxAlpha, double minAlpha) {
        screenEffects.add(new WorldEffect(image, x, y, width, height, lifespan, followCam, maxAlpha, minAlpha));
    }

    /**
     * Adds a screen-overlay colour-fill effect.
     *
     * @param color     fill colour
     * @param x         screen X
     * @param y         screen Y
     * @param width     render width
     * @param height    render height
     * @param lifespan  seconds until the effect expires
     * @param followCam if {@code true} the position is treated as screen-space
     * @param maxAlpha  starting opacity
     * @param minAlpha  ending opacity
     */
    public static void setScreenEffects(Color color, double x, double y, double width, double height,
                                        double lifespan, boolean followCam, double maxAlpha, double minAlpha) {
        screenEffects.add(new WorldEffect(color, x, y, width, height, lifespan, followCam, maxAlpha, minAlpha));
    }

    /**
     * Displays a skill icon pop-up briefly on the HUD.
     *
     * @param image the icon image of the skill or weapon that was activated
     */
    public static void spawnSkillPopUp(Image image) {
        skillPopUps.add(new SkillPopUp(image));
    }

    /**
     * Clears all active VFX lists.  Called by {@link core.GameManager#resetGame()}
     * to ensure no stale effects carry over between runs.
     */
    public static void clear() {
        damageTexts.clear();
        slashs.clear();
        groundEffects.clear();
        skillPopUps.clear();
        screenEffects.clear();
    }
}

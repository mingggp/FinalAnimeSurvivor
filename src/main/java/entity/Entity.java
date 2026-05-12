package entity;


import entityInterface.GameObject;

/**
 * Root abstract class for every object that exists in the game world.
 *
 * <p>Every game object that occupies a position on the map (characters,
 * enemies, items, weapons, exp-orbs, etc.) extends {@code Entity}.
 * The class stores the world-space coordinates ({@link #getMapX()},
 * {@link #getMapY()}) and the logical name of the object.
 *
 * <p>Subclasses are expected to implement {@link GameObject#copy()} to
 * support the Prototype pattern used when duplicating weapons, items, and
 * accessories from the master lists.
 *
 * @see entityInterface.GameObject
 */
public abstract class Entity implements GameObject {

    /** Logical identifier used to look up icons, sounds, and drop tables. */
    private String name;

    /** World-space X coordinate (pixels). Origin is the top-left of the map. */
    private double mapX;

    /** World-space Y coordinate (pixels). Origin is the top-left of the map. */
    private double mapY;

    /** No-arg constructor required for subclasses that set their own state. */
    public Entity() {
    }

    /**
     * Returns the world-space Y coordinate of this entity.
     *
     * @return Y position in pixels from the top of the map
     */
    public double getMapY() {
        return mapY;
    }

    /**
     * Sets the world-space Y coordinate of this entity.
     *
     * @param mapY Y position in pixels
     */
    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    /**
     * Returns the world-space X coordinate of this entity.
     *
     * @return X position in pixels from the left of the map
     */
    public double getMapX() {
        return mapX;
    }

    /**
     * Sets the world-space X coordinate of this entity.
     *
     * @param mapX X position in pixels
     */
    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    /**
     * Returns the logical name of this entity.
     *
     * @return non-null name string (e.g. {@code "slime"}, {@code "cleave"})
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the logical name of this entity.
     *
     * @param name identifier string; should match the resource file name where applicable
     */
    public void setName(String name) {
        this.name = name;
    }
}

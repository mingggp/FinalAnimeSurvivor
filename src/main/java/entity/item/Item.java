package entity.item;

import entity.Entity;
import entityInterface.GameObject;
import javafx.scene.image.Image;

/**
 * Abstract base class for all items that the player can collect and store
 * in the backpack.
 *
 * <p>Items are stackable: the {@link #amount} field tracks how many copies of
 * the same item the player has picked up.  The {@link #taggedByMagnet} flag is
 * set by magnet-type accessories to pull items toward the player automatically.
 *
 * <p>Concrete subclasses implement one or more of the item behaviour interfaces:
 * <ul>
 *   <li>{@link entityInterface.itemInterface.Usable} — item is consumed on demand</li>
 *   <li>{@link entityInterface.itemInterface.Droppable} — item can be found on the map</li>
 *   <li>{@link entityInterface.itemInterface.Craftable} — item is assembled from materials</li>
 *   <li>{@link entityInterface.itemInterface.Unique} — at most one instance may exist at a time</li>
 * </ul>
 *
 * @see entity.item.Soda
 * @see entity.item.Harvest
 * @see entity.item.craftable.MalevolentKitchen
 */
public abstract class Item extends Entity implements GameObject {

    /** Number of this item in the player's backpack. Starts at 0 (not yet owned). */
    private int amount = 0;

    /** Icon displayed in the backpack grid. */
    private Image Icon;

    /**
     * Whether this item has been tagged by a magnet accessory and should
     * be pulled toward the player's position each frame.
     */
    protected boolean taggedByMagnet;

    /**
     * Constructs an item with the given logical name.
     *
     * @param name identifier string (e.g. {@code "soda"}); used to match
     *             stacks in the backpack and to look up resources
     */
    public Item(String name) {
        this.setName(name);
    }

    /**
     * Returns the stack size of this item in the backpack.
     *
     * @return number of copies currently held (0 = none)
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the stack size for this item.
     *
     * @param amount new count (must be ≥ 0)
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /** @return the inventory icon image, or {@code null} if not yet loaded */
    public Image getIcon() {
        return Icon;
    }

    /** @param icon the icon image to display in the backpack slot */
    public void setIcon(Image icon) {
        Icon = icon;
    }

    /**
     * Marks this dropped item as targeted by the player's magnet effect,
     * causing it to be attracted toward the player each frame.
     */
    public void tag() {
        taggedByMagnet = true;
    }
}

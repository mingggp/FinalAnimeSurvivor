package entityInterface.itemInterface;

/**
 * Marker interface for items that serve as crafting ingredients.
 *
 * <p>Items tagged with {@code Material} can be consumed by
 * {@link Craftable#deductMaterial()} when a craftable item is assembled.
 * The interface carries no methods; its sole purpose is to allow
 * {@link core.GameManager} to filter the backpack for eligible ingredients
 * via {@code instanceof Material}.
 *
 * @see entity.item.SukunaFinger
 */
public interface Material {
}

package entityInterface.itemInterface;

/**
 * Marker interface for items that can only appear once per run.
 *
 * <p>When a {@code Unique} item is offered as a drop or chest reward,
 * {@link core.GameManager} removes it from the pool so it will never be
 * offered again in the same session.  The interface carries no methods;
 * its purpose is to enable {@code instanceof Unique} checks in the
 * drop / chest logic.
 *
 * @see entity.item.SubaruShirt
 */
public interface Unique {
}

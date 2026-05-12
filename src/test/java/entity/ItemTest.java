package entity;

import entity.item.Item;
import entityInterface.GameObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the common logic on the abstract {@link Item} base — amount
 * accounting and the magnet tag flag.
 */
class ItemTest {

    private static class StubItem extends Item {
        StubItem(){ super("stub-item"); }
        @Override public GameObject copy(){ return this; }
        boolean isTagged(){ return taggedByMagnet; }
    }

    private StubItem item;

    @BeforeEach
    void setUp(){
        item = new StubItem();
    }

    @Test
    void defaultAmountIsZero(){
        assertEquals(0, item.getAmount());
    }

    @Test
    void setAmountStoresValue(){
        item.setAmount(7);
        assertEquals(7, item.getAmount());
    }

    @Test
    void tagFlipsMagnetFlag(){
        assertFalse(item.isTagged());
        item.tag();
        assertTrue(item.isTagged());
    }

    @Test
    void nameIsSetByConstructor(){
        assertEquals("stub-item", item.getName());
    }
}

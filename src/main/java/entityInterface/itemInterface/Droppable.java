package entityInterface.itemInterface;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;

public interface Droppable {
    void updateAsDroppedItem(double accumulateDeltaTime);
    void renderAsDroppedItem(GraphicsContext gc);
    BoundingBox getItemHitBox();
    void setItemHitBox(BoundingBox itemHitBox);
}

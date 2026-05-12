package vfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DamageText {
    private double x;
    private double y;
    private double amount;
    private double lifespan;
    private double age;
    //private boolean isCritical;
    private Color color;

    public DamageText(double x, double y, double amount,Color color) {
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.lifespan = 0.7;
        this.age = 0;
        this.color = color;
    }
    public void update(double accumulateDeltaTime) {
        this.y -= 40 * accumulateDeltaTime; // Float up
        this.age += accumulateDeltaTime;
    }
    public void render(GraphicsContext gc,double characterMapX,double characterMapY) {
        if (age > lifespan || !(x > characterMapX-960-96 && x < characterMapX+960 && y > characterMapY-540-96 && y < characterMapY+540)) return;
        
        double alpha = 1.0 - (age / lifespan);
        gc.setGlobalAlpha(alpha);
        
        gc.setFill(color);
        gc.setFont(Font.font("Impact", 30));
        
        double renderX = x -characterMapX+960;
        double renderY = y -characterMapY+540;
        
        // Draw shadow/outline for readability
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeText(String.format("-%.0f", amount), renderX, renderY);
        gc.fillText(String.format("-%.0f", amount), renderX, renderY);
        
        gc.setGlobalAlpha(1.0);
    }

    public boolean isExpired() {
        return age >= lifespan;
    }
}

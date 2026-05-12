package vfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Slash {

    private double x, y;
    private double lifespan;
    private double age;
    private int size;
    private double angle;
    private static final Image whiteSlash = new Image("weapon/asset/whiteSlash.png");
    private static final Image blackSlash = new Image("weapon/asset/blackSlash.png");
    private static int i = 0;
    private Image sprite;

    public Slash(double x, double y,int size,double angle,boolean cleave) {
        this.x = x;
        this.y = y;
        this.lifespan = 0.3;
        this.age = 0;
        this.size = size;
        this.angle = angle;
        if(i==0){
            sprite=whiteSlash;
            i++;
        }
        else if(i==1){
            sprite=blackSlash;
            i--;
        }
        if(cleave)sprite=blackSlash;
    }

    public void update(double accumulateDeltaTime) {
        this.age += accumulateDeltaTime;
    }

    public void render(GraphicsContext gc,double characterMapX,double characterMapY) {
        if (age > lifespan ) return;


        double alpha = 1.0 - (age / lifespan);
        gc.setGlobalAlpha(alpha);


        double renderX = x -characterMapX+960;
        double renderY = y -characterMapY+540;

        gc.save();

        gc.translate(renderX, renderY);
        gc.rotate(angle);
        gc.drawImage(sprite, -size/2.0, -size/2.0,size,size);
        gc.restore();

        gc.setGlobalAlpha(1.0);

    }

    public boolean isExpired() {
        return age >= lifespan;
    }
}

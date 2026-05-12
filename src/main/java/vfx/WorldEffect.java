package vfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class WorldEffect {

    private double x, y;
    private double lifespan;
    private double age;
    private double width;
    private double height;
    private Image image;
    private Color color;
    private boolean followCam;
    private double maxAlpha;
    private double minAlpha;

    public WorldEffect(Image image, double x, double y, double width, double height, double lifespan, boolean followCam ,double maxAlpha,double minAlpha){
        this.image = image;
        this.x =x;
        this.y = y;
        this.width=width;
        this.height=height;
        this.lifespan= lifespan;
        this.age = 0;
        this.followCam =followCam;
        this.maxAlpha = maxAlpha;
        this.minAlpha = minAlpha;
    }
    public WorldEffect(Color color, double x, double y, double width, double height, double lifespan, boolean followCam ,double maxAlpha,double minAlpha){
        this.color = color;
        this.x =x;
        this.y = y;
        this.width=width;
        this.height=height;
        this.lifespan= lifespan;
        this.age = 0;
        this.followCam=followCam;
        this.maxAlpha = maxAlpha;
        this.minAlpha=minAlpha;
    }
    public void update(double accumulateDeltaTime,double playerX,double playerY){
        age+=accumulateDeltaTime;
        if (followCam){
            this.x = playerX;
            this.y = playerY;
        }
    }
    public void render(GraphicsContext gc,double playerX,double playerY){
        double screenX = x - playerX + 960;
        double screenY = y - playerY +540;
        if(age<2){
            gc.setGlobalAlpha(age/2);
        }
        else if(lifespan-age<2){
            gc.setGlobalAlpha((lifespan-age)/2);
        }
        if(gc.getGlobalAlpha()>maxAlpha)gc.setGlobalAlpha(maxAlpha);
        if(gc.getGlobalAlpha()<minAlpha)gc.setGlobalAlpha(minAlpha);

        if(image != null){
            gc.drawImage(image,screenX-width/2,screenY-height/2,width,height);
        }
        else{
            gc.setFill(color);
            gc.fillRect(screenX-width/2,screenY-height/2,width,height);
        }
        gc.setGlobalAlpha(1.0);
    }
    public boolean isExpired(){
        return age > lifespan;
    }
}

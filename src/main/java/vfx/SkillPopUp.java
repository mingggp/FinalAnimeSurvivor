package vfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SkillPopUp {
    private double ScreenX;
    private double ScreenY;
    private Image image;
    private int size;
    private double age;
    private double lifeSpan;
    private double speed;

    public SkillPopUp(Image image){
        this.image = image;
        this.size = 512;
        this.age =0;
        this.ScreenX = 960;
        this.ScreenY = 1080;
        this.lifeSpan=1.2;//1.75
    }
    public void update(double accumulateDeltaTime){
        age+=accumulateDeltaTime;
        if(age<0.125){
            speed = 4600;
        }
        else {
            speed = 20;
        }
        ScreenY -= speed * accumulateDeltaTime;

    }
    public void render(GraphicsContext gc){
        if(age>1){
            gc.setGlobalAlpha((1.2-age)/0.2);
        }
        gc.drawImage(image,ScreenX-size/2.0,ScreenY-size/2.0,512,512);
        gc.setGlobalAlpha(1.0);
    }
    public boolean isExpired(){
        return age>=lifeSpan;
    }
}

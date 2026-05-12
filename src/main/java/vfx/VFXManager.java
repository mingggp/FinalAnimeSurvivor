package vfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class VFXManager {

    private static final ArrayList<DamageText> damageTexts = new ArrayList<>();
    private static final ArrayList<Slash> slashs = new ArrayList<>();
    private static final ArrayList<WorldEffect> groundEffects = new ArrayList<>();
    private static final ArrayList<WorldEffect> screenEffects = new ArrayList<>();
    private static final ArrayList<SkillPopUp> skillPopUps = new ArrayList<>();

    public static void updateGround(double accumulateDeltaTime,double playerX,double playerY){
        for(WorldEffect worldEffect : groundEffects){
            worldEffect.update(accumulateDeltaTime, playerX, playerY);
        }
        groundEffects.removeIf(WorldEffect::isExpired);
    }
    public static void update(double accumulateDeltaTime) {
        for (DamageText dt : damageTexts) {
            dt.update(accumulateDeltaTime);
        }
        /*Iterator<DamageText> damageTextIterator = damageTexts.iterator();
        while (damageTextIterator.hasNext()) {
            DamageText damageText = damageTextIterator.next();
            if (damageText.isExpired()) {
                damageTextIterator.remove();
            } else {
                damageText.update(accumulateDeltaTime);
            }
        }*/
        for(SkillPopUp skillPopUp: skillPopUps){
            skillPopUp.update(accumulateDeltaTime);
        }
        for (Slash slash : slashs){
            slash.update(accumulateDeltaTime);
        }
        damageTexts.removeIf(DamageText::isExpired);
        slashs.removeIf(Slash ::isExpired);
        skillPopUps.removeIf(SkillPopUp::isExpired);
    }
    public static void updateScreenEffect(double accumulateDeltaTime,double playerX,double playerY){
        for(WorldEffect worldEffect: screenEffects){
            worldEffect.update( accumulateDeltaTime, playerX, playerY);
        }
        screenEffects.removeIf(WorldEffect::isExpired);
    }
    public static void renderGround(GraphicsContext gc, double playerX,double playerY){
        for(WorldEffect worldEffect : groundEffects){
            worldEffect.render(gc,playerX,playerY);
        }
    }
    public static void render(GraphicsContext gc,double characterMapX,double characterMapY) {
        for (DamageText dt : damageTexts) {
            dt.render(gc,characterMapX,characterMapY);
        }/*
        Iterator<DamageText> damageTextIterator = damageTexts.iterator();
        while (damageTextIterator.hasNext()) {
             DamageText damageText = damageTextIterator.next();
             damageText.render(gc,deltaTime,characterMapX,characterMapY);
        }*/
        for (Slash slash : slashs){
            slash.render(gc,characterMapX,characterMapY);
        }
    }
    public static void renderScreen(GraphicsContext gc, double playerX,double playerY){
        for(WorldEffect worldEffect : screenEffects){
            worldEffect.render(gc,playerX,playerY);
        }
    }
    public static void renderPopup(GraphicsContext gc){
        for(SkillPopUp skillPopUp: skillPopUps){
            skillPopUp.render(gc);
        }
    }

    public static void spawnDamageText(double x, double y, double amount, Color color) {
        // Add random jitter so numbers don't perfectly overlap
        double offsetX = (Math.random() - 0.5) * 30;
        double offsetY = (Math.random() - 0.5) * 15;
        damageTexts.add(new DamageText(x + offsetX, y + offsetY, amount,color));
    }
    public static void spawnSlash(double x, double y,int size,boolean cleave){

        slashs.add(new Slash(x, y,size,Math.random()*360,cleave));

    }
    public static void setGroundEffects(Image image,double x,double y,double width,double height,double lifespan,boolean followCam ,double maxAlpha,double minAlpha){
        groundEffects.add(new WorldEffect(image, x, y,width, height,lifespan,followCam,maxAlpha,minAlpha ));
    }
    public static void setGroundEffects(Color color,double x,double y,double width,double height,double lifespan,boolean followCam ,double maxAlpha,double minAlpha){
        groundEffects.add(new WorldEffect(color, x, y,width, height,lifespan,followCam ,maxAlpha,minAlpha ));
    }
    public static void setScreenEffects(Image image,double x,double y,double width,double height,double lifespan,boolean followCam,double maxAlpha ,double minAlpha){
        screenEffects.add(new WorldEffect(image, x, y,width, height,lifespan,followCam ,maxAlpha,minAlpha ));
    }
    public static void setScreenEffects(Color color, double x, double y, double width, double height, double lifespan, boolean followCam,double maxAlpha,double minAlpha ){
        screenEffects.add(new WorldEffect(color, x, y,width, height,lifespan,followCam ,maxAlpha ,minAlpha));
    }
    public static void spawnSkillPopUp(Image image){
        skillPopUps.add(new SkillPopUp(image));
    }


    public static void clear() {
        damageTexts.clear();
        slashs.clear();
        groundEffects.clear();
        skillPopUps.clear();
        screenEffects.clear();
    }
}

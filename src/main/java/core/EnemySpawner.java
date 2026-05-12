package core;

import entity.enemy.Enemy;

import java.util.ArrayList;
import java.util.Random;

public class EnemySpawner {

    private final GameManager gameManager;
    private final Random random;
    private final ArrayList<Enemy> spawnableEnemyList;
    private double time;

    public EnemySpawner(GameManager gameManager) {
        this.gameManager = gameManager;
        this.random = new Random();
        this.spawnableEnemyList = new ArrayList<>();
        this.time = 0;
    }
    public void update(int level,double gameTimer,double accumulateDeltaTime){
        time += accumulateDeltaTime;
        double characterMapX = gameManager.getCharacter().getMapX();
        double characterMapY = gameManager.getCharacter().getMapY();

        //random x and y for enemy
        double ex,ey;


        if(random.nextBoolean()){
            ex = random.nextBoolean() ? characterMapX + 960 + Math.random()*200  : characterMapX - 960 - Math.random()*200 ;
            ey = characterMapY - 740 + Math.random()*1480;
        }
        else{
            ex = characterMapX - 1160 + Math.random()*2320;
            ey = random.nextBoolean() ? characterMapY + 540 + Math.random()*200  : characterMapY - 540 - Math.random()*200 ;
        }

        //enemy only spawn inside border
        if(ex<192) ex = 192;
        if(ey<192) ey = 192;
        if(ex>3648*2) ex = 3648*2;
        if(ey>2688*2) ey = 2688*2;



        //spawn enemy every five second for test
        if(level < 50 /*&& time > 0.5*/ && gameManager.getInGameEnemyList().size()<level*2){
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(),ex,ey));
            time -= 0.5;
        }
        if(level >= 50 /*&& time >0.3 */&& gameManager.getInGameEnemyList().size()<100){
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(),ex,ey));
            time -= 0.3;
        }
        if(level >= 100 /*&& time >0.15*/ && gameManager.getInGameEnemyList().size()<175){
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(),ex,ey));
            time -= 0.15;
        }
        if(level >= 120 && gameManager.getInGameEnemyList().size()<250){
            gameManager.addEnemy(new Enemy(spawnableEnemyList.getFirst(),ex,ey));
            time=0;
        }




    }
    public void addEnemyToSpawner(Enemy enemy){
        spawnableEnemyList.add(enemy);
    }
    public void resetEnemyInSpawner(){
        spawnableEnemyList.clear();
        time = 0;
    }
}


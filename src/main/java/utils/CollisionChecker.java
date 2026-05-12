package utils;

import entity.character.Character;
import entity.enemy.Enemy;
import tile.Tile;


public class CollisionChecker {

    private static int[][] mapTilesNum;
    private static Tile[] tiles;

    public static void setTile(int[][] mapTilesNum, Tile[] tiles){
        CollisionChecker.mapTilesNum = mapTilesNum;
        CollisionChecker.tiles = tiles;

    }

    public static void checkTileCollision(Character character, double accumulateDeltaTime){


        if (mapTilesNum == null || tiles == null) return;

        double leftHitboxX = (character.getMapX() - character.getWidth()  /2.0    );
        double RightHitboxX = (character.getMapX() + character.getWidth()  /2.0);
        double TopHitboxY =  (character.getMapY());
        double BottomHitboxY = (character.getMapY() + character.getHeight());

        int entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX )/96)));
        int entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX )/96)));
        int entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY )/96)));
        int entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY )/96)));

        int tileNum1,tileNum2;

        if(character.getDy() < 0 ){
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY - character.getSpeed()*accumulateDeltaTime)/96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityTopRow];
            if(tiles[tileNum1].collision || tiles[tileNum2].collision){
                character.setDy(0);
            }
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY )/96)));
        }
        else if(character.getDy() > 0 ){
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY + character.getSpeed()*accumulateDeltaTime)/96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityBottomRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if(tiles[tileNum1].collision || tiles[tileNum2].collision){
                character.setDy(0);
            }
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY )/96)));
        }
        if(character.getDx() < 0 ){
            entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX - character.getSpeed()*accumulateDeltaTime)/96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityLeftCol][entityBottomRow];
            if(tiles[tileNum1].collision || tiles[tileNum2].collision){
                character.setDx(0);
            }
        }
        else if(character.getDx() > 0 ){
            entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX + character.getSpeed()*accumulateDeltaTime)/96)));
            tileNum1 = mapTilesNum[entityRightCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if(tiles[tileNum1].collision || tiles[tileNum2].collision){
                character.setDx(0);
            }
        }

    }
    public static void checkTileCollision(Enemy enemy, double accumulateDeltaTime) {

        if (mapTilesNum == null || tiles == null) return;

        double leftHitboxX = (enemy.getMapX() - enemy.getWidth() / 2.0);
        double RightHitboxX = (enemy.getMapX() + enemy.getWidth() / 2.0);
        double TopHitboxY = (enemy.getMapY() - enemy.getHeight() /2.0);
        double BottomHitboxY = (enemy.getMapY() + enemy.getHeight() /2.0);

        int entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX) / 96)));
        int entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX) / 96)));
        int entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY) / 96)));
        int entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY) / 96)));

        int tileNum1, tileNum2;

        if (enemy.getDy() < 0) {
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY - enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityTopRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDy(0);
            }
            entityTopRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((TopHitboxY) / 96)));
        } else if (enemy.getDy() > 0) {
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY + enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityBottomRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDy(0);
            }
            entityBottomRow = Math.max(0, Math.min(mapTilesNum[0].length - 1, (int) ((BottomHitboxY) / 96)));
        }
        if (enemy.getDx() < 0) {
            entityLeftCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((leftHitboxX - enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityLeftCol][entityTopRow];
            tileNum2 = mapTilesNum[entityLeftCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDx(0);
            }
        } else if (enemy.getDx() > 0) {
            entityRightCol = Math.max(0, Math.min(mapTilesNum.length - 1, (int) ((RightHitboxX + enemy.getSpeed() * accumulateDeltaTime) / 96)));
            tileNum1 = mapTilesNum[entityRightCol][entityTopRow];
            tileNum2 = mapTilesNum[entityRightCol][entityBottomRow];
            if (tiles[tileNum1].collision || tiles[tileNum2].collision) {
                enemy.setDx(0);
            }
        }
    }
}

package tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;

public class TileManager {

    public Tile[] tiles;
    public int[][] mapTilesNum;
    int tileSize;
    GraphicsContext gc;
    public TileManager(GraphicsContext gc,int tileSize){
        tiles = new Tile[2];
        mapTilesNum = new int[80][60];
        this.gc = gc;
        this.tileSize = tileSize;
        getTileImage();
    }
    public void getTileImage(){
        tiles[0] = new Tile();
        tiles[0].image = new Image("tile/dirt.png");
        tiles[0].collision = true;
        tiles[1] = new Tile();
        tiles[1].image = new Image("tile/grass.png");
    }
    public void loadMap(){
        try{
            InputStream is = getClass().getResourceAsStream("/map/map1.txt");
            assert is != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            for(int j = 0; j < 60; j++) {
                String line = reader.readLine();
                for (int i = 0; i < 80; i++) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[i]);
                    mapTilesNum[i][j] = num;
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
        }
        catch (IOException e){
            System.out.println("something wrong");
        }


    }

    public void render(double characterMapX,double characterMapY){

        for(int i = 0 ; i < 80 ; i++){
            for(int j = 0 ; j < 60 ; j++){

                int mapX = i*tileSize;
                int mapY = j*tileSize;
                double screenX = mapX - characterMapX + 960;
                double screenY = mapY - characterMapY + 540;

                if(mapX > characterMapX-960-96 && mapX < characterMapX+960 && mapY > characterMapY-540-96 && mapY < characterMapY+540) {
                    gc.drawImage(tiles[mapTilesNum[i][j]].image, screenX, screenY);

                }
            }
        }
    }
}

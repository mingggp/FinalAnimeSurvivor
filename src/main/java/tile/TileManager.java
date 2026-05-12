package tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.*;

/**
 * Manages the tile map: loading tile archetypes, reading the map layout from a
 * text file, and rendering the visible portion of the map each frame.
 *
 * <h2>Tile data</h2>
 * <p>{@link #tiles} holds the small set of unique tile archetypes (e.g. dirt,
 * grass).  {@link #mapTilesNum} is an 80 × 60 grid of indices into that array,
 * one entry per tile cell.  The grid is indexed as {@code [col][row]} where
 * (0, 0) is the top-left corner.
 *
 * <h2>Map file format</h2>
 * <p>{@code /map/map1.txt} contains 60 rows × 80 space-separated integers.
 * Each integer is an index into {@link #tiles}.
 */
public class TileManager {

    /**
     * Array of tile archetypes.  Each element defines the sprite and whether
     * that tile type blocks movement.  Currently two types:
     * index 0 = dirt (solid wall), index 1 = grass (walkable).
     */
    public Tile[] tiles;

    /**
     * Tile-type index grid: {@code mapTilesNum[col][row]} holds the index
     * into {@link #tiles} for that map cell.  Dimensions: 80 columns × 60 rows.
     */
    public int[][] mapTilesNum;

    /** Rendered tile size in pixels (96). */
    int tileSize;

    /** Graphics context used to draw tile sprites. */
    GraphicsContext gc;

    /**
     * Constructs the manager, allocates the tile array and map grid, and
     * pre-loads tile images.
     *
     * @param gc       graphics context for rendering
     * @param tileSize tile render size in pixels (should be 96)
     */
    public TileManager(GraphicsContext gc, int tileSize) {
        tiles = new Tile[2];
        mapTilesNum = new int[80][60];
        this.gc = gc;
        this.tileSize = tileSize;
        getTileImage();
    }
    public void getTileImage(){
        tiles[0] = new Tile();
        tiles[0].image = utils.SpriteManager.loadImage("tile/dirt.png");
        tiles[0].collision = true;
        tiles[1] = new Tile();
        tiles[1].image = utils.SpriteManager.loadImage("tile/grass.png");
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

package tile;

import StartMenu.MainMenuTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TileManager {
    MainMenuTest mainMenuTest;
    Tiles[] tile;

    public TileManager(MainMenuTest mainMenuTest){

        this.mainMenuTest = mainMenuTest;

        tile = new Tiles[2];
        getTileImage();

    }

    public void getTileImage(){
        try{
            tile[0] = new Tiles();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resTiles/tiles.png")));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        g2.drawImage(tile[0].image,0,0,100,100,null);
    }
}

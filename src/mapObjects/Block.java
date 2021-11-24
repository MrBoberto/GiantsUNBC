package mapObjects;

import eye_candy.BlockShadow;
import game.Controller;
import game.GameObject;
import game.World;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Block extends GameObject {

    BlockShadow shadow;
    public Block(double x, double y) {
        super(x, y);

        Random random = new Random();
        String path = "/resources/Textures/blocks/block_texture_" + (random.nextInt(7)+1) + ".png";

        try {
            texture =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        shadow = new BlockShadow(x,y);
    }

    @Override
    public void tick() {
        /* empty */
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture, (int) x, (int) y, Controller.GRID_SIZE,Controller.GRID_SIZE, World.controller);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y,Controller.GRID_SIZE,Controller.GRID_SIZE);
    }

    public Rectangle getShadowBounds(){
        return new Rectangle((int) x + Controller.GRID_SIZE/2,
                (int) y - Controller.GRID_SIZE/2,
                Controller.GRID_SIZE,
                Controller.GRID_SIZE);
    }
}

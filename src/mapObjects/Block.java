package mapObjects;

import game.Controller;
import game.GameObject;
import game.World;
import utilities.BufferedImageLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Block extends GameObject {

    BufferedImage texture;
    public Block(double x, double y) {
        super(x, y);

        try {
            texture =  ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/resTiles/brickwall.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture, (int) x, (int) y, Controller.GRID_SIZE,Controller.GRID_SIZE, World.controller);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y,Controller.GRID_SIZE,Controller.GRID_SIZE);
    }
}

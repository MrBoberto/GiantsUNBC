package map_objects;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A block that interrupts movement of players and causes bullets to despawn or detonate by setting their velocities
 * to 0.
 *
 * @author The Boyz
 * @version 1
 */

import eye_candy.BlockShadow;
import game.Controller;
import game.GameObject;
import game.World;
import utilities.BufferedImageLoader;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Block extends GameObject {

    public Block(double x, double y) {
        super(x, y);

        Random random = new Random();
        String path = "/resources/Textures/blocks/sci-fi_texture (" + (random.nextInt(5)+1) + ").png";
        texture = BufferedImageLoader.loadImage(path);
        new BlockShadow(x,y);
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

package game;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * All entities in the game extend GameObject. GameObject keeps track of x and y coordinates, angle the entity is
 * facing, the horizontal and vertical velocities of the entity, and, in some cases, the current frame being displayed
 * by the entity.
 *
 * @author The Boyz
 * @version 1
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public abstract class GameObject implements Serializable {
    // The precise position of the object, for use with physics
    protected double x, y, angle;
    private double velX = 0;
    private double velY = 0;
    transient protected BufferedImage texture;

    public GameObject(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;


    }

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = 0;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (texture == null) {
            return;
        }
        out.writeInt(1); // how many images are serialized?
        ImageIO.write(texture, "png", out); // png is lossless
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (texture != null) texture = ImageIO.read(in);
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract Rectangle getBounds();

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }


}

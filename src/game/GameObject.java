package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class GameObject {
    // The precise position of the object, for use with physics
    protected double x, y, angle;
    protected double velX = 0;
    protected double velY = 0;
    transient protected BufferedImage texture;

    public abstract void tick();
    public abstract void render(Graphics g);
    public Rectangle getBounds() {
        if(texture != null){
            return  new Rectangle(
                    (int)x - texture.getWidth() / 2,
                    (int)y - texture.getHeight() / 2,
                    texture.getWidth(),
                    texture.getHeight()
            );
        } else {
            System.out.println("ERROR");
            return null;
        }
    }

    public GameObject(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;

        Controller.gameObjects.add(this);
    }

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = 0;
    }

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

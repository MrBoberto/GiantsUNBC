package game;

import java.awt.*;
import java.awt.image.ImageObserver;

public abstract class GameObject {
    // The precise position of the object, for use with physics
    protected double x, y, angle;
    protected double velX = 0;
    protected double velY = 0;

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

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

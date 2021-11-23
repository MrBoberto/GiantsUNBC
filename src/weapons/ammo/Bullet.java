package weapons.ammo;

import game.Controller;
import game.GameObject;
import game.World;
import mapObjects.Block;
import player.MainPlayer;

import java.awt.*;

public abstract class Bullet extends GameObject implements Projectile {
    // The precise position of the object, for use with physics
    protected int ID;
    private static int nextID = 0;
    protected int playerIBelongToNumber;
    protected ProjectileType ProjectileTYPE = null;
    protected int damage;
    Rectangle boundRect;


    protected Bullet(double x, double y, double angle) {
        super(x, y, angle);
        ID = nextID++;
        Controller.movingAmmo.add(this);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
        if(x == 0 || y == 0){
            return;
        }

        if (World.pythHyp(velX, velY) >= Controller.FRICTION) {
            double newSpeed = World.pythHyp(velX, velY) - Controller.FRICTION;

            if (angle >= Math.PI / 2 || (angle < 0 && angle >= -Math.PI / 2)) {
//            System.out.print(", Negative, speed = " + weapon.getMOMENTUM() / MASS);
                velX = World.cosAdj(newSpeed, angle);
                velY = World.sinOpp(newSpeed, angle);
            } else {
//            System.out.print(", Positive, speed = " + weapon.getMOMENTUM() / MASS);
                velX = World.sinOpp(newSpeed, angle);
                velY = World.cosAdj(newSpeed, angle);
            }
        } else {
            velX = 0;
            velY = 0;
        }

        if(texture != null){
            boundRect = new Rectangle((int)x - texture.getWidth() / 2,
                    (int)y- texture.getHeight() / 2, texture.getWidth(),
                    texture.getHeight());
        }

        checkBlockCollision();
    }

    private void checkBlockCollision(){
        for (int i = 0; i < Controller.blocks.size(); i++) {
            Block block = Controller.blocks.get(i);

            if(getBounds() != null && getBounds().intersects(block.getBounds())){
                velX = 0;
                velY = 0;
            }
        }
    }


    public int getID(){
        return ID;
    }

    public boolean hasStopped() {
        return (velX == 0 && velY == 0);
    }

    public int getPlayerIBelongToNumber() {
        return playerIBelongToNumber;
    }

    public ProjectileType getTYPE() {
        return ProjectileTYPE;
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "x=" + x +
                ", y=" + y +
                ", ID=" + ID +
                ", playerIBelongTo=" + playerIBelongToNumber +
                ", TYPE=" + ProjectileTYPE +
                '}';
    }
    public int getDamage() {
        return damage;
    }

    @Override
    public Rectangle getBounds() {
        return boundRect;
    }
}

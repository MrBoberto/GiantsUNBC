package eye_candy;

import game.Controller;
import game.GameObject;

import java.awt.*;

public class BlockShadow extends GameObject {
    public BlockShadow(double x, double y) {
        super(x, y);
        Controller.eyeCandy.add(this);
    }

    @Override
    public void tick() {
        /* empty */
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0,0,0,64));

        //Main block shadow
        g.fillRect((int) x + Controller.GRID_SIZE/2,
                (int) y - Controller.GRID_SIZE/2,
                Controller.GRID_SIZE,
                Controller.GRID_SIZE);

        //Small shadows to make blocks pop out more
        g.fillRect((int) x ,
                (int) y+ Controller.GRID_SIZE ,
                Controller.GRID_SIZE,
                1);

        g.fillRect((int) x -1,
                (int) y ,
                1,
                Controller.GRID_SIZE);

        //Shadow connectors, avoid printing on top of other shadows
        boolean skipShadow1 = false;
        boolean skipShadow2 = false;

        for (int i = 0; i < Controller.blocks.size(); i++) {
            if(Controller.blocks.get(i).getShadowBounds().intersects(new Rectangle(
                    (int) x - Controller.GRID_SIZE/2,
                    (int) y - Controller.GRID_SIZE/2,
                    Controller.GRID_SIZE,
                    Controller.GRID_SIZE))){
                skipShadow1 =true;
            }
        }
        if(!skipShadow1) g.fillPolygon(new Polygon(
                new int[] {(int) x, (int) x + Controller.GRID_SIZE/2, (int) x + Controller.GRID_SIZE/2},
                new int[] {(int) y, (int) y, (int) y - Controller.GRID_SIZE/2},
                3));

        for (int i = 0; i < Controller.blocks.size(); i++) {
            if(Controller.blocks.get(i).getShadowBounds().intersects(new Rectangle(
                    (int) x + Controller.GRID_SIZE/2,
                    (int) y + Controller.GRID_SIZE/2,
                    Controller.GRID_SIZE,
                    Controller.GRID_SIZE))){
                skipShadow2 =true;
            }
        }
        if(!skipShadow2) g.fillPolygon(new Polygon(
                new int[] {(int) x + Controller.GRID_SIZE, (int) x + Controller.GRID_SIZE, (int) x + Controller.GRID_SIZE*3/2},
                new int[] {(int) y + Controller.GRID_SIZE/2, (int) y + Controller.GRID_SIZE, (int) y + Controller.GRID_SIZE/2},
                3));

    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}

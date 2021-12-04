package eye_candy;

import game.Controller;
import game.GameObject;
import game.SingleController;
import game.World;
import player.Player;
import utilities.BufferedImageLoader;

import java.awt.*;

public class DeathMark extends GameObject {

    final int playerNumber;
    public DeathMark(double x, double y, int playerNumber) {
        super(x, y);

        Controller.eyeCandy.add(this);

        this.playerNumber = playerNumber;
        loadImage();
    }

    private void loadImage() {
        if(playerNumber == Player.SERVER_PLAYER){
            texture = BufferedImageLoader.loadImage("/resources/Textures/eye_candy/death_mark_blue.png");
        } else if (World.controller instanceof SingleController) {
            texture = BufferedImageLoader.loadImage("/resources/Textures/eye_candy/death_mark_thanos.png");
        } else {
            texture = BufferedImageLoader.loadImage("/resources/Textures/eye_candy/death_mark_red.png");
        }
    }

    @Override
    public void tick() {
        /* empty */
    }

    @Override
    public void render(Graphics g) {
        if(texture == null){
            loadImage();
        }
        if (texture != null){
            g.drawImage(texture, (int)x - Controller.GRID_SIZE/4, (int)y - Controller.GRID_SIZE/4, Controller.GRID_SIZE/2, Controller.GRID_SIZE/2, World.controller);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x,(int)y,texture.getWidth(),texture.getHeight());
    }
}

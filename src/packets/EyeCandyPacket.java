package packets;

import game.GameObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EyeCandyPacket implements Serializable {
    GameObject[] eyeCandy;

    public EyeCandyPacket(GameObject[] eyeCandy){
        this.eyeCandy = eyeCandy;
    }

    public GameObject[] getEyeCandy() {
        return eyeCandy;
    }
}

package packets;

import weapons.ammo.Bullet;

import java.io.Serializable;

public class ServerBulletPacket implements Serializable {
Bullet[] bullets;

    public ServerBulletPacket(Bullet[] bullets) {
        this.bullets = bullets;
    }

    public Bullet[] getAmmo() {
        return bullets;
    }
}

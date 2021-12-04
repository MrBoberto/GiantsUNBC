package packets;

import weapons.ammo.Bullet;

import java.io.Serializable;

public record ServerBulletPacket(Bullet[] bullets) implements Serializable {

    public Bullet[] getAmmo() {
        return bullets;
    }
}

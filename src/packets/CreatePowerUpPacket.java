package packets;

import power_ups.PowerUp;

import java.io.Serializable;

public class CreatePowerUpPacket implements Serializable {
    private final int x;
    private final int y;
    private final PowerUp.PowerUpType powerUpType;

    public CreatePowerUpPacket(int x, int y, PowerUp.PowerUpType powerUpType) {
        this.x = x;
        this.y = y;
        this.powerUpType = powerUpType;
    }

    public PowerUp.PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

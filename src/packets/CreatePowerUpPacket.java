package packets;

import power_ups.PowerUp;

import java.io.Serializable;

public record CreatePowerUpPacket(int x, int y, PowerUp.PowerUpType powerUpType) implements Serializable {}

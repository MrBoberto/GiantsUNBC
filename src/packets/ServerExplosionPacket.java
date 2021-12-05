package packets;

import java.io.Serializable;

public record ServerExplosionPacket(double x, double y, int playerNumber) implements Serializable {}

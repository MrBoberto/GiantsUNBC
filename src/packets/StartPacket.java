package packets;

import java.io.Serializable;

public record StartPacket(double x, double y, double angle, String playerName,
                          int mapSelected) implements Serializable {}

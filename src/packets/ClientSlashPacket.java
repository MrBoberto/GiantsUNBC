package packets;

import java.io.Serializable;

public record ClientSlashPacket(double x, double y, double angle, boolean isLeft, int damage) implements Serializable {}

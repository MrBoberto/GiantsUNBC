package packets;

import java.io.Serializable;

public record DeathCountPacket(int serverDeaths, int clientDeaths) implements Serializable {}

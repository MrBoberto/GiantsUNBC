package packets;

import game.GameObject;

import java.io.Serializable;

public record EyeCandyPacket(GameObject[] eyeCandy) implements Serializable {}

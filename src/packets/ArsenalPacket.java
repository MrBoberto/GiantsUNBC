package packets;

import weapons.guns.Weapon;

import java.io.Serializable;

public record ArsenalPacket(Weapon.WeaponType primary, Weapon.WeaponType secondary, int selected,
                            Weapon.WeaponType[] inventory) implements Serializable{}

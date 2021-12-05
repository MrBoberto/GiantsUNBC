package packets;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * A packet that informs a player of the current loadout of the other
 *
 * @author The Boyz
 * @version 1
 */

import weapons.guns.Weapon;

import java.io.Serializable;

public record ArsenalPacket(Weapon.WeaponType primary, Weapon.WeaponType secondary, int selected,
                            Weapon.WeaponType[] inventory) implements Serializable{}

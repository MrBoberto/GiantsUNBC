package player;

import weapons.Weapon;

import java.util.ArrayList;

public class Arsenal {
    // Default selected weapon(s) on startup
    private Weapon primary;
    private Weapon secondary;
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

    public void add(Weapon weapon) {
        if (primary == null) {
            primary = weapon;
        } else if (secondary == null) {
            secondary = weapon;
        } else {
            weapons.add(weapon);
        }
    }

    public Weapon remove(Weapon weapon) {
        if (weapon.getSERIAL() == primary.getSERIAL()) {
            return null;
        } else if (weapon.getSERIAL() == secondary.getSERIAL()) {
            return null;
        } else {
            for (int i = 0; i < weapons.size(); i++) {
                if (weapons.get(i).getSERIAL() == weapon.getSERIAL()) {
                    weapons.remove(i);
                    return weapon;
                }
            }
            return null;
        }
    }

    public void setPrimary(Weapon weapon) {
        if (weapon.getSERIAL() == primary.getSERIAL()) {
            // Do nothing
        } else if (weapon.getSERIAL() == secondary.getSERIAL()) {
            // Switch primary and secondary
            secondary = primary;
            primary = weapon;
            return;
        } else {
            for (int i = 0; i < weapons.size(); i++) {
                if (weapons.get(i).getSERIAL() == weapon.getSERIAL()) {
                    weapons.add(primary);
                    primary = weapon;
                    return;
                }
            }
        }
        System.out.println("weapons.Weapon " + weapon.getSERIAL() + " not found.");
    }

    public void setSecondary(Weapon weapon) {
        if (weapon.getSERIAL() == secondary.getSERIAL()) {
            // Do nothing
        } else if (weapon.getSERIAL() == primary.getSERIAL()) {
            // Switch primary and secondary
            primary = secondary;
            secondary = weapon;
            return;
        } else {
            for (int i = 0; i < weapons.size(); i++) {
                if (weapons.get(i).getSERIAL() == weapon.getSERIAL()) {
                    weapons.add(secondary);
                    secondary = weapon;
                    return;
                }
            }
        }
        System.out.println("weapons.Weapon " + weapon.getSERIAL() + " not found.");
    }

    public Weapon getPrimary() {
        return primary;
    }

    public Weapon getSecondary() {
        return secondary;
    }
}

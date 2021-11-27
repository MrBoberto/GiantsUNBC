package player;

import weapons.guns.Weapon;

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
        System.out.println("weapons.guns.Weapon " + weapon.getSERIAL() + " not found.");
    }

    public void setPrimary(int inventoryNum) {
        System.out.println("setPrimary");
        if (inventoryNum >= 1 && weapons.size() >= inventoryNum) {
            if (weapons.get(inventoryNum - 1).getSERIAL() == primary.getSERIAL()) {
                // Do nothing
                return;
            } else if (weapons.get(inventoryNum - 1).getSERIAL() == secondary.getSERIAL()) {
                // Switch primary and secondary
                secondary = primary;
                primary = weapons.get(inventoryNum - 1);
                return;
            } else {
                weapons.add(primary);
                primary = weapons.get(inventoryNum - 1);
                weapons.remove(inventoryNum - 1);
                return;
            }
        }
        System.out.println("Weapon inventory index " + inventoryNum + " DNE.");
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
        System.out.println("weapons.guns.Weapon " + weapon.getSERIAL() + " not found.");
    }

    public void setSecondary(int inventoryNum) {
        System.out.println("setSecondary");
        if (inventoryNum >= 1 && weapons.size() >= inventoryNum) {
            if (weapons.get(inventoryNum - 1).getSERIAL() == secondary.getSERIAL()) {
                // Do nothing
                return;
            } else if (weapons.get(inventoryNum - 1).getSERIAL() == primary.getSERIAL()) {
                // Switch primary and secondary
                primary = secondary;
                secondary = weapons.get(inventoryNum - 1);
                return;
            } else {
                weapons.add(secondary);
                secondary = weapons.get(inventoryNum - 1);
                weapons.remove(inventoryNum - 1);
                return;
            }
        }
        System.out.println("Weapon inventory index " + inventoryNum + " DNE.");
    }

    public Weapon getPrimary() {
        return primary;
    }

    public Weapon getSecondary() {
        return secondary;
    }

    public Weapon get(int index) {
        return weapons.get(index);
    }

    public int size() {
        return weapons.size();
    }

    public void clear() {
        primary = null;
        secondary = null;
        for (int i = 0; i < weapons.size(); i++) {
            weapons.remove(0);
        }
    }

    public boolean hasWeapon(int SERIAL) {
        if (primary.getSERIAL() == SERIAL || secondary.getSERIAL() == SERIAL) {
            return true;
        } else {
            for (int i = 0; i < weapons.size(); i++) {
                if (weapons.get(i).getSERIAL() == SERIAL) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String toString() {
        String string = "Primary: " + primary + ", Secondary: " + secondary + ",\nInventory: {";
        for (int i = 0; i < weapons.size(); i++) {
            string += weapons.get(i);
            if (i < weapons.size() - 1) {
                string += ", ";
            }
            else {
                string += "}";
            }
        }
        return string;

    }
}

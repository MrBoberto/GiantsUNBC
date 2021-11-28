package player;

import game.*;
import utilities.BufferedImageLoader;
import weapons.guns.Pistol;
import weapons.guns.Weapon;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Arsenal extends GameObject {
    // Default selected weapon(s) on startup
    private Weapon primary;
    private Weapon secondary;
    private Player playerIBelongTo;
    private ArrayList<Weapon> weapons = new ArrayList<Weapon>();

    //Textures
    BufferedImage shadow;
    BufferedImage inventorySlots;
    BufferedImage profile;

    public Arsenal(double x, double y, Player playerIBelongTo) {
        super(x, y - 35);
        this.playerIBelongTo = playerIBelongTo;
        Controller.arsenals.add(this);

        add(new Pistol(playerIBelongTo));

        System.out.println(playerIBelongTo.getClass().toString() + " + " + playerIBelongTo.getPlayerNumber());
    }

    @Override
    public void tick() {
        /* empty */
    }

    @Override
    public void render(Graphics g) {
        if(shadow == null){
            loadImages();
        }
        Graphics2D g2d = (Graphics2D) g;
        if(playerIBelongTo.getPlayerNumber()== Player.SERVER_PLAYER){
            g2d.drawImage(shadow, (int) x, (int) y+15, shadow.getWidth(), shadow.getHeight(), World.controller);
            g2d.drawImage(inventorySlots, (int) x , (int) y, inventorySlots.getWidth(), inventorySlots.getHeight(), World.controller);
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.75f));
            g2d.drawImage(profile, (int) x , (int) y, inventorySlots.getHeight(),inventorySlots.getHeight(), World.controller);
            } else {
            int offset = 5;
            g2d.drawImage(shadow, (int) x - offset, (int) y+15, shadow.getWidth(), shadow.getHeight(), World.controller);
            g2d.drawImage(inventorySlots, (int) x - offset, (int) y, inventorySlots.getWidth(), inventorySlots.getHeight(), World.controller);
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.75f));
            g2d.drawImage(profile, (int) x - offset+ inventorySlots.getWidth() - 100, (int) y, inventorySlots.getHeight(),inventorySlots.getHeight(), World.controller);
        }
    }

    private void loadImages() {

        if(playerIBelongTo.getPlayerNumber()== Player.SERVER_PLAYER) {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_shadow_gui_left_corner.png");
            inventorySlots = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/inventory_slots_left.png");
            profile = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_blue.png");
        } else {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_shadow_gui_right_corner.png");
            inventorySlots = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/inventory_slots_right.png");
            profile = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_red.png");
        }

    }

    @Override
    public Rectangle getBounds() {
        /* no bounds */
        return null;
    }

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
                Weapon newWeaponsArray[] = new Weapon[weapons.size()];
                for (int i = 0; i < inventoryNum - 1; i++) {
                    newWeaponsArray[i] = weapons.get(i);
                }
                newWeaponsArray[inventoryNum - 1] = primary;
                primary = weapons.get(inventoryNum - 1);
                weapons.remove(inventoryNum - 1);
                for (int i = inventoryNum; i <= weapons.size(); i++) {
                    newWeaponsArray[i] = weapons.get(i - 1);
                }

                weapons.clear();
                for (int i = 0; i < newWeaponsArray.length; i++) {
                    weapons.add(newWeaponsArray[i]);
                }

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
                Weapon newWeaponsArray[] = new Weapon[weapons.size()];
                for (int i = 0; i < inventoryNum - 1; i++) {
                    newWeaponsArray[i] = weapons.get(i);
                }
                newWeaponsArray[inventoryNum - 1] = secondary;
                secondary = weapons.get(inventoryNum - 1);
                weapons.remove(inventoryNum - 1);
                for (int i = inventoryNum; i <= weapons.size(); i++) {
                    newWeaponsArray[i] = weapons.get(i - 1);
                }

                weapons.clear();
                for (int i = 0; i < newWeaponsArray.length; i++) {
                    weapons.add(newWeaponsArray[i]);
                }

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
        if (primary.getSERIAL() == SERIAL || (secondary != null && secondary.getSERIAL() == SERIAL)) {
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

package player;

import game.*;
import power_ups.*;
import utilities.BufferedImageLoader;
import weapons.guns.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Arsenal extends GameObject {
    // Default selected weapon(s) on startup
    private Weapon primary;
    private Weapon secondary;
    private final Player playerIBelongTo;
    private final ArrayList<Weapon> weapons = new ArrayList<Weapon>();

    //Textures
    BufferedImage shadow;
    BufferedImage inventorySlots;
    BufferedImage profile;
    BufferedImage[] weaponTextures;

    //PowerUps
    private final PowerUp[] cosmeticPowerUps;
    private static final int HIDDEN_POS = -50;

    public Arsenal(double x, double y, Player playerIBelongTo) {
        super(x, y - 35);
        this.playerIBelongTo = playerIBelongTo;
        Controller.arsenals.add(this);
        loadGunTextures();
        add(new Pistol(playerIBelongTo));

        cosmeticPowerUps = new PowerUp[PowerUp.PowerUpType.values().length];

        cosmeticPowerUps[0] = new DamageUp(HIDDEN_POS, HIDDEN_POS,-1);
        cosmeticPowerUps[1] = new DamageDown(HIDDEN_POS, HIDDEN_POS,-1);
        cosmeticPowerUps[2] = new SpeedUp(HIDDEN_POS, HIDDEN_POS,-1);
        cosmeticPowerUps[3] = new SpeedDown(HIDDEN_POS, HIDDEN_POS,-1);
        cosmeticPowerUps[4] = new Ricochet(HIDDEN_POS, HIDDEN_POS,-1);

        for(PowerUp powerUp: cosmeticPowerUps){
            powerUp.setCosmetic(true);
        }
    }

    @Override
    public void tick() {
        for(PowerUp powerUp: cosmeticPowerUps){
            powerUp.tick();
        }
    }

    @Override
    public void render(Graphics g) {
        if(shadow == null){
            loadImages();
        }

        Graphics2D g2d = (Graphics2D) g;
        if(playerIBelongTo.getPlayerNumber()== Player.SERVER_PLAYER){

            //Inventory slots
            g2d.drawImage(shadow, (int) x, (int) y+15, shadow.getWidth(), shadow.getHeight(), World.controller);
            g2d.drawImage(inventorySlots, (int) x , (int) y, inventorySlots.getWidth(), inventorySlots.getHeight(), World.controller);

            //Player image
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.75f));
            g2d.drawImage(profile, (int) x , (int) y, inventorySlots.getHeight(),inventorySlots.getHeight(), World.controller);

            //Primary && secondary
            if(playerIBelongTo.getSelectedWeapon() == Player.PRIMARY_WEAPON) {
                g2d.drawImage(getWeaponTexture(primary), (int) x + 120, (int) y + 20, 65, 65, World.controller);
                g2d.drawImage(getWeaponTexture(secondary), (int) x + 197, (int) y + 46, 46, 46, World.controller);
            } else {
                g2d.drawImage(getWeaponTexture(secondary), (int) x + 120, (int) y + 20, 65, 65, World.controller);
                g2d.drawImage(getWeaponTexture(primary), (int) x + 197, (int) y + 46, 46, 46, World.controller);
            }

            //Rest of inventory
            for (int i = 0; i < weapons.size(); i++) {
                g2d.drawImage(getWeaponTexture(weapons.get(i)), (int) x + 250 + i * 52, (int) y + 35, 55, 55, World.controller);
            }

            //Powerups
            for(PowerUp powerUp: cosmeticPowerUps){
                powerUp.render(g2d);
            }
            for (int i = 0; i < playerIBelongTo.getPowerUps().length; i++) {
                getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i]).setX(x + 258 + i * 25);
                getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i]).setY(y);
            }

            for(PowerUp powerUp: getUnusedPowerUps()){
                powerUp.setX(HIDDEN_POS);
                powerUp.setY(HIDDEN_POS);
            }


            } else {
            int offset = 5;

            //Inventory slots
            g2d.drawImage(shadow, (int) 0, (int) y+15, shadow.getWidth(), shadow.getHeight(), World.controller);
            g2d.drawImage(inventorySlots, (int) x - offset, (int) y, inventorySlots.getWidth(), inventorySlots.getHeight(), World.controller);

            //Player image
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.75f));
            g2d.drawImage(profile, (int) x - offset+ inventorySlots.getWidth() - 100, (int) y, inventorySlots.getHeight(),inventorySlots.getHeight(), World.controller);

            //Primary && secondary
            if(playerIBelongTo.getSelectedWeapon() == Player.PRIMARY_WEAPON) {
                g2d.drawImage(getWeaponTexture(primary), (int) x + 275, (int) y + 20, 65, 65, World.controller);
                g2d.drawImage(getWeaponTexture(secondary), (int) x + 217, (int) y + 46, 46, 46, World.controller);
            } else {
                g2d.drawImage(getWeaponTexture(secondary), (int) x + 275, (int) y + 20, 65, 65, World.controller);
                g2d.drawImage(getWeaponTexture(primary), (int) x + 217, (int) y + 46, 46, 46, World.controller);
            }

            //Rest of inventory
            for (int i = 0; i < weapons.size(); i++) {
                g2d.drawImage(getWeaponTexture(weapons.get(i)), (int) x + i * 52, (int) y + 35, 55, 55, World.controller);
            }

            //Powerups
            for(PowerUp powerUp: cosmeticPowerUps){
                powerUp.render(g2d);
            }
            for (int i = 0; i < playerIBelongTo.getPowerUps().length; i++) {
                getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i]).setX(x + 173 - i * 25);
                getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i]).setY(y);
            }

            for(PowerUp powerUp: getUnusedPowerUps()){
                powerUp.setX(HIDDEN_POS);
                powerUp.setY(HIDDEN_POS);
            }
        }
    }

    private void loadGunTextures() {
        weaponTextures = new BufferedImage[Objects.requireNonNull(new File("src/resources/GUI/arsenal_slot").list()).length];
        for (int i = 0; i < weaponTextures.length; i++) {
            weaponTextures[i] = BufferedImageLoader.loadImage("/resources/GUI/arsenal_slot/arsenal (" + (i-1) + ").png");
        }
    }
    private PowerUp[] getUnusedPowerUps(){
        ArrayList<PowerUp> notPresent = new ArrayList<>(java.util.List.of(cosmeticPowerUps));
        for (int i = 0; i < playerIBelongTo.getPowerUps().length; i++) {
            notPresent.remove(getPowerUpCosmetic( playerIBelongTo.getPowerUps()[i]));
        }
        return notPresent.toArray(new PowerUp[0]);
    }

    private PowerUp getPowerUpCosmetic(PowerUp.PowerUpType type) {
        switch (type){
            case DamageUp -> {
                return cosmeticPowerUps[0];
            }
            case DamageDown -> {
                return cosmeticPowerUps[1];
            }
            case SpeedUp -> {
                return cosmeticPowerUps[2];
            }
            case SpeedDown -> {
                return cosmeticPowerUps[3];
            }
            case Ricochet -> {
                return cosmeticPowerUps[4];
            }
        }
        return null;
    }

    private BufferedImage getWeaponTexture(Weapon weapon) {
        if(weapon instanceof Shotgun){
            return weaponTextures[Shotgun.SERIAL +1];
        } else if (weapon instanceof SniperRifle){
            return weaponTextures[SniperRifle.SERIAL +1];
        }else if (weapon instanceof Pistol){
            return weaponTextures[Pistol.SERIAL +1];
        }else if (weapon instanceof AssaultRifle){
            return weaponTextures[AssaultRifle.SERIAL +1];
        }else if (weapon instanceof RocketLauncher){
            return weaponTextures[RocketLauncher.SERIAL +1];
        } else {
            return weaponTextures[0];
        }
    }

    private void loadImages() {

        if(playerIBelongTo.getPlayerNumber()== Player.SERVER_PLAYER) {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_shadow_gui_left_corner.png");
            inventorySlots = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/inventory_slots_left.png");
            profile = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_blue.png");


        } else {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_shadow_gui_left_corner.png");
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
        System.out.println("WEAPONS ARRAY: " + weapons);
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

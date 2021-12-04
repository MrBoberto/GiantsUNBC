package player;

import animation.ImageFrame;
import animation.ImageStrip;
import game.*;
import power_ups.*;
import utilities.BufferedImageLoader;
import weapons.guns.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static weapons.aoe.Explosion.buildImageStrip;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class Arsenal extends GameObject {
    // Default selected weapon(s) on startup
    private Weapon primary;
    private Weapon secondary;
    private final Player playerIBelongTo;
    private ArrayList<Weapon> weapons = new ArrayList<>();
    protected ImageStrip swordAnimationStrip;
    protected ImageFrame swordFrame;

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

        loadImageStrips();
        swordFrame = swordAnimationStrip.getHead();
        loadGunTextures();

        // Starting weapon(s)
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
            g2d.setColor(Color.BLUE);
            g2d.drawString(String.valueOf(Controller.PLAYER_LIVES - playerIBelongTo.getDeathCount()),(int) x + 90, (int) y - 10);


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

            //Power ups
            for(PowerUp powerUp: cosmeticPowerUps){
                powerUp.render(g2d);
            }
            for (int i = 0; i < playerIBelongTo.getPowerUps().length; i++) {
                Objects.requireNonNull(getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i])).setX(x + 258 + i * 25);
                Objects.requireNonNull(getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i])).setY(y);
            }

            for(PowerUp powerUp: getUnusedPowerUps()){
                powerUp.setX(HIDDEN_POS);
                powerUp.setY(HIDDEN_POS);
            }


            } else {
            int offset = 5;

            //Inventory slots
            g2d.drawImage(shadow, 0, (int) y+15, shadow.getWidth(), shadow.getHeight(), World.controller);
            g2d.drawImage(inventorySlots, (int) x - offset, (int) y, inventorySlots.getWidth(), inventorySlots.getHeight(), World.controller);

            //Player image
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.75f));
            g2d.drawImage(profile, (int) x - offset+ inventorySlots.getWidth() - 100, (int) y, inventorySlots.getHeight(),inventorySlots.getHeight(), World.controller);
            if(playerIBelongTo instanceof AIPlayer){
                g2d.setColor(Color.MAGENTA);
            } else {
                g2d.setColor(Color.RED);
            }

            g2d.drawString(String.valueOf(Controller.PLAYER_LIVES - playerIBelongTo.getDeathCount()),(int) x - offset+ inventorySlots.getWidth() - 100, (int) y - 10);

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

            //Power ups
            for(PowerUp powerUp: cosmeticPowerUps){
                powerUp.render(g2d);
            }
            for (int i = 0; i < playerIBelongTo.getPowerUps().length; i++) {
                Objects.requireNonNull(getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i])).setX(x + 173 - i * 25);
                Objects.requireNonNull(getPowerUpCosmetic(playerIBelongTo.getPowerUps()[i])).setY(y);
            }

            for(PowerUp powerUp: getUnusedPowerUps()){
                powerUp.setX(HIDDEN_POS);
                powerUp.setY(HIDDEN_POS);
            }
        }
    }

    private void loadGunTextures() {
        //weaponTextures = new BufferedImage[Objects.requireNonNull(new File("src/resources/GUI/arsenal_slot").list()).length];
        weaponTextures = new BufferedImage[6];
        for (int i = 0; i < weaponTextures.length; i++) {
            weaponTextures[i] = BufferedImageLoader.loadImage("/resources/GUI/arsenal_slot/arsenal (" + (i-1) + ").png");
        }
    }

    private void setCurrentSwordFrame() {
        swordFrame = swordFrame.getNext();
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
        } else if (weapon instanceof LightningSword){
            setCurrentSwordFrame();
            return swordFrame.getImage();
        }  else {
            return weaponTextures[0];
        }
    }

    private void loadImages() {

        if(playerIBelongTo.getPlayerNumber()== Player.SERVER_PLAYER) {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_light_gui_left_corner.png");
            inventorySlots = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/inventory_slots_left.png");
            profile = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_blue.png");

        } else if(playerIBelongTo instanceof AIPlayer) {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_light_gui_left_corner.png");
            inventorySlots = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/inventory_slots_right.png");
            profile = BufferedImageLoader.loadImage("/resources/GUI/character_closeups/character_closeup_thanos.png");

        } else {
            shadow = BufferedImageLoader.loadImage("/resources/GUI/in-game_gui/background_light_gui_left_corner.png");
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
                Weapon[] newWeaponsArray = new Weapon[weapons.size()];
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
                Collections.addAll(weapons, newWeaponsArray);

                return;
            }
        }
        System.out.println("Weapon inventory index " + inventoryNum + " DNE.");

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
                Weapon[] newWeaponsArray = new Weapon[weapons.size()];
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
                weapons.addAll(Arrays.asList(newWeaponsArray));

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

    public boolean lacksWeapon(int SERIAL) {
        if (primary.getSERIAL() == SERIAL || (secondary != null && secondary.getSERIAL() == SERIAL)) {
            return false;
        } else {
            for (Weapon weapon : weapons) {
                if (weapon.getSERIAL() == SERIAL) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Primary: " + primary + ", Secondary: " + secondary + ",\nInventory: {");
        for (int i = 0; i < weapons.size(); i++) {
            string.append(weapons.get(i));
            if (i < weapons.size() - 1) {
                string.append(", ");
            }
            else {
                string.append("}");
            }
        }
        return string.toString();

    }

    public void loadImageStrips() {
        ArrayList<String> imgLocStr = new ArrayList<>();
        String defLocStr;

        // Saves amount of text to be used
        if (playerIBelongTo.playerNumber == 0) {
            defLocStr = "/resources/GUI/sword/sword_blue (";
        } else if (playerIBelongTo instanceof AIPlayer) {
            defLocStr = "/resources/GUI/sword/sword_thanos (";
        } else {
            defLocStr = "/resources/GUI/sword/sword_red (";
        }

        // Builds image strip for respective sword colour
        for (int i = 1; i <= 4; i++) {
            imgLocStr.add(i + ").png");
        }
        swordAnimationStrip = buildImageStrip(imgLocStr, defLocStr);
//        System.out.println(standing.toString());
        imgLocStr.clear();
    }

    public Weapon.WeaponType[] getInventory() {
        Weapon.WeaponType[] weaponTypes = new Weapon.WeaponType[weapons.size()];
        for (int i = 0; i < weapons.size(); i++) {
            weaponTypes[i] = weapons.get(i).getWeaponType();
        }
        return weaponTypes;
    }

    public void setInventory(Weapon.WeaponType primary, Weapon.WeaponType secondary, int selected, Weapon.WeaponType[] inventory){
        switch (primary){
            case Shotgun -> this.primary = new Shotgun(playerIBelongTo);
            case SniperRifle -> this.primary = new SniperRifle(playerIBelongTo);
            case Pistol -> this.primary = new Pistol(playerIBelongTo);
            case AssaultRifle -> this.primary = new AssaultRifle(playerIBelongTo);
            case RocketLauncher -> this.primary = new RocketLauncher(playerIBelongTo);
            case LightningSword -> this.primary = new LightningSword(playerIBelongTo);
        }

        switch (secondary){
            case Shotgun -> this.secondary = new Shotgun(playerIBelongTo);
            case SniperRifle -> this.secondary = new SniperRifle(playerIBelongTo);
            case Pistol -> this.secondary = new Pistol(playerIBelongTo);
            case AssaultRifle -> this.secondary = new AssaultRifle(playerIBelongTo);
            case RocketLauncher -> this.secondary = new RocketLauncher(playerIBelongTo);
            case LightningSword -> this.secondary = new LightningSword(playerIBelongTo);
        }

        playerIBelongTo.setSelectedWeapon(selected);

        ArrayList<Weapon> newWeapons = new ArrayList<>();
        for (int i = 0; i < inventory.length; i++) {
            switch (inventory[i]){
                case Shotgun -> newWeapons.add(new Shotgun(playerIBelongTo));
                case SniperRifle -> newWeapons.add(new SniperRifle(playerIBelongTo));
                case Pistol -> newWeapons.add(new Pistol(playerIBelongTo));
                case AssaultRifle -> newWeapons.add(new AssaultRifle(playerIBelongTo));
                case RocketLauncher -> newWeapons.add(new RocketLauncher(playerIBelongTo));
                case LightningSword -> newWeapons.add(new LightningSword(playerIBelongTo));
            }
        }

        weapons = newWeapons;

    }
}

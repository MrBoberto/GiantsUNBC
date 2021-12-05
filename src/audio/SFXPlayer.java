package audio;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * Plays a sound effect file based upon the received integer value
 *
 * @author The Boyz
 * @version 1
 */

import game.Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SFXPlayer {
    Clip clip;
    AudioInputStream audioInputStream;
    protected static final int volume = 100;
    static FloatControl gainControl;
    static final double gain = 1;
    protected static float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);

    public void setFile(int fileInt) {
        String fileLocation;

        if (fileInt == 0) {
            fileLocation = "/resources/SFX/Shotgun.wav";
        } else if (fileInt == 1) {
            fileLocation = "/resources/SFX/Sniper Rifle.wav";
        } else if (fileInt == 2) {
            fileLocation = "/resources/SFX/Pistol.wav";
        } else if (fileInt == 3) {
            fileLocation = "/resources/SFX/Assault Rifle.wav";
        } else if (fileInt == 4) {
            fileLocation = "/resources/SFX/Rocket Launcher.wav";
        } else if (fileInt == 5) {
            fileLocation = "/resources/SFX/Slash.wav";
        } else if (fileInt == -1) {
            fileLocation = "/resources/SFX/Explosion1.wav";
        } else {
            fileLocation = "/resources/SFX/Click1.wav";
        }

        URL audioUrl = this.getClass().getResource(fileLocation);

        try {
            assert audioUrl != null;
            audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);

            // open audioInputStream to the clip
            clip.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = (double) Main.getVolumeSFX() / (double) 100;
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public void setVolume() {
        if (volume == Main.getVolumeSFX()) return;
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = (double) Main.getVolumeSFX() / (double) 100;
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}
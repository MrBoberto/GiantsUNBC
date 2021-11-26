package audio;

import game.Main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SFXPlayer {
    Clip clip;
    AudioInputStream audioInputStream;
    protected static int volume = 100;
    static FloatControl gainControl;
    static double gain = 1;
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
        } else {
            fileLocation = "/resources/SFX/Pistol.wav";
        }

        URL audioUrl = this.getClass().getResource(fileLocation);

        try {
            audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);

            // open audioInputStream to the clip
            clip.open(audioInputStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = (double) Main.getVolumeMusic() / (double) 100;
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public void setVolume(int volume) {
        if (volume == Main.getVolumeSFX()) return;
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = (double) Main.getVolumeMusic() / (double) 100;
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}
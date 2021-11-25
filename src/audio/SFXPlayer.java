package audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SFXPlayer {
    Clip clip;
    AudioInputStream audioInputStream;

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
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}
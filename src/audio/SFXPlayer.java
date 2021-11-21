package audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SFXPlayer {
    Clip clip;

    public void setFile(String fileLocation) {
        try {
            File file = new File(fileLocation);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        }
        catch (Exception e) {

        }
    }

    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }
}
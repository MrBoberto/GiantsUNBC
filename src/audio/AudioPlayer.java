package audio;

/**
 * This file is part of a solution to
 *		CPSC300 Term Project Fall 2021
 *
 * Plays music files
 *
 * @author The Boyz
 * @version 1
 */

import game.Main;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;

public class AudioPlayer {

    static Clip clip;

    static AudioInputStream audioInputStream;
    protected static final int volume = 100;
    static FloatControl gainControl;
    static final double gain = 1;
    protected static float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);

    /**
     * Constructor to initialize streams and clip.
     * @param filePath where to get the music files.
     */
    public AudioPlayer(String filePath) {
        // create AudioInputStream object
        URL audioUrl = this.getClass().getResource(filePath);

        try {
            assert audioUrl != null;
            audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
            DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
            clip = (Clip) AudioSystem.getLine(info);

            // open audioInputStream to the clip
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = (double) Main.getVolumeMusic() / (double) 100;
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    public static void setVolume() {
        if (volume == Main.getVolumeMusic()) return;
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = (double) Main.getVolumeMusic() / (double) 100;
        dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }

    /**
     * Play audio
     */
    public void play() {
        //start the clip
        clip.start();
    }

    /**
     * Stop audio
     */
    public void stop() {
        clip.stop();
        clip.close();
    }
}

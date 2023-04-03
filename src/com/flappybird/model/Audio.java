package com.flappybird.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Audio {
    private Clip clip;

    public Audio(String fileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/assets/song.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) {
            return;
        }
        stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        stop();
        clip.close();
    }

    public void setVolume(float volume) {
        if (clip != null && volume >= 0 && volume <= 1) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }


}

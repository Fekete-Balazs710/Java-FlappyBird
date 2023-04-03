package com.flappybird.view;

import com.flappybird.model.Audio;

import javax.swing.*;
import java.awt.*;

public class FlappyBird {

    // constants for the window width and height
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    // constructor for the main window of the game
    public FlappyBird(int width, int height, String title, Game game) {
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setTitle(title);

        // set the close operation and dimensions of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMaximumSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // make the window visible
        frame.setVisible(true);
        
    }

    public static void main(String[] args) {

        // create a new game and audio object
        Game game = new Game();
        Audio audio = new Audio("song.wav");

        // play the audio and set the volume to 75%
        audio.play();
        audio.setVolume(0.75F); //set the volume to 75%

        // create a new FlappyBird window
        FlappyBird window = new FlappyBird(WIDTH, HEIGHT, "Flappy Bird", game);

    }
}

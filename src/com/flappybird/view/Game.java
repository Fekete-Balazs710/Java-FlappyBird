package com.flappybird.view;

import com.flappybird.controller.Controller;
import com.flappybird.model.Bird;
import com.flappybird.model.Wall;
import com.flappybird.model.WallColumn;
import com.flappybird.model.proxy.ProxyImage;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Game extends JPanel implements ActionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    // flag to check if the game is running
    private boolean isRunning = false;

    private ProxyImage proxyImage;
    private Image background;
    private Bird bird;
    private WallColumn wallColumn;
    private int score;
    private int highScore;
    private int backgroundX;

    // audio clip for the collision sound effect
    private Clip collisionSound;

    public Game() {

        proxyImage = new ProxyImage("/assets/background.png");
        background = proxyImage.loadImage().getImage();
        setFocusable(true);
        setDoubleBuffered(false);
        addKeyListener(new GameKeyAdapter());
        Timer timer = new Timer(10, this);
        timer.start();

        // Read the high score from the text file
        File file = new File("D:/Egyetem/5_Felev/Java/Jatek/Flappy Bird/src/assets/highscore.txt");
        try (Scanner scanner = new Scanner(file)) {
            this.highScore = scanner.nextInt();
        } catch (FileNotFoundException e) {
            // Handle the exception
            System.out.println("Error: The file could not be found.");
        }

        // load the sound file - game over effect
        try {
            URL url = getClass().getResource("/assets/effect.wav");
            System.out.println(url);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            collisionSound = AudioSystem.getClip();
            collisionSound.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playCollisionSound() {
        // set the volume
        FloatControl volumeControl = (FloatControl) collisionSound.getControl(FloatControl.Type.MASTER_GAIN);
        float volume = 0.85f;  // 85% volume
        float range = volumeControl.getMaximum() - volumeControl.getMinimum();
        float gain = (range * volume) + volumeControl.getMinimum();
        volumeControl.setValue(gain);

        // play the sound
        collisionSound.setFramePosition(0);
        collisionSound.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().sync();
        if (isRunning) {

            // Update the game state
            bird.tick();
            wallColumn.tick();
            checkColision();
            this.score++;

            // Move the background image
            backgroundX -= 2;
        }

        // Repaint the screen
        repaint();
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;


        g.drawImage(background, backgroundX, 0, getWidth(), getHeight(), null);
        g.drawImage(background, backgroundX + getWidth(), 0, getWidth(), getHeight(), null);

        InputStream fontStream = getClass().getResourceAsStream("/assets/PixelEmulator.otf");
        Font pixelatedFont = null;

        try {
            pixelatedFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //moving background
        if (backgroundX + getWidth() <= 0) {
            backgroundX = 0;
        }

        if (isRunning) {

            this.bird.render(g2, this);
            this.wallColumn.render(g2, this);

            g2.setColor(Color.white);
            g.setFont(new Font("pixelatedFont", 1, 20));
            g.setFont(pixelatedFont);
            g2.drawString("Your score: " + this.wallColumn.getPoints(), 10, 20);


        } else {
            g2.setColor(Color.white);

            g.setFont(new Font("pixelatedFont", 1, 30));
            g.setFont(pixelatedFont.deriveFont(45f));
            g2.drawString("Flappy Bird", FlappyBird.WIDTH / 2 - 185, FlappyBird.HEIGHT / 2 - 45);

            g.setFont(pixelatedFont.deriveFont(20f));
            g2.drawString("<< Press Enter to Start the Game >>", FlappyBird.WIDTH / 2 - 270, FlappyBird.HEIGHT / 2);
            g2.setColor(Color.white);

        }

        g2.setColor(Color.white);
        g.setFont(new Font("pixelatedFont", 1, 20));
        g.setFont(pixelatedFont);
        g2.drawString("High Score: " + highScore, FlappyBird.WIDTH - 280, 20);

        g.dispose();
    }

    private void restartGame() {
        if (!isRunning) {
            this.isRunning = true;
            this.bird = new Bird(FlappyBird.WIDTH / 3, FlappyBird.HEIGHT / 2);
            this.wallColumn = new WallColumn();
        }
    }

    private void endGame() {
        this.isRunning = false;
        if (this.wallColumn.getPoints() > highScore) {
            this.highScore = this.wallColumn.getPoints();

            File file = new File("D:/Egyetem/5_Felev/Java/Jatek/Flappy Bird/src/assets/highscore.txt");

            // Delete the file
            if (file.delete()) {
                System.out.println("The file was deleted successfully.");
            } else {
                System.out.println("Error: The file could not be deleted.");
            }

            // Create a new empty file with the same name as the file that was deleted
            try {
                if (file.createNewFile()) {
                    System.out.println("The file was created successfully.");
                } else {
                    System.out.println("Error: The file could not be created.");
                }
            } catch (IOException e) {
                System.out.println("Error: An I/O exception occurred.");
            }

        }

        try (FileWriter fw = new FileWriter("D:/Egyetem/5_Felev/Java/Jatek/Flappy Bird/src/assets/highscore.txt");
             BufferedWriter bw = new BufferedWriter(fw)) {
            // Clear the contents of the file
            RandomAccessFile raf = new RandomAccessFile("D:/Egyetem/5_Felev/Java/Jatek/Flappy Bird/src/assets/highscore.txt", "rw");
            raf.setLength(0);
            raf.close();

            // Write the new text to the file
            bw.write(String.valueOf(highScore));
        } catch (IOException e) {
            System.out.println("Error.");
        }

        this.wallColumn.setPoints(0);

    }

    private void checkColision() {
        Rectangle rectBird = this.bird.getBounds();
        Rectangle rectWall;

        // Iterate through the list of walls
        for (int i = 0; i < this.wallColumn.getWall().size(); i++) {
            Wall tempWall = this.wallColumn.getWall().get(i);
            rectWall = tempWall.getBounds();

            // Check if the bird intersects with the wall
            if (rectBird.intersects(rectWall)) {
                 // If the bird collides with the wall, play the collision sound and end the game
                 playCollisionSound();
                 endGame();
                }
            }
        }


    // Key
    private class GameKeyAdapter extends KeyAdapter {

        private final Controller controller;

        public GameKeyAdapter() {
            controller = new Controller();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                restartGame();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (isRunning) {
                controller.controllerReleased(bird, e);
            }
        }
    }
}

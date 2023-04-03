
package com.flappybird.model;

import com.flappybird.view.FlappyBird;

import java.awt.*;

import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WallColumn {

    private int base = FlappyBird.HEIGHT - 60;

    private List<Wall> tubes;
    private Random random;
    private int points = 0;
    private int speed = 5;
    private int changeSpeed = speed;

    public WallColumn() {
        tubes = new ArrayList<>();
        random = new Random();
        initTubes();
    }

    private void initTubes() {

        int last = base;
        int randWay = random.nextInt(10);

        for (int i = 0; i < 20; i++) {

            Wall tempTube = new Wall(900, last);
            tempTube.setDx(speed);
            last = tempTube.getY() - tempTube.getHeight();
            if (i < randWay || i > randWay + 4) {
                tubes.add(tempTube);
            }

        }

    }

    public void tick() {

        for (int i = 0; i < tubes.size(); i++) {
            tubes.get(i).tick();

            if (tubes.get(i).getX() < 0) {
                tubes.remove(tubes.get(i));
            }
        }

        if (tubes.isEmpty()) {
            this.points += 1;
            if (changeSpeed == points) {
                this.speed += 1;
                changeSpeed += 4;
                System.out.println(speed);
                
            }
            initTubes();
        }

    }

    public void render(Graphics2D g, ImageObserver obs) {
        for (int i = 0; i < tubes.size(); i++) {
            tubes.get(i).render(g, obs);
        }

    }

    public List<Wall> getWall() {
        return tubes;
    }

    public void setTubes(List<Wall> tubes) {
        this.tubes = tubes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}

package com.flappybird.model;

import com.flappybird.model.proxy.ProxyImage;
import com.flappybird.view.FlappyBird;

import java.awt.*;
import java.awt.image.ImageObserver;

public class Bird extends GameObject {

    private ProxyImage proxyImage;
    private final Wall[] tube;

    public Bird(int x, int y){
        super(x, y);
        if(proxyImage == null) {
            proxyImage = new ProxyImage("/assets/bird.gif");
        }
        this.image = proxyImage.loadImage().getImage();

        // scale down the image
        int newWidth = image.getWidth(null) / 6;
        int newHeight = image.getHeight(null) / 6;
        this.image = image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);

        // update the width and height of the bird
        this.width = newWidth;
        this.height = newHeight;

        this.x -= width;
        this.y -= height;
        tube = new Wall[1];
        tube[0] = new Wall(900, FlappyBird.HEIGHT - 60);
        this.dy = 4;
    }
    @Override
    public void tick() {
        if(dy < 5) {
            dy += 1.5;
        }
        this.y += dy;
        tube[0].tick();
        checkWindowBorder();
    }
    public void jump() {
        if(dy > 0) {
            dy = 0;
        }
        dy -= 15;
    }

    public void fall() {
        if(dy > 0) {
            dy = 0;
        }
        dy += 10;
    }
    
    private void checkWindowBorder() {
        if(this.x > FlappyBird.WIDTH) {
            this.x = FlappyBird.WIDTH;
        }
        if(this.x < 0) {
            this.x = 0;
        }
        if(this.y > FlappyBird.HEIGHT - 50) {
            this.y = FlappyBird.HEIGHT - 50;
        }
        if(this.y < 0) {
            this.y = 0;
        }
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        g.drawImage(image, x, y, obs);
        tube[0].render(g, obs);
    }
    
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

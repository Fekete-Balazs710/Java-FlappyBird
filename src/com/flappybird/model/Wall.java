package com.flappybird.model;

import com.flappybird.model.proxy.ProxyImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class Wall extends GameObject {

    private ProxyImage proxyImage;
    public Wall(int x, int y) {
        super(x, y);
        if (proxyImage == null) {
            proxyImage = new ProxyImage("/assets/wall.png");

        }
        this.image = proxyImage.loadImage().getImage();
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
    }

    @Override
    public void tick() {
        this.x -= dx;
    }

    @Override
    public void render(Graphics2D g, ImageObserver obs) {
        g.drawImage(image, x, y, obs);

    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

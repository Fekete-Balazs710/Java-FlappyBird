package com.flappybird.controller;

import com.flappybird.model.Bird;
import java.awt.event.KeyEvent;


public class Controller implements IStrategy {

    @Override
    public void controller(Bird bird, KeyEvent kevent) {
    }

    @Override
    public void controllerReleased(Bird bird, KeyEvent kevent) {
        //SPACE and UP ARROW -> jump
        if(kevent.getKeyCode() == KeyEvent.VK_SPACE || kevent.getKeyCode() == KeyEvent.VK_UP) {
            bird.jump();
        }
        //DOWN ARROW -> fall
        if(kevent.getKeyCode() == KeyEvent.VK_DOWN) {
            bird.fall();
        }
    }
    
}

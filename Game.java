package frogger;

import java.awt.Canvas;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.sound.sampled.*;

public class Game extends Canvas implements Runnable{
    public static int WIDTH = 720;
    public static int HEIGHT = 92 * 3 - 46;
    private Thread thread;
    private boolean running = false;
    public Player player;
    private Handler handler;
    public int min_threshold = (92 * 3 - 48) - 17 * 8 + 92;
    public Game(){
        player = new Player(WIDTH/2 - 24, (92 * 3 - 55) - (17 * 6), ID.Player);
        Window my_window = new Window(WIDTH, HEIGHT, "Frogger", this, player);
        handler = new Handler();
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 5; j++){
                handler.addObject(new Road(155 * j, (92 * 3 - 50) - 17 * 8 - 92 * i, ID.Road));
            }
        }
        handler.addObject(player);
        for (GameObject i: handler.object){
            if (i != player){
                i.load_image("frogger/Images/Road.png");
            }
            else {
                i.load_image("frogger/Images/frog1.png");
            }
        }
        File music = new File("frogger/Audio/start.wav");
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(music);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true;
    }
    public synchronized void stop(){
        try {
            thread.join();
            running = false;
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running){
            if (player.getMovement()){
                for (GameObject i: handler.object){
                    if (i != player){
                        i.setY(i.getY() + 1);
                        if (i.getY() >= min_threshold){
                            i.setY((92 * 3 - 48) - 17 * 8 - 92 * 3);
                        }
                    }
                }
            }
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1){
                tick();
                delta --;
            }
            if (running){
                render();
            }
            frames++;
            if (System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick(){
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        handler.render(g);
        g.dispose();
        bs.show();
    }
    public static void main(String[] args){
        new Game();
    }
}
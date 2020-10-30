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
import java.lang.Math;

public class Game extends Canvas implements Runnable{
    public static int WIDTH = 720;
    public static int my_height = 92;
    public static int HEIGHT = 503;
    private Thread thread;
    private boolean running = false;
    public Player player;
    private Handler handler;
    public int min_threshold = my_height * 6;
    public BufferedImage image;
    public Clip clip;
    public boolean can_move = false;
    public AudioInputStream audioInput;
    public Game(){
        player = new Player(WIDTH/2 - 24, 400, ID.Player);
        Window my_window = new Window(WIDTH, HEIGHT, "Frogger", this, player);
        handler = new Handler();
        try {
            image = ImageIO.read(new File("frogger/Images/Road.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 5; j++){
                handler.addObject(new Road(155 * j, (-1 * (my_height * 2)) + my_height * i, ID.Road, Math.abs((-1 * (my_height * 2)) + my_height * i  - min_threshold)));
            }
        }
        handler.addObject(player);
        for (GameObject i: handler.object){
            if (i != player){
                i.inputImage = i.load_image("frogger/Images/Road.png"); 
            }
            else {
                for (int j = 1; j < 8; j++){
                    player.image_array[j - 1] = player.load_image("frogger/Images/frog" + Integer.toString(j) + ".png");
                }
                i.inputImage = i.image_array[0];
            }
        }
        File music = new File("frogger/Audio/start.wav");
        try {
            audioInput = AudioSystem.getAudioInputStream(music);
            clip = AudioSystem.getClip();
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
            if (can_move == false && player.getMovement()){
                can_move = true;
            }
            if (can_move && player.getMovement() == false){
                for (GameObject i: handler.object){
                    if (i != player){
                        if (i.getY() == min_threshold && i.how_many_moves == 0){
                            i.how_many_moves = min_threshold;
                            i.setY(0);
                        }
                    }
                }
            }
            if (player.getMovement()){
                if (player.moves_remaining == my_height){
                    clip.stop();
                    File music = new File("frogger/Audio/hop.wav");
                    try {
                        audioInput = AudioSystem.getAudioInputStream(music);
                        clip = AudioSystem.getClip();
                        clip.open(audioInput);
                        clip.start();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                for (GameObject i: handler.object){
                    if (i != player){
                        if (i.getY() == min_threshold && i.how_many_moves == 0){
                            i.how_many_moves = min_threshold;
                            i.setY(0);
                        }
                        else{
                            i.setY(i.getY() + 1);
                            i.how_many_moves -= 1;
                        }  
                    }
                }
                player.moves_remaining -= 1;
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
                // System.out.println("FPS: " + frames);
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
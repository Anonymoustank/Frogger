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
    public BufferedImage image, car_image0, car_image1, car_image2;
    public Clip first_clip, clip;
    public boolean can_move = false;
    public AudioInputStream audioInput;
    public int car_space = 15;
    public Game(){
        player = new Player(WIDTH/2 - 24, 400, ID.Player);
        Window my_window = new Window(WIDTH, HEIGHT, "Frogger", this, player);
        handler = new Handler();
        try {
            image = ImageIO.read(new File("frogger/Images/Road.png"));
            car_image0 = ImageIO.read(new File("frogger/Images/0.png"));
            System.out.println(car_image0.getWidth());
            System.out.println(car_image0.getHeight());
            car_image1 = ImageIO.read(new File("frogger/Images/1.png"));
            car_image2 = ImageIO.read(new File("frogger/Images/2.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 5; j++){
                Car temp_car;
                Road temp_road = new Road(155 * j, (-1 * (my_height * 2)) + my_height * i, ID.Road, Math.abs((-1 * (my_height * 2)) + my_height * i  - min_threshold));
                if (j < 2){
                    temp_car = new Car(275 * (j + 1), (-1 * (my_height * 2)) + my_height * i + car_space, ID.Car);
                    temp_road.car_array.add(temp_car);
                    handler.addObject(temp_car);
                }
                handler.object.add(0, temp_road);
            }
        }
        
        handler.addObject(player);
        for (GameObject i: handler.object){
            if (i.getID() == ID.Road){
                i.inputImage = image; 
            }
            else if (i.getID() == ID.Car){
                i.inputImage = car_image0;
            }
            else {
                for (int j = 1; j < 8; j++){
                    try {
                        player.image_array[j - 1] = ImageIO.read(new File("frogger/Images/frog" + Integer.toString(j) + ".png"));
                        player.left_image_array[j - 1] = ImageIO.read(new File("frogger/Images/left_frog" + Integer.toString(j) + ".png"));
                        player.right_image_array[j - 1] = ImageIO.read(new File("frogger/Images/right_frog" + Integer.toString(j) + ".png"));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                i.inputImage = i.image_array[0];
            }
        }
        File music = new File("frogger/Audio/start.wav");
        try {
            audioInput = AudioSystem.getAudioInputStream(music);
            first_clip = AudioSystem.getClip();
            first_clip.open(audioInput);
            first_clip.start();
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
            if (can_move){
                for (GameObject i: handler.object){
                    if (i.getID() == ID.Road){
                        if (i.getY() == min_threshold && i.how_many_moves == 0){
                            i.how_many_moves = min_threshold;
                            i.setY(0 - 92);
                            for (GameObject j: i.car_array){
                                j.setY(0 - 92 + car_space);
                            }
                        }
                        for (GameObject j: i.car_array){
                            if (j.getX() > WIDTH){
                                j.setX(-132);
                            }
                            else {
                                j.setX(j.getX() + 2);
                            }
                        }
                    }
                }
            }
            if (player.getMovement()){
                first_clip.stop();
                if (player.moves_remaining == my_height){
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
                    if (i.getID() == ID.Road){
                        if (i.getY() == min_threshold && i.how_many_moves == 0){
                            i.how_many_moves = min_threshold;
                            for (GameObject j: i.car_array){
                                j.setY(0 - 92 + car_space);
                            }
                            i.setY(0 - 92);
                        }
                        else{
                            if (player.degrees == 0){
                                for (GameObject j: i.car_array){
                                    j.setY(j.getY() + 1);
                                }
                                i.setY(i.getY() + 1);
                                i.how_many_moves -= 1;
                            }
                        }  
                    }
                }
                if (player.degrees == 0){
                    player.moves_remaining -= 1;
                }
            }
            if (player.getMovement()){
                if (player.degrees == 270){
                    player.setX(player.getX() - 1);
                    player.moves_remaining -= 1;
                }
                else if (player.degrees == 90){
                    player.setX(player.getX() + 1);
                    player.moves_remaining -= 1;
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
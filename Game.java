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
import java.util.Random;

public class Game extends Canvas implements Runnable{
    public static int WIDTH = 720;
    public static int my_height = 92;
    public static int HEIGHT = 503;
    private Thread thread;
    private boolean running = false;
    public Player player;
    private Handler handler;
    public int min_threshold = my_height * 7;
    public BufferedImage image, rightcar_image0, leftcar_image0, car_image1, car_image2, death_image;
    public BufferedImage car_image;
    public Clip first_clip, clip;
    public boolean can_move = true;
    public AudioInputStream audioInput;
    public int car_space = 15;
    public Random r = new Random();
    public int min = 0;
    public int max = 350;
    public int speed = 1;
    public Game(){
        player = new Player(WIDTH/2 - 24, 400, ID.Player);
        Window my_window = new Window(WIDTH, HEIGHT, "Frogger", this, player);
        handler = new Handler();
        try {
            image = ImageIO.read(new File("frogger/Images/Road.png"));
            death_image = ImageIO.read(new File("frogger/Images/death.png"));
            rightcar_image0 = ImageIO.read(new File("frogger/Images/right0.png"));
            car_image = rightcar_image0;
            leftcar_image0 = ImageIO.read(new File("frogger/Images/left0.png"));
            car_image1 = ImageIO.read(new File("frogger/Images/1.png"));
            car_image2 = ImageIO.read(new File("frogger/Images/2.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < 8; i++){
            int random_num = r.nextInt((max - min) + 1) + min;
            for (int j = 0; j < 5; j++){
                Car temp_car;
                Road temp_road = new Road(155 * j, (-1 * (my_height * 2)) + my_height * (i - 2), ID.Road, Math.abs((-1 * (my_height * 2)) + my_height * (i - 2)  - min_threshold));
                if (j == 0){
                    temp_car = new Car(random_num, (-1 * (my_height * 2)) + my_height * (i - 2) + car_space, ID.Car);
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
                if (car_image == rightcar_image0){
                    i.inputImage = car_image;
                    i.degrees = 90;
                    car_image = leftcar_image0;
                }
                else {
                    i.inputImage = car_image;
                    i.degrees = 270;
                    car_image = rightcar_image0;
                }
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
        my_window.start_game();
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
    public boolean has_collided(GameObject object1, GameObject object2){
        int x1 = object1.getX();
        int y1 = object1.getY();
        int width1 = object1.inputImage.getWidth();
        int height1 = object1.inputImage.getHeight();

        int x2 = object2.getX();
        int y2 = object2.getY();
        int width2 = object2.inputImage.getWidth();
        int height2 = object2.inputImage.getHeight(); 
        if(x1 < x2 + width2 &&
            x1 + width1 > x2 &&
            y1 < y2 + height2 &&
            y1 + height1 > y2)
        {
            return true;
        }
        else {
            return false;
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
            for (GameObject i: handler.object){
                if (i.getID() == ID.Road){
                    if (i.getY() >= min_threshold && i.how_many_moves <= 0){
                        i.how_many_moves = min_threshold;
                        i.setY(0 - my_height);
                        for (GameObject j: i.car_array){
                            j.setY(i.getY() + car_space);
                        }
                    }
                    for (GameObject j: i.car_array){
                        if (has_collided(player, j) && player.dead == false){
                            player.inputImage = death_image;
                            player.dead = true;
                            File music = new File("frogger/Audio/squashed.wav");
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
                        if (j.degrees == 90){
                            if (j.getX() > WIDTH){
                                j.setX(-132);
                            }
                            else {
                                j.setX(j.getX() + speed);
                            }
                        }
                        else {
                            if (j.getX() < -132){
                                j.setX(HEIGHT + 132 * 2);
                            }
                            else {
                                j.setX(j.getX() - speed);
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
                        if (i.getY() >= min_threshold && i.how_many_moves <= 0){
                            i.how_many_moves = min_threshold;
                            i.setY(0 - my_height);
                            for (GameObject j: i.car_array){
                                j.setY(i.getY() + car_space);
                            }    
                        }
                        else{
                            if (player.degrees == 0){
                                i.setY(i.getY() + 1);
                                for (GameObject j: i.car_array){
                                    j.setY(i.getY() + car_space);
                                }
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
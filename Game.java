package frogger;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.io.File;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Canvas implements Runnable{
    public static int WIDTH = 720;
    public static int HEIGHT = 503;
    public static int my_height = 92;
    private Thread thread;
    private boolean running = false;
    public Player player;
    private Handler handler;
    public int min_threshold = my_height * 7;
    public BufferedImage image, rightcar_image0, leftcar_image0, rightcar_image1, rightcar_image2, leftcar_image1, leftcar_image2, death_image, grass_image, car_image, river_image, log_image1;
    public Clip first_clip;
    public AudioInputStream audioInput;
    public int car_space = 15;
    public Random r = new Random();
    public int min = 0;
    public int max = 350;
    public int speed = 1;
    public int my_score = 0;
    public int times_moved = 0;
    public int frames = 0;
    public int display_fps = 0;
    public long timer;
    public boolean turn_to_grass = false;
    public boolean turn_to_river = false;
    public ArrayList<BufferedImage> rightcars, leftcars;
    public ArrayList<GameObject> reordered_items = new ArrayList<GameObject>();
    public int amount_of_times_moved = 0;
    public Timer my_timer = new Timer();
    public TimerTask task = null;
    public long start_time = System.currentTimeMillis();
    public Game(){
        player = new Player(WIDTH/2 - 24, 400, ID.Player);
        Window my_window = new Window(WIDTH, HEIGHT, "Frogger", this, player);
        handler = new Handler();
        try {
            grass_image = ImageIO.read(new File("frogger/Images/Grass.png"));
            log_image1 = ImageIO.read(new File("frogger/Images/Road1.png"));
            image = ImageIO.read(new File("frogger/Images/Road.png"));
            river_image = ImageIO.read(new File("frogger/Images/River.png"));
            death_image = ImageIO.read(new File("frogger/Images/death.png"));
            rightcar_image0 = ImageIO.read(new File("frogger/Images/right0.png"));
            rightcar_image1 = ImageIO.read(new File("frogger/Images/right1.png"));
            rightcar_image2 = ImageIO.read(new File("frogger/Images/right2.png"));
            car_image = rightcar_image0;
            leftcar_image0 = ImageIO.read(new File("frogger/Images/left0.png"));
            leftcar_image1 = ImageIO.read(new File("frogger/Images/left1.png"));
            leftcar_image2 = ImageIO.read(new File("frogger/Images/left2.png"));
            rightcars = new ArrayList<BufferedImage>(Arrays.asList(rightcar_image0, rightcar_image1, rightcar_image2));
            leftcars = new ArrayList<BufferedImage>(Arrays.asList(leftcar_image0, leftcar_image1, leftcar_image2));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        for (int i = 0; i < 8; i++){
            int random_num = r.nextInt((max - min) + 1) + min;
            for (int j = 0; j < 5; j++){
                if (i != 7){
                    GameObject temp_car;
                    GameObject temp_road = new GameObject(155 * j, (-1 * (my_height * 2)) + my_height * (i - 2), ID.Road, Math.abs((-1 * (my_height * 2)) + my_height * (i - 2) - min_threshold));
                    if (j == 0){
                        temp_car = new GameObject(random_num, (-1 * (my_height * 2)) + my_height * (i - 2) + car_space, ID.Car, 0);
                        temp_road.car_array.add(temp_car);
                        handler.addObject(temp_car);
                    }
                    handler.object.add(0, temp_road);
                }
                else {
                    handler.addObject(new GameObject(155 * j, (-1 * (my_height * 2)) + my_height * (i - 1) + 6, ID.Grass, Math.abs((-1 * (my_height * 2)) + my_height * (i - 2) - min_threshold)));  
                }
            }
        }
        handler.addObject(player);
        for (GameObject i: handler.object){
            if (i.getID() == ID.Road){
                i.inputImage = image; 
                if (car_image == rightcar_image0){
                    i.degrees = 90;
                    car_image = leftcar_image0;
                }
                else {
                    i.degrees = 270;
                    car_image = rightcar_image0;
                }
                for (GameObject j: i.car_array){
                    j.degrees = i.degrees;
                    if (i.degrees == 90){
                        j.inputImage = rightcars.get(r.nextInt(rightcars.size()));
                    }
                    else {
                        j.inputImage = leftcars.get(r.nextInt(leftcars.size()));
                    }
                }
            }
            else if (i.getID() == ID.Grass){
                i.inputImage = grass_image;
                if (car_image == rightcar_image0){
                    i.degrees = 90;
                    car_image = leftcar_image0;
                }
                else {
                    i.degrees = 270;
                    car_image = rightcar_image0;
                }
            }
            else if (i.getID() == ID.Player) {
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
        this.start();
    }
    public synchronized void start(){
        thread = new Thread(this);
        thread.start();
        running = true; 
        first_clip = play_music("frogger/Audio/start.wav");
    }
    public void dead(){
        player.setMovement(false);
        player.inputImage = death_image;
        player.dead = true;
        play_music("frogger/Audio/squashed.wav");
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
        
        if(x1 < x2 + width2 && x1 + width1 > x2 && y1 < y2 + height2 && y1 + height1 > y2){ 
            return true;
        }
        else {
            return false;
        }
    }
    public Clip play_music(String filepath){
        Clip clip = null;
        try {
            audioInput = AudioSystem.getAudioInputStream(new File(filepath));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            return clip;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return clip;
    }
    public void check_move_up(GameObject i){
        if (i.getY() >= min_threshold && i.how_many_moves <= 0){
            i.how_many_moves = min_threshold;
            int random_num = r.nextInt((WIDTH + 132) + 1) - 132;
            if (i.getID() == ID.Road){
                i.setY(0 - my_height);
                if (my_score % 16 == 0 && turn_to_grass){
                    i.setID(ID.Grass);
                    i.inputImage = grass_image;
                    reordered_items.add(0, i);
                    for (int j = 0; j < i.car_array.size(); j++){
                        handler.removeObject(i.car_array.get(j));
                        i.car_array.remove(i.car_array.get(j));
                    }
                }
                else if (turn_to_river && i.getID() != ID.River){
                    i.setID(ID.River);
                    i.inputImage = river_image;
                    reordered_items.add(i);
                    for (int j = 0; j < i.car_array.size(); j++){
                        handler.removeObject(i.car_array.get(j));
                        i.car_array.remove(i.car_array.get(j));
                    }
                    if (i.getX() == 0){
                        for (int j = 0; j < 2; j++){
                            GameObject log = new GameObject(random_num + (r.nextInt((500 - 300) + 1) + 300) * j, i.getY() + car_space, ID.Log, 0);
                            log.inputImage = log_image1;
                            log.degrees = i.degrees;
                            handler.addObject(log);
                            i.car_array.add(log);
                        }
                    } 
                }
                else{
                    for (GameObject j: i.car_array){
                        j.degrees = i.degrees;
                        if (j.degrees == 90){
                            j.inputImage = rightcars.get(r.nextInt(rightcars.size()));
                        }
                        else {
                            j.inputImage = leftcars.get(r.nextInt(leftcars.size()));
                        }
                        j.setY(i.getY() + car_space);
                        j.setX(random_num);
                    }
                }      
            }
            else if (i.getID() == ID.River){
                i.setY(0 - my_height);
                i.setID(ID.Road);
                i.car_array.clear();
                if (i.getX() == 0){
                    GameObject car = new GameObject(random_num, i.getY() + car_space, ID.Car, 0);
                    i.car_array.add(car);
                    handler.addObject(car);
                    if (i.degrees == 90){
                        car.inputImage = rightcars.get(r.nextInt(rightcars.size()));
                    }
                    else {
                        car.inputImage = leftcars.get(r.nextInt(leftcars.size()));
                    }
                    car.degrees = i.degrees;
                }
                i.inputImage = image;
            }
            else if (i.getID() == ID.Grass){
                i.setY(0 - my_height);
                i.setID(ID.Road);
                if (amount_of_times_moved == 0){
                    GameObject car = new GameObject(random_num, i.getY() + car_space, ID.Car, 0);
                    i.car_array.add(car);
                    handler.addObject(car);
                    if (i.degrees == 90){
                        car.inputImage = rightcars.get(r.nextInt(rightcars.size()));
                    }
                    else {
                        car.inputImage = leftcars.get(r.nextInt(leftcars.size()));
                    }
                    car.degrees = i.degrees;
                }
                i.inputImage = image;
                amount_of_times_moved++;
            }
            else {
                i.setY(0 - my_height);
            }
        }
    }
    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        timer = System.currentTimeMillis();
        frames = 0;
        TimerTask task = null;
        task = new TimerTask() {
            @Override
            public void run() {
                if (player.getMovement()){
                    if (player.degrees == 270){
                        player.setX(player.getX() - 1);
                    }
                    else if (player.degrees == 90){
                        player.setX(player.getX() + 1);
                    }
                    if (player.moves_remaining == my_height){
                        first_clip.stop();
                        play_music("frogger/Audio/hop.wav");
                    }
                    for (GameObject i: handler.object){
                        if (i.getID() == ID.Road || i.getID() == ID.Grass || i.getID() == ID.River){
                            if (player.degrees == 0){
                                i.setY(i.getY() + 1);
                                for (GameObject j: i.car_array){
                                    j.setY(i.getY() + car_space);
                                }
                                i.how_many_moves -= 1;
                                // times_moved++;
                            } 
                        }
                    }
                    player.moves_remaining -= 1;
                    if (player.moves_remaining == 0){
                        if (player.degrees == 0){
                            my_score += 1;
                        }
                        // System.out.println(times_moved);
                        times_moved = 0;
                        player.setMovement(false);
                        player.moves_remaining = my_height;
                    }
                }
                for (GameObject i: handler.object){
                    if (i.getID() == ID.Road || i.getID() == ID.Grass || i.getID() == ID.River){
                        for (GameObject j: i.car_array){
                            // if (has_collided(player, j) && player.dead == false && j.getID() != ID.Log){
                            //     dead();
                            // }
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
                    else if (i.getID() == ID.Player){
                        if (player.getX() > WIDTH + player.max_int || player.getX() < 0 - player.max_int){
                            if (!player.dead){
                                dead();
                            } 
                        }
                        else if (player.on_log && !player.getMovement() && has_collided(player, player.log_being_touched)){
                            player.setX(player.getX() + player.side_move);
                        }
                    }
                    if (i.getID() == ID.River){
                        if (has_collided(player, i) && player.dead == false && player.on_log == false){
                            player.on_water = true;
                            player.river_being_touched = i;
                        }
                    }
                    else if (i.getID() == ID.Log){
                        if (has_collided(player, i) && player.dead == false){
                            player.log_being_touched = i;
                            player.on_log = true;
                            player.on_water = false;
                            if (i.degrees == 90){
                                player.side_move = 1;
                            }
                            else if (i.degrees == 270){
                                player.side_move = -1;
                            }
                        }
                    }
                }
                if (player.on_water && !player.getMovement() && has_collided(player, player.river_being_touched)){
                    dead();
                    player.on_water = false;
                }
                player.on_log = false;
                if (r.nextInt(3) == 0){
                    turn_to_grass = true;
                }
                if (r.nextInt(3) == 0){
                    turn_to_river = true;
                }
                for (int i = 0; i < handler.object.size(); i++){
                    if (handler.object.get(i).getID() == ID.Road || handler.object.get(i).getID() == ID.Grass || handler.object.get(i).getID() == ID.River){
                        check_move_up(handler.object.get(i));
                    }
                }
                for (GameObject i: reordered_items){
                    handler.removeObject(i);
                    handler.object.add(handler.object.size() - 1, i);
                }
                handler.removeObject(player);
                handler.addObject(player);
                reordered_items.clear();
                turn_to_grass = false;
                turn_to_river = false;
                amount_of_times_moved = 0;
                if (running){
                    if (System.currentTimeMillis() >= start_time + 400){
                        render();
                    }
                }
                frames++;
            }
        };
        my_timer.scheduleAtFixedRate(task, 400, 3);
        stop();
    }

    private void tick(){
        handler.tick();
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if (!running){
            task.cancel();
        }
        if (bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        handler.render(g);
        int fontSize = 25;
        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
        g.setColor(Color.red);
        g.drawString("Score: " + my_score, WIDTH - 125, 450);
        if (System.currentTimeMillis() - timer > 1000){
            timer += 1000;
            display_fps = frames;
            frames = 0;
        }
        if (display_fps != 0){
            g.drawString("FPS: " + display_fps, 0, 450);
        }
        g.dispose();
        bs.show();
    }
    public static void main(String[] args){
        new Game();
    }
}
package frogger;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.sound.sampled.*;
public class Player extends GameObject{
    protected BufferedImage img;
    protected int max_int = 86;
    protected int moves_remaining = max_int;
    protected boolean dead = false;
    public Player(int x, int y, ID id){
        super(x, y, id);
    }
    @Override
    public void tick(){
    }
    @Override
    public void render(Graphics g){
        if (getMovement() && this.moves_remaining > 0){
            if (this.moves_remaining == max_int){
                File music = new File("frogger/Audio/hop.wav");
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
            this.moves_remaining -= 1;
            load_image("frogger/Images/frog" + Integer.toString(this.moves_remaining/16 + 1) + ".png");
        }
        else {
            this.moves_remaining = max_int;
            this.setMovement(false);
        }
        g.drawImage(inputImage, x, y, null);
    }
}
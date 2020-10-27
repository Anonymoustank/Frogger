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
    protected int moves_remaining = 92;
    // BufferedImage inputImage;
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
            if (moves_remaining == 92){
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
            // try {
            //     img = ImageIO.read(new File("frogger/Images/frog" + Integer.toString(this.moves_remaining/16 + 1) + ".png"));
            // }
            // catch (Exception e) {
            //     e.printStackTrace();
            //     System.out.println(Integer.toString(this.moves_remaining/8 + 1));
            // }
            load_image("frogger/Images/frog" + Integer.toString(this.moves_remaining/16 + 1) + ".png");
        }
        else {
            this.moves_remaining = 92;
            this.setMovement(false);
            // load_image("frogger/Images/frog1.png");
            // try {
            //     img = ImageIO.read(new File("frogger/Images/frog1.png"));
            // }
            // catch (Exception e){
            //     e.printStackTrace();
            // }
        }
        g.drawImage(inputImage, x, y, null);
    }
}
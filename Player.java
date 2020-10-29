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
    protected int max_int = 92;
    protected int moves_remaining = max_int;
    protected boolean dead = false;
    public Player(int x, int y, ID id){
        super(x, y, id, 0);
    }
    @Override
    public void tick(){
    }
    @Override
    public void render(Graphics g){
        if (getMovement() && this.moves_remaining > 0){
            this.inputImage = this.image_array[this.moves_remaining/15];
        }
        else {
            this.moves_remaining = max_int;
            this.setMovement(false);
        }
        g.drawImage(inputImage, x, y, null);
    }
}
package frogger;
import java.awt.*;
import java.lang.Math;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.sound.sampled.*;
public class Player extends GameObject{
    protected int max_int = 92;
    protected int moves_remaining = max_int;
    protected boolean dead = false;
    protected int number_to_divide = max_int/6;
    protected int degrees = 0;
    public Player(int x, int y, ID id){
        super(x, y, id, 0);
    }
    @Override
    public void render(Graphics g){
        if (getMovement() && this.moves_remaining > 0){
            if (this.degrees == 0){
                this.inputImage = this.image_array[this.moves_remaining/number_to_divide];
            }
            else if (this.degrees == 270){
                this.inputImage = this.left_image_array[this.moves_remaining/number_to_divide];
            }
            else if (this.degrees == 90){
                this.inputImage = this.right_image_array[this.moves_remaining/number_to_divide];
            }
        }
        else {
            this.moves_remaining = max_int;
            this.setMovement(false);
        }
        g.drawImage(inputImage, x, y, null);
    }
}
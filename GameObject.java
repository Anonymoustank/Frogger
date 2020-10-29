package frogger;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class GameObject{
    protected int x, y;
    protected ID id;
    protected boolean inAnimation = false;
    protected BufferedImage inputImage;
    protected boolean move = false;
    protected int how_many_moves;
    public GameObject(int x, int y, ID id, int how_many_moves){
        this.x = x;
        this.y = y;
        this.id = id;
        this.how_many_moves = how_many_moves;
    }

    public void load_image(String file_path){
        try {
            this.inputImage = ImageIO.read(new File(file_path));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void tick(){

    }
    public void render(Graphics g){

    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setID(ID id){
        this.id = id;
    }
    public ID getID(){
        return id;
    }
    public void setMovement(boolean inAnimation){
        this.inAnimation = inAnimation;
    }
    public boolean getMovement(){
        return inAnimation;
    }
}
package frogger;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Player extends GameObject{
    protected BufferedImage img;
    public Player(int x, int y, ID id){
        super(x, y, id);
    }
    @Override
    public void tick(){

    }
    @Override
    public void render(Graphics g){
        try {
            img = ImageIO.read(new File("Images/28.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        g.setColor(Color.white);
        g.drawImage(img, x, y, null);
    }
}
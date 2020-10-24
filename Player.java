package frogger;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
public class Player extends GameObject{
    protected BufferedImage img;
    protected Image gif_img;
    public Player(int x, int y, ID id){
        super(x, y, id);
    }
    @Override
    public void tick(){

    }
    @Override
    public void render(Graphics g){
        setMovement(true);
        if (getMovement()){
            try {
                gif_img = new ImageIcon("Frogger/Images/frog.gif").getImage();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            g.drawImage(gif_img, x, y, null);
        }
        else {
            try {
                img = ImageIO.read(new File("Frogger/Images/frog.gif"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            g.drawImage(img, x, y, null);
        }
    }
}
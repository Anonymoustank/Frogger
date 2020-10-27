package frogger;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.sound.sampled.*;
public class Road extends GameObject{
    protected BufferedImage inputImage;
    public Road(int x, int y, ID id){
        super(x, y, id);
    }
    @Override
    public void tick(){
    }
    @Override
    public void render(Graphics g){
        try {
            inputImage = ImageIO.read(new File("frogger/Images/Road2.png"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        g.drawImage(inputImage, x, y, null);
    }
}
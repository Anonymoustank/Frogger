package frogger;
import java.awt.*;
import java.lang.Math;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Car extends GameObject{
    public Car(int x, int y, ID id){
        super(x, y, id, 0);
    }
    @Override
    public void render(Graphics g){
        g.drawImage(inputImage, x, y, null);
    }

}
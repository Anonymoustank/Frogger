package frogger;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.sound.sampled.*;
public class Road extends GameObject{
    public Road(int x, int y, ID id, int how_many_moves){
        super(x, y, id, how_many_moves);
    }
    @Override
    public void render(Graphics g){
        g.drawImage(inputImage, x, y, null);
    }
}
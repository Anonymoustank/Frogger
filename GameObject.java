package frogger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class GameObject{
    protected int x, y;
    protected ID id;
    protected boolean inAnimation = false;
    protected BufferedImage[] image_array = new BufferedImage[7];
    protected BufferedImage[] left_image_array = new BufferedImage[7];
    protected BufferedImage[] right_image_array = new BufferedImage[7];
    protected BufferedImage inputImage;
    protected ArrayList<GameObject> car_array = new ArrayList<GameObject>();
    protected int how_many_moves;
    protected int degrees = 0;
    public GameObject(int x, int y, ID id, int how_many_moves){
        this.x = x;
        this.y = y;
        this.id = id;
        this.how_many_moves = how_many_moves;
    }
    public void tick(){

    }
    public void render(Graphics g){
        g.drawImage(inputImage, x, y, null);
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
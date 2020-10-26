package frogger;
import java.util.LinkedList;
import java.awt.Graphics;
public class Handler{
    LinkedList<GameObject> object = new LinkedList<GameObject>();
    public void tick(){
        for (GameObject i: object){
            i.tick();
        }
    }
    public void render(Graphics g){
        for (GameObject i: object){
            i.render(g);
        }
    }
    public void addObject(GameObject object){
        this.object.add(object);
    }
    public void removeObject(GameObject object){
        this.object.remove(object);
    }
}
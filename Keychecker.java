package frogger;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class Keychecker extends KeyAdapter {
    Player player;
    int width;
    public Keychecker(Player player, int width){
        this.player = player;
        this.width = width;
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP){
            if (player.dead == false && player.getMovement() == false){
                player.degrees = 0;
                player.setMovement(true);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
            if (player.dead == false && player.getMovement() == false && player.getX() > player.max_int){
                player.degrees = 270;
                player.setMovement(true);
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
            if (player.dead == false && player.getMovement() == false && player.getX() < width - player.max_int * 2){
                player.degrees = 90;
                player.setMovement(true);
            }
        }
    }
}
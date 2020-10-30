package frogger;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class Keychecker extends KeyAdapter {
    Player player;
    public Keychecker(Player player){
        this.player = player;
    }
    public void keyTyped(KeyEvent e) {
        // Invoked when a key has been typed.
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
            if (player.dead == false && player.getMovement() == false && player.getX() < 720 - player.max_int * 2){
                player.degrees = 90;
                player.setMovement(true);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        // Invoked when a key has been released.
    }
}
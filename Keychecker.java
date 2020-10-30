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
            if (player.dead == false){
                player.setMovement(true);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        // Invoked when a key has been released.
    }
}
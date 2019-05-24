
package controller;

import java.util.Random;
import javax.swing.JPanel;
import model.PairOfButtons;

/**
 *
 * @author AnhTV
 */
public class PipesController {
    private PairOfButtons head = null;
    private PairOfButtons tail = null;
    

    public PairOfButtons getHead() {
        return head;
    }

    public void setHead(PairOfButtons head) {
        this.head = head;
    }

    public PairOfButtons getTail() {
        return tail;
    }

    public void setTail(PairOfButtons tail) {
        this.tail = tail;
    }

    
    
    public void setListDefault(int n){
        PairOfButtons[] pipes = new PairOfButtons[n];
        for (int i = 0; i < n; i++) {// init---------
            pipes[i] = new PairOfButtons();
        }
        Random r = new Random();
        pipes[0].setX(r.nextInt(100) + 250);//first POP in [250-350]
        for (int i = 0; i < n; i++) {
            pipes[i].random();
            if(i != n-1){
                pipes[i].setNext(pipes[i+1]);
                pipes[i+1].setX(pipes[i].getX() + 50 + pipes[i].getDistance_next());
            }
            pipes[i].setLocation();
            
        }
        head = pipes[0];
        tail = pipes[n-1];
    }
    
}

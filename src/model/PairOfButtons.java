
package model;

import controller.HappyFrogController;
import java.util.Random;
import javax.swing.JButton;

/**
 *
 * @author AnhTV
 */
public class PairOfButtons {
    private JButton up = new JButton();
    private JButton down = new JButton();
    private int distance;//khoang cach giua 2 buttons
    private int h;// height of button_up;
    private int x;// x of up
    private PairOfButtons next = null;
    private int distance_next;// khoang cach vs next;

    public PairOfButtons getNext() {
        return next;
    }

    public void setNext(PairOfButtons next) {
        this.next = next;
    }

    public int getDistance_next() {
        return distance_next;
    }

    public void setDistance_next(int distance_next) {
        this.distance_next = distance_next;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
    
    public JButton getUp() {
        return up;
    }

    public void setUp(JButton up) {
        this.up = up;
    }

    public JButton getDown() {
        return down;
    }

    public void setDown(JButton down) {
        this.down = down;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    public void random(){
        Random r = new Random();
        distance_next = r.nextInt(100) + 50;// 50-150
        distance = r.nextInt(50) + 100;//[100-150];
        int hMin = (HappyFrogController.h - distance) / 3;
        h = r.nextInt(HappyFrogController.h - distance - 2*hMin) + hMin;
    }
    public void setLocation(){
        up.setBounds(x, 0, 50, h);
        down.setBounds(x, h + distance, 50, HappyFrogController.h - h - distance);
    }
    
}

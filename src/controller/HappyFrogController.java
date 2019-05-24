
package controller;

import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.PairOfButtons;
import view.HappyFrogView;

/**
 *
 * @author AnhTV
 */
public class HappyFrogController  implements KeyListener{
    public static final int h = 400;
    public static final int w = 1000;
    
    private HappyFrogView view;
    private int score = 0;
    private int speed = 20;
    private int yOfFrog;
    private int xOfFrog = 100;
    
    private PairOfButtons currentPOP = null;// current pair of pipes
    private Thread t;
    private PipesController listOfPOP = new PipesController();
    

    public HappyFrogController(HappyFrogView view) {
        this.view = view;
        setFrame();
        play();
        control();
    }
    
    public void createPipes(){
        int n = w / 100 + 1;
        yOfFrog = (h-50) / 2;
        listOfPOP.setListDefault(n);
        try {
            readFile();
        } catch (IOException ex) {
        }
    }
    public void setFrame(){
        createPipes();
        PairOfButtons p = listOfPOP.getHead();
        currentPOP = listOfPOP.getHead();
        while(p != null){
            view.getPan().add(p.getUp());
            view.getPan().add(p.getDown());
            view.getPan().updateUI();
            p = p.getNext();
        }
        view.getPan().setLayout(null);
        view.getPan().setBounds(20, 20, w, h);
        
        int dis = w / 11;
        int width = 2 * dis;
        int height = 50;
        view.getBtnPause().setBounds(20, h + 50, width, height);
        view.getBtnSave().setBounds(20 + 3*dis, h + 50, width, height);
        view.getLblScore().setText("Score: "+ score);
        view.getLblScore().setBounds(20 + 6*dis, h + 50, width, height);
        view.getBtnExit().setBounds(20 + 9*dis, h + 50, width, height);
        
        
        view.getLblFrog().setBounds(xOfFrog, yOfFrog , 50, 50);
        view.setSize(w + 50, h + 150);
        view.setLayout(null);
        
        view.getBtnPause().setFocusable(false);
        view.setFocusable(true);
        view.addKeyListener(this);
        
    }

    public void frogFallDown(){
        view.getLblFrog().setLocation(xOfFrog, ++yOfFrog);
    }
    public void frogFly(){
        yOfFrog -= 20;
        view.getLblFrog().setLocation(xOfFrog, yOfFrog);
    }
    public void pipeMove(){
        PairOfButtons p = listOfPOP.getHead();
        while(p != null){
            p.setX(p.getX() - 1);
            p.setLocation();
            if(currentPOP.getX()+50 < xOfFrog){
                currentPOP = currentPOP.getNext();
                score++;
            }
            p = p.getNext();
        }
        if(listOfPOP.getHead().getX() <= -50) {
            
            //remove head
            view.getPan().remove(listOfPOP.getHead().getUp());
            view.getPan().remove(listOfPOP.getHead().getDown());
            
            //set new head = head.next()
            listOfPOP.setHead(listOfPOP.getHead().getNext());
            
            //add new tail
            PairOfButtons newPOP = new PairOfButtons();
            newPOP.random();
            newPOP.setX(listOfPOP.getTail().getX() + 50 + listOfPOP.getTail().getDistance_next());
            newPOP.setLocation();
            view.getPan().add(newPOP.getUp());
            view.getPan().add(newPOP.getDown());
            view.getPan().updateUI();
            listOfPOP.getTail().setNext(newPOP);
            listOfPOP.setTail(newPOP);
            
        }
    }
    
    public boolean isGameOver(){
        if(yOfFrog <= 0 && yOfFrog+50 >= h) return true;// cham tren cham duoi
        
        if(xOfFrog+50 < currentPOP.getX() || xOfFrog > currentPOP.getX() + 50 
                || (yOfFrog > currentPOP.getH() && yOfFrog+50 < currentPOP.getH() + currentPOP.getDistance()))
            return false;
        return true;
    }
    
    public void play(){
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        frogFallDown();
                        pipeMove();
                        view.getLblScore().setText("Score: " + score);
                        if(isGameOver()){
                            GameOver();
                        }
                        speedUp();
                        Thread.sleep(speed);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HappyFrogController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }
    public void GameOver(){
        try {
            resetFile();
        } catch (IOException ex) {
        }
        JOptionPane.showMessageDialog(view, "You have " + score + " scores");
        System.exit(0);
        t.stop();
        
    }
    
    public void speedUp(){
        if(score >= 10 && score < 20) speed = 16;
        else if(score >= 20 && score < 30) speed = 12;
        else if(score >= 30 && score < 40) speed = 8;
        else if(score >= 40) speed = 4;
    }

    public void control(){
        view.getBtnPause().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.suspend();
            }
        });
        view.getBtnSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t.stop();
                try {
                    saveFile();
                } catch (IOException ex) {
                }
                JOptionPane.showMessageDialog(view, "Save successful!");
                System.exit(0);
                
            }
        });
        view.getBtnExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    resetFile();
                } catch (IOException ex) {
                }
                System.exit(0);
            }
        });
    }
    public void readFile() throws IOException{
        FileInputStream in = new FileInputStream("data.txt");
        int i;
        String s = "";
        while((i = in.read()) != -1){
            s += (char)i;
        }
        String[] data = s.split("[\\n]");
        if(Integer.parseInt(data[0]) == 0);// default
        else{//ORDER: score,yOfFrog,speed,head[x,h,distance,distance_next] ... tail  
            score = Integer.parseInt(data[0]);
            speed = Integer.parseInt(data[1]);
            yOfFrog = Integer.parseInt(data[2]);
            PairOfButtons p = listOfPOP.getHead();
            for(i = 3; i < data.length; i ++){
                String[] innerData = data[i].split("[\\s]");
                p.setX(Integer.parseInt(innerData[0]));
                p.setH(Integer.parseInt(innerData[1]));
                p.setDistance(Integer.parseInt(innerData[2]));
                p.setDistance_next(Integer.parseInt(innerData[3]));
                p.setLocation();
                p = p.getNext();
            }
        }
        in.close();
    }
    public void saveFile() throws IOException{
        FileOutputStream out = new FileOutputStream("data.txt");
        String s = score + "\n" + speed + "\n" + yOfFrog + "\n";
        PairOfButtons p = listOfPOP.getHead();
        while(p != null){
            s += p.getX() + " " + p.getH() + " " + p.getDistance() + " " + p.getDistance_next() + "\n";
            p = p.getNext();
        }
        byte[] b = s.getBytes();
        out.write(b);
        out.close();
    }
    public void resetFile() throws IOException{
        FileOutputStream out = new FileOutputStream("data.txt");
        String s = "0";
        out.write(s.getBytes());
        out.close();
    }
        
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(!isGameOver() && e.getKeyCode() == KeyEvent.VK_UP){
            frogFly();
            if(!t.isAlive()) t.start();
            else t.resume();
        }
            
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    
    
    
    
    
    
}

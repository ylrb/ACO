import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class Carte extends JFrame implements ActionListener, MouseListener {
    private int width;
    private int height;
    private int dt = 100;
    private Timer timer;

    public Carte() {
        width = 1000;
        height = 700;

    }
    public Carte(int width, int height) {
        this.width = width;
        this.height = height;

        setBounds(100, 50, width, height) ;
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setBackground(new Color(43, 37, 20));

        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();

        repaint();
        
    }

    public void paint (Graphics g) {

        g.setColor(couleurMur);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(couleurSol);
        g.fillRect((int)(borneSup.x),(int)(borneSup.y),(int)(borneInf.x-borneSup.x),(int)(borneInf.y-borneSup.y));

        spawn.draw(g);
        food.draw(g);

        for (int i = 0; i<nombreFourmis; i++) {
            g.setColor(Color.ORANGE);
            fifi[i].draw(g,true);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==timer) {
            
            repaint();
        }


    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.setTitle("X="+e.getX()+";Y="+e.getY());
        System.out.println("clik");
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Carte extends JFrame implements ActionListener, MouseListener {
    private int width;
    private int height;
    private int dt = 20;
    private Timer timer;
    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>();

    public Carte() {
        width = 1000;
        height = 700;

    }
    public Carte(int width, int height) {
        this.width = width;
        this.height = height;

        for (int i = 0; i < 1000; i++) {
            fourmis.add(new Fourmi(400.0,400.0));
        }

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
        Toolkit.getDefaultToolkit().sync();

        g.setColor(new Color(43, 37, 20));
        g.fillRect(0, 0,this.getWidth(), this.getHeight());

        for (Fourmi f : fourmis) {
            f.dessine(g);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==timer) {
            for (Fourmi f : fourmis) {
                f.avancer();
                repaint();
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        this.setTitle("X="+e.getX()+";Y="+e.getY());
        System.out.println("click");
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}

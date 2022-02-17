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
    private int dt = 20;
    private Timer timer;
    private Fourmi fourmi;

    public Carte() {
        width = 1000;
        height = 700;

    }
    public Carte(int width, int height) {
        this.width = width;
        this.height = height;

        fourmi = new Fourmi(400.0,400.0);

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

        //g.setColor(new Color(43, 37, 20));
        //g.fillRect(0, 0,this.getWidth(), this.getHeight());

        fourmi.dessine(g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==timer) {
            fourmi.avancer();
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

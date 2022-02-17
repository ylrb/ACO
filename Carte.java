import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    private ArrayList<PheroAller> pheromonesAller = new ArrayList<PheroAller>();

    private static int compteur = 0; //compteur qui compte le nombre de boucle effectué pour pouvoir espacer les phéromones
    private static final int compteurMAX = 20; //espacement des phéromones

    public Carte() {
        width = 1000;
        height = 700;

    }
    public Carte(int width, int height) {
        this.width = width;
        this.height = height;

        for (int i = 0; i < 1; i++) {
            fourmis.add(new Fourmi(400.0,400.0));
        }

        setBounds(100, 50, width, height);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(43, 37, 20));

        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();
    }

    public void paint (Graphics g) {
        Toolkit.getDefaultToolkit().sync();

        g.setColor(new Color(43, 37, 20));
        g.fillRect(0, 0,this.getWidth(), this.getHeight());

        for (Fourmi f : fourmis) {
            f.dessine(g);
        }
        for (PheroAller p : pheromonesAller) {
            p.dessine(g);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==timer) {
            //les phéromones disparaissent si leur taux est trop f(aible
            ArrayList<Integer> tauxTropBas = new ArrayList<Integer>();
            for (PheroAller p : pheromonesAller) {
                if (p.getTaux()<5) {
                    tauxTropBas.add(pheromonesAller.indexOf(p));
                }
            }
            for (Integer i : tauxTropBas) {
                pheromonesAller.remove((int)i);
            }
            //les phéromones s'estompent (leur taux diminue)
            for (PheroAller p : pheromonesAller) {
                p.estompe();
            }
            //les fourmis avancent
            for (Fourmi f : fourmis) {
                f.avancer();
            }
            //on rajoute des phéromones toutes les 10 itérations de la boucle
            if (compteur>compteurMAX) {
                for (Fourmi f : fourmis) {
                    pheromonesAller.add(new PheroAller(f.getx(),f.gety()));
                }
                compteur=0;
            }
            compteur++;
            repaint();
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
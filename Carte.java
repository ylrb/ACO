import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Carte extends JPanel implements ActionListener, MouseListener {
    private int dt = 20;
    private Timer timer;
    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>();
    private ArrayList<PheroAller> pheromonesAller = new ArrayList<PheroAller>();
    private static int compteur = 0; //compteur qui compte le nombre de boucle effectué pour pouvoir espacer les phéromones
    private static final int COMPTEUR_MAX = 20; //espacement des phéromones

    public Carte() {

        for (int i = 0; i < 10; i++) {
            fourmis.add(new Fourmi(400.0,400.0));
        }

        setBackground(new Color(43, 37, 20));

        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();
        setVisible(true);
        repaint();
    }

    public void paint (Graphics g) {
        Toolkit.getDefaultToolkit().sync();

        g.setColor(new Color(43, 37, 20));
        g.fillRect(0, 0,this.getWidth(), this.getHeight());

        // On dessine toutes les fouris et phéromones
        for (Fourmi f : fourmis) {
            f.dessine(g);
        }
        for (PheroAller p : pheromonesAller) {
            p.dessine(g);
        }
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource()==timer) {
            // Les phéromones disparaissent si leur taux est trop faible
            ArrayList<Integer> tauxTropBas = new ArrayList<Integer>();
            for (PheroAller p : pheromonesAller) {
                if (p.getTaux()<5) {
                    tauxTropBas.add(pheromonesAller.indexOf(p));
                }
            }
            for (Integer i : tauxTropBas) {
                pheromonesAller.remove((int)i);
            }
            // Les phéromones s'estompent (leur taux diminue)
            for (PheroAller p : pheromonesAller) {
                p.estompe();
            }
            // Les fourmis avancent
            for (Fourmi f : fourmis) {
                f.avancer();
            }
            // On rajoute des phéromones toutes les COMPTEUR_MAX itérations de la boucle
            if (compteur>COMPTEUR_MAX) {
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

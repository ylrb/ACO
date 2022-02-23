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
    private ArrayList<PheroRetour> pheromonesRetour = new ArrayList<PheroRetour>();
    private ArrayList<Nourriture> nourritures = new ArrayList<Nourriture>();
    private Fourmiliere fourmiliere;

    private static int compteur = 0; // Compteur qui compte le nombre de boucle effectué pour pouvoir espacer les phéromones
    private static final int COMPTEUR_MAX = 20; // Espacement des phéromones

    public Carte() {
        setBackground(new Color(43, 37, 20));
        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();

        // Initialisation de la fourmilière, des fourmis et de la nourriture
        fourmiliere = new Fourmiliere(300.0,300.0);
        for (int i = 0; i < 10; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
        fourmis.add(new FourmiB(600.0,600.0));
        nourritures.add(new Nourriture(600, 600, 10));       

        setVisible(true);
        repaint();
    }

    public void paint (Graphics g) {
        Toolkit.getDefaultToolkit().sync();

        g.setColor(new Color(43, 37, 20));
        g.fillRect(0, 0,this.getWidth(), this.getHeight());

        // On dessine la fourmilière et toutes les fourmis, phéromones et nourritures
        fourmiliere.dessine(g);
        for (Fourmi f : fourmis) {
            f.dessine(g);
        }
        for (PheroAller p : pheromonesAller) {
            p.dessine(g);
        }
        for (PheroRetour p : pheromonesRetour) {
            p.dessine(g);
        }
        for (Nourriture n : nourritures) {
            n.dessine(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==timer) {
            updatePheromones(); // Mise à jour des phéromones
            for (Fourmi f : fourmis) {
                if ((f.getPosition().x<5)||(f.getPosition().x>getWidth()-5)) { // Les fourmis "rebondissent" sur les murs
                    f.inverserVertical();
                }
                if ((f.getPosition().y<5)||(f.getPosition().y>getHeight()-5)) {
                    f.inverserHorizontal();;
                }
                f.avancer(nourritures, fourmiliere); // Les fourmis avancent
            }
            repaint();
        }

    }

    public void updatePheromones() {
        // Les phéromones disparaissent si leur taux est trop faible
        ArrayList<Integer> tauxTropBasAller = new ArrayList<Integer>();
        ArrayList<Integer> tauxTropBasRetour = new ArrayList<Integer>();
        // On fait s'estomper les phéromones et on stocke les indices de celles qui ont un indice trop faible
        for (PheroAller p : pheromonesAller) {
            if (p.getTaux()<5) {
                tauxTropBasAller.add(pheromonesAller.indexOf(p));
            }
            p.estompe();
        }
        for (PheroRetour p : pheromonesRetour) {
            if (p.getTaux()<5) {
                tauxTropBasRetour.add(pheromonesRetour.indexOf(p));
            }
            p.estompe();
        }
        // Les phéromones qui ont un indice trop faible sont supprimées
        for (Integer i : tauxTropBasAller) {
            pheromonesAller.remove((int)i);
        }
        for (Integer i : tauxTropBasRetour) {
            pheromonesRetour.remove((int)i);
        }
        // On rajoute des phéromones toutes les COMPTEUR_MAX itérations de la boucle
        if (compteur>COMPTEUR_MAX) {
            for (Fourmi f : fourmis) {
                if (f.getClass() == FourmiA.class) {
                    pheromonesAller.add(new PheroAller(f.getPosition()));
                } else {
                    pheromonesRetour.add(new PheroRetour(f.getPosition()));
                }
            }
            compteur=0;
        }
        compteur++;
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

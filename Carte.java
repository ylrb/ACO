import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Carte extends JPanel implements ActionListener, MouseListener {
    private int dt = 2;
    private Timer timer;
    
    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>();
    private ArrayList<Pheromone> pheromonesAller = new ArrayList<Pheromone>();
    private ArrayList<Pheromone> pheromonesRetour = new ArrayList<Pheromone>();
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
        for (int i = 0; i < 20; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
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
        if (true) {
            for (Pheromone p : pheromonesAller) {
                p.dessine(g);
            }
            for (Pheromone p : pheromonesRetour) {
                p.dessine(g);
            }
        }
        for (Nourriture n : nourritures) {
            n.dessine(g);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==timer) {
            updatePheromones(); // Mise à jour des phéromones
            ajoutPheromones(); // On ajoute les nouvelles phéromones
            promotionFourmis(); // Promotion des fourmis en type A ou B si elles ont atteint la fourmilière/nourriture
            for (Fourmi f : fourmis) { // Déplacement des fourmis
                if ((f.getPosition().x<5)||(f.getPosition().x>getWidth()-5)) { // Les fourmis "rebondissent" sur les murs
                    f.direction.inverserVertical();
                    f.errance.inverserVertical();
                }
                if ((f.getPosition().y<5)||(f.getPosition().y>getHeight()-5)) {
                    f.direction.inverserHorizontal();
                    f.errance.inverserHorizontal();
                }
                if (f.getClass() == FourmiA.class) {
                    f.avancer(nourritures, fourmiliere, pheromonesRetour);
                } else {
                    f.avancer(nourritures, fourmiliere, pheromonesAller);
                }
            }
            repaint();
        }
    }

    private void updatePheromones() {
        // Les phéromones disparaissent si leur taux est trop faible
        ArrayList<Integer> tauxTropBasAller = new ArrayList<Integer>();
        ArrayList<Integer> tauxTropBasRetour = new ArrayList<Integer>();
        // On fait s'estomper les phéromones et on stocke les indices de celles qui ont un indice trop faible
        for (Pheromone p : pheromonesAller) {
            if (p.getTaux()<5) {
                tauxTropBasAller.add(pheromonesAller.indexOf(p));
            }
            p.estompe();
        }
        for (Pheromone p : pheromonesRetour) {
            if (p.getTaux()<5) {
                tauxTropBasRetour.add(pheromonesRetour.indexOf(p));
            }
            p.estompe();
        }
        // Les phéromones qui ont un taux trop faible sont supprimées
        for (Integer i : tauxTropBasAller) {
            pheromonesAller.remove((int)i);
        }
        for (Integer i : tauxTropBasRetour) {
            pheromonesRetour.remove((int)i);
        }
    }

    private void ajoutPheromones() {
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

    private void promotionFourmis() {
        ArrayList<Integer> promuAversB = new ArrayList<Integer>();
        ArrayList<Integer> promuBversA = new ArrayList<Integer>();
        // On stocke les indices des fourmis qui ont atteint leur objectif
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiA.class) {
                for (Nourriture n : nourritures) {
                    if (f.getPosition().distance(n.getPosition())<3.0) {
                        promuAversB.add(fourmis.indexOf(f));
                    }
                }
            } else {
                if (f.getPosition().distance(fourmiliere.getPosition())<3.0) {
                    promuBversA.add(fourmis.indexOf(f));
                }
            }
        }
        // On change le type de ces fourmis (on ne peut pas le faire à l'intérieur du for each donc on a recours aux indices)
        for (Integer i : promuAversB) {
            double X = fourmis.get(i).getPosition().x;
            double Y = fourmis.get(i).getPosition().y;
            Vecteur dir = fourmis.get(i).getDirection(); // Il faut conserver la direction initiale de la fourmi
            dir.inverser(); // Puis il faut l'inverser pour que la fourmi reparte en arrière
            fourmis.remove((int)i);
            fourmis.add(new FourmiB(X,Y,dir));
        }
        for (Integer i : promuBversA) {
            double X = fourmis.get(i).getPosition().x;
            double Y = fourmis.get(i).getPosition().y;
            Vecteur dir = fourmis.get(i).calculAttractionPheromones(pheromonesRetour, true);
            fourmis.remove((int)i);
            fourmis.add(new FourmiA(X,Y,dir));
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

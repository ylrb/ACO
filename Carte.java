import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Carte extends JPanel implements ActionListener, MouseListener {
    
    // Tous les éléments du terrain, qui sont contenus séparément dans des ArrayLists
    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>();
    private ArrayList<Pheromone> pheromonesAller = new ArrayList<Pheromone>();
    private ArrayList<Pheromone> pheromonesRetour = new ArrayList<Pheromone>();
    private ArrayList<Nourriture> nourritures = new ArrayList<Nourriture>();
    private Fourmiliere fourmiliere;

    // Images
    private BufferedImage imageFourmiA, imageFourmiB;
    protected static final int TAILLE = 20;

    // Variables du timer
    private static int dt = 3;
    private static Timer timer;

    // Réglages
    private static int compteur = 0; // Compteur qui indique le nombre de boucles effectuées pour pouvoir espacer les phéromones
    private static final int COMPTEUR_MAX = 20; // Espacement des phéromones
    private static final boolean AFFICHAGE_PHEROMONES = true; // Doit-on visualiser les phéromones ou non
    private static final int NOMBRE_FOURMIS = 50;

    public Carte() {
        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();

        // On importe les images de fourmis
        try {
            imageFourmiA = ImageIO.read(new File("assets/Fourmi.png")); 
            imageFourmiB = ImageIO.read(new File("assets/FourmiMiam.png")); 
            imageFourmiA = redimensionner(imageFourmiA);
            imageFourmiB = redimensionner(imageFourmiB);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier.");
        }

        // Initialisation de la fourmilière, des fourmis et de la nourriture
        nourritures.add(new Nourriture(600, 600, 10));
        fourmiliere = new Fourmiliere(300.0,300.0);
        for (int i = 0; i < NOMBRE_FOURMIS; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }

        setVisible(true);
        repaint();
    }

    public void paint (Graphics g) {
        Toolkit.getDefaultToolkit().sync();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // On dessine la fourmilière et toutes les fourmis, phéromones et nourritures
        if (AFFICHAGE_PHEROMONES) {
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
        fourmiliere.dessine(g);
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiA.class) {
                f.dessine(g,imageFourmiA);
            } else {
                f.dessine(g,imageFourmiB);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==timer) {

            updatePheromones(); // Mise à jour des phéromones actuelles
            ajoutPheromones(); // On ajoute les nouvelles phéromones
            changementFourmis(); // Changement des fourmis en type A ou B si elles ont atteint la fourmilière/nourriture
            deplacementFourmis(); // Déplacement des fourmis selon leur type et gestion des murs

            repaint();
        }
    }

    // Les phéromones disparaissent si leur taux est trop faible
    private void updatePheromones() {
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

        // Les phéromones qui ont un taux trop faible, dont on connaît les sont supprimées
        for (Integer i : tauxTropBasAller) {
            pheromonesAller.remove((int)i);
        }
        for (Integer i : tauxTropBasRetour) {
            pheromonesRetour.remove((int)i);
        }
    }

    // On rajoute des phéromones toutes les COMPTEUR_MAX itérations de la boucle
    private void ajoutPheromones() {
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

    // Les fourmis changent d'état si elles ont atteint leur objectif (nourriture/fourmilière)
    private void changementFourmis() {
        ArrayList<Integer> changeAversB = new ArrayList<Integer>();
        ArrayList<Integer> changeBversA = new ArrayList<Integer>();

        // On stocke les indices des fourmis qui ont atteint leur objectif
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiA.class) {
                for (Nourriture n : nourritures) {
                    if (f.getPosition().distance(n.getPosition()) < 1.8*n.getRayon()) {
                        changeAversB.add(fourmis.indexOf(f));
                    }
                }
            } else {
                if (f.getPosition().distance(fourmiliere.getPosition()) < 1.0) {
                    changeBversA.add(fourmis.indexOf(f));
                }
            }
        }

        // On change le type de ces fourmis (on ne peut pas le faire à l'intérieur du for each donc on a recours aux indices)
        for (Integer i : changeAversB) {
            double X = fourmis.get(i).getPosition().x;
            double Y = fourmis.get(i).getPosition().y;
            Vecteur dir = fourmis.get(i).getDirection(); // Il faut conserver la direction initiale de la fourmi
            dir.inverser(); // Puis il faut l'inverser pour que la fourmi reparte en arrière
            fourmis.remove((int)i);
            fourmis.add(new FourmiB(X,Y,dir));
        }
        for (Integer i : changeBversA) {
            fourmiliere.depot(); // La fourmi dépose la nourriture dans la fourmilière
            double X = fourmiliere.getPosition().x;
            double Y = fourmiliere.getPosition().y;
            Vecteur dir = fourmis.get(i).calculAttractionPheromones(pheromonesRetour, true);
            fourmis.remove((int)i); 
            fourmis.add(new FourmiA(X,Y,dir));
        }
    }

    // Déplacement des fourmis selon leur type et gestion des murs
    private void deplacementFourmis() {
        for (Fourmi f : fourmis) { 
            
            // Les fourmis "rebondissent" sur les murs
            if ((f.getPosition().x<5)||(f.getPosition().x>getWidth()-5)) {
                f.direction.inverserVertical();
                f.errance.inverserVertical();
            }
            if ((f.getPosition().y<5)||(f.getPosition().y>getHeight()-5)) {
                f.direction.inverserHorizontal();
                f.errance.inverserHorizontal();
            }

            // Les fourmis avancent en fonction de leur environnement
            if (f.getClass() == FourmiA.class) {
                f.avancer(nourritures, fourmiliere, pheromonesRetour);
            } else {
                f.avancer(nourritures, fourmiliere, pheromonesAller);
            }
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

    // Redimensionne l'image de fourmi à la taille désirée TAILLE
    private BufferedImage redimensionner(BufferedImage img) {
        int largeur = img.getWidth();
        int hauteur = img.getHeight();
    
        BufferedImage nouvelleImage = new BufferedImage(TAILLE, TAILLE, img.getType());
        Graphics2D g2 = nouvelleImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g2.drawImage(img, 0, 0, TAILLE, TAILLE, 0, 0, largeur, hauteur, null);  
        g2.dispose(); 

        return nouvelleImage;
    }

}

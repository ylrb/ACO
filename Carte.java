import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Carte extends JPanel implements ActionListener, MouseListener {
    
    // Tous les éléments du terrain, qui sont contenus séparément dans des listes                                                                                        
    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>(NOMBRE_FOURMIS);
    private LinkedList<Pheromone> pheromonesAller = new LinkedList<Pheromone>();
    private LinkedList<Pheromone> pheromonesRetour = new LinkedList<Pheromone>();
    private ArrayList<Nourriture> nourritures = new ArrayList<Nourriture>();
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    private Fourmiliere fourmiliere;

    // Images
    private BufferedImage imageFourmiA, imageFourmiB;
    protected static final int TAILLE = 25;

    // Variables du timer par défaut
    public static int dt = 10;
    public Timer timer;

    // Réglages
    private static boolean deplaceFourmiliere = false;
    private static Nourriture deplaceNourriture;
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
        nourritures.add(new Nourriture(800, 400, 10));
        fourmiliere = new Fourmiliere(300.0,300.0);
        for (int i = 0; i < NOMBRE_FOURMIS; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
        Vecteur[] bordures = {new Vecteur(10,10), new Vecteur(1015,10), new Vecteur(1015,680), new Vecteur(10,680)};
        obstacles.add(new Obstacle(bordures));
        Vecteur[] coins1 = {new Vecteur(600,200), new Vecteur(700,200), new Vecteur(700,500), new Vecteur(600,500)};
        obstacles.add(new Obstacle(coins1));


        setVisible(true);
        repaint();
    }

    public void paint (Graphics g) {
        Toolkit.getDefaultToolkit().sync();

        g.setColor(new Color(120,100,80));
        g.fillRect(0, 0, getWidth(), getHeight());

        // On dessine la fourmilière et toutes les fourmis, phéromones et nourritures
        for (Obstacle o : obstacles) {
            o.dessine(g);
        }
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
        int tauxTropBasAller = 0;
        int tauxTropBasRetour = 0;

        // On fait s'estomper les phéromones et on compte le nombre de phéromones qui ont un indice trop faible
        for (Pheromone p : pheromonesAller) {
            if (p.getTaux()<5) {
                tauxTropBasAller++;
            }
            p.estompe();
        }
        for (Pheromone p : pheromonesRetour) {
            if (p.getTaux()<5) {
                tauxTropBasRetour++;
            }
            p.estompe();
        }

        // Les phéromones qui ont un taux trop faible sont supprimées : on supprime les "tauxTropBas" premiers éléments des LinkedLists
        for (int i = 0; i < tauxTropBasAller; i++) {
            pheromonesAller.remove(0);
        }
        for (int i = 0; i < tauxTropBasRetour; i++) {
            pheromonesRetour.remove(0);
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
                f.avancer(nourritures, fourmiliere, pheromonesRetour, obstacles);
            } else {
                f.avancer(nourritures, fourmiliere, pheromonesAller, obstacles);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("click");
    }

    public void mousePressed(MouseEvent e) {
        Vecteur mousePos = new Vecteur(e.getX(), e.getY());
        if ( mousePos.distance(fourmiliere.getPosition()) < fourmiliere.getRayon()){
            deplaceFourmiliere = true;
        }
        for (Nourriture n : nourritures){
            if ( mousePos.distance(n.getPosition()) < n.getRayon()){
                deplaceNourriture = n;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        Vecteur sourisPos = new Vecteur(e.getX(), e.getY());
        if (deplaceFourmiliere){
            fourmiliere.setPosition(sourisPos);
            repaint();
            deplaceFourmiliere = false;
        }
        if (deplaceNourriture != null){
            deplaceNourriture.setPosition(sourisPos);
            repaint();
            deplaceNourriture = null;
        }
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

    public void changeDt(int newDt){
        timer.stop();
        timer = new Timer(newDt, this);
        timer.start();
    }

    public void reset(){
        pheromonesAller.clear();
        pheromonesRetour.clear();
        int length = fourmis.size();
        fourmis.clear();
        for (int i=0; i<length; i++) {
            fourmis.add( new FourmiA(fourmiliere.getPosition()) );
        }
        repaint();
    }

    public void changeNbFourmis(int nb){
        fourmis.clear();
        for (int i=0; i<nb; i++) {
            fourmis.add( new FourmiA(fourmiliere.getPosition()) );
        }
        reset();
    }

}

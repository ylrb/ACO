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
    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>(nombreFourmis);
    private LinkedList<Pheromone> pheromonesAller = new LinkedList<Pheromone>();
    private LinkedList<Pheromone> pheromonesRetour = new LinkedList<Pheromone>();
    private ArrayList<Nourriture> nourritures = new ArrayList<Nourriture>();
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    private Fourmiliere fourmiliere;

    // Images et tailles
    private BufferedImage imageFourmiA, imageFourmiB, imageFourmiliere, imageNourriture, imageFond;
    protected static final int TAILLE_FOURMI = 25;
    protected static final int TAILLE_FOURMILIERE = 40;
    protected static final int TAILLE_NOURRITURE = 30;

    // Variables du timer par défaut
    private static int dt = 10;
    private Timer timer;

    // Réglages
    private static int nombreFourmis = 30;
    private static boolean affichagePheromones = true; // Doit-on visualiser les phéromones ou non
    private static int compteur = 0; // Compteur qui indique le nombre de boucles effectuées pour pouvoir espacer les phéromones
    private static final int COMPTEUR_MAX = 20; // Espacement des phéromones

    // Attributs permettant de savoir si l'utilisateur déplace une fourmilière ou de la nourriture
    private static boolean deplaceFourmiliere = false;
    private static Nourriture deplaceNourriture;
    
    // Getters
    public int getDt(){
        return dt;
    }
    public int getNbFourmis(){
        return nombreFourmis;
    }


    /*
    ** CONSTRUCTEUR ET MÉTHODES LIÉES
    */

    public Carte() {
        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();

        importerImages();
        initialiserTerrain();
        genererObstacles();
        
        setVisible(true);
        repaint();
    }

    // Importation et redimensionnement des images qu'on importe en tant que BufferedImage
    private void importerImages() {
        try {
            // On importe les images
            imageFourmiA = ImageIO.read(new File("assets/Fourmi.png")); 
            imageFourmiB = ImageIO.read(new File("assets/FourmiMiam.png")); 
            imageFourmiliere = ImageIO.read(new File("assets/Fourmiliere.png")); 
            imageNourriture = ImageIO.read(new File("assets/Nourriture.png")); 
            imageFond = ImageIO.read(new File("assets/Fond.png")); 

            // On leur donne la taille désirée
            imageFourmiA = redimensionner(imageFourmiA, TAILLE_FOURMI);
            imageFourmiB = redimensionner(imageFourmiB, TAILLE_FOURMI);
            imageFourmiliere = redimensionner(imageFourmiliere, TAILLE_FOURMILIERE);
            imageNourriture = redimensionner(imageNourriture, TAILLE_NOURRITURE);
            imageFond = redimensionner(imageFond, 1025);

        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire les fichiers images.");
        }
    }
    
    // Redimensionne l'image de fourmi à la taille désirée
    private static BufferedImage redimensionner(BufferedImage img, int largeurVoulue) {
        int largeur = img.getWidth();
        int hauteur = img.getHeight();
        int hauteurVoulue = (largeurVoulue*hauteur)/largeur; // Simple produit en croix

        // On crée une nouvelle image vide la taille désirée
        BufferedImage nouvelleImage = new BufferedImage(largeurVoulue, hauteurVoulue, img.getType());
        Graphics2D g = nouvelleImage.createGraphics();

        // On place l'image dans cette nouvelle image de manière à ce qu'elle la remplisse, par interpolation
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, largeurVoulue, hauteurVoulue, 0, 0, largeur, hauteur, null);  
        g.dispose(); 

        return nouvelleImage;
    }

    // Initialisation de la fourmilière, des fourmis et de la nourriture
    private void initialiserTerrain() {
        fourmiliere = new Fourmiliere(300.0, 300.0, TAILLE_FOURMILIERE);
        nourritures.add(new Nourriture(600, 200, TAILLE_NOURRITURE));
        for (int i = 0; i < nombreFourmis; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
    }

    // Générations des murs
    private void genererObstacles() {
        Vecteur[] bordures = {new Vecteur(10,10), new Vecteur(1015,10), new Vecteur(1015,680-300), new Vecteur(10,680-300)};
        obstacles.add(new Obstacle(bordures, true));
        Vecteur[] obstacle1 = {new Vecteur(400,200), new Vecteur(500,200), new Vecteur(500,500), new Vecteur(400,500)};
        obstacles.add(new Obstacle(obstacle1, false));
    }

    // Méthode paint modifiée (on utilise les graphics2D pour pouvoir faire des rotations d'éléments)
    public void paint (Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        Toolkit.getDefaultToolkit().sync();
        // g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
        
        g.setColor(new Color(120,100,80));
        g.drawImage(imageFond, 0, 0, null); // Taille de l'image : 1024x698

        // On dessine la fourmilière et toutes les fourmis, phéromones et nourritures
        for (Obstacle o : obstacles) {
            o.dessine(g);
        }
        if (affichagePheromones) {
            for (Pheromone p : pheromonesAller) {
                p.dessine(g);
            }
            for (Pheromone p : pheromonesRetour) {
                p.dessine(g);
            }
        }
        for (Nourriture n : nourritures) {
            n.dessine(g, imageNourriture);
        }
        fourmiliere.dessine(g, imageFourmiliere);
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiA.class) {
                f.dessine(g,imageFourmiA);
            } else {
                f.dessine(g,imageFourmiB);
            }
        }
    }
    


    /*
    ** MÉTHODES DÉCRIVANT LE COMPORTEMENT DES FOURMIS
    */

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
            Vecteur dir = fourmis.get(i).calculAttractionPheromones(pheromonesRetour, obstacles, true);
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



    /*
    ** MÉTHODES PERMETTANT L'INTERACTION DE L'UTILISATEUR AVEC LA CARTE
    */

    // Si l'utilisateur clique sur un objet, on stocke cette information dans la carte
    public void mousePressed(MouseEvent e) {
        Vecteur sourisPos = new Vecteur(e.getX(), e.getY());
        if (sourisPos.distance(fourmiliere.getPosition()) < fourmiliere.getRayon()){
            deplaceFourmiliere = true;
        }
        for (Nourriture n : nourritures){
            if (sourisPos.distance(n.getPosition()) < n.getRayon()){
                deplaceNourriture = n;
            }
        }
    }

    // Quand l'utilisateur relâche le click, on remet à false les booleans et on met à jour les positions
    public void mouseReleased(MouseEvent e) {
        Vecteur sourisPos = new Vecteur(e.getX(), e.getY());
        if (deplaceFourmiliere) {
            fourmiliere.setPosition(sourisPos);
            deplaceFourmiliere = false;
        }
        if (deplaceNourriture != null) {
            deplaceNourriture.setPosition(sourisPos);
            deplaceNourriture = null;
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    // Quand l'utilisateur appuie sur le bouton "valider"
    public void valider(int dt, int nombreFourmis, boolean afficherPheromones){
        setDt(dt);
        changerNombreFourmis(nombreFourmis);
        changerAffichagePheromones(afficherPheromones);
        reinitialiser();
    }

    public void setDt(int nouveauDt){
        timer.stop();
        timer = new Timer(nouveauDt, this);
        timer.start();
    }

    public void changerNombreFourmis(int nombre){
        fourmis.clear();
        for (int i = 0; i < nombre; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
    }

    public void changerAffichagePheromones(boolean afficher){
        affichagePheromones = afficher;
    }

    // Relance une instance avec les paramètres actuels
    public void reinitialiser(){
        pheromonesAller.clear();
        pheromonesRetour.clear();
        int taille = fourmis.size();
        fourmis.clear();
        for (int i = 0; i < taille; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
    }

}

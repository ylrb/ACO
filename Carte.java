import javax.swing.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class Carte extends JPanel implements ActionListener, MouseListener {

    // Tous les éléments du terrain, qui sont contenus séparément dans des listes                                                                                        
    private LinkedList<Fourmi> fourmis = new LinkedList<Fourmi>();
    private LinkedList<Pheromone> pheromonesAller = new LinkedList<Pheromone>();
    private LinkedList<Pheromone> pheromonesRetour = new LinkedList<Pheromone>();
    private LinkedList<Nourriture> nourritures = new LinkedList<Nourriture>();
    private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
    private Fourmiliere fourmiliere;

    // Images et tailles
    private BufferedImage imageFourmiliere, imageNourriture, imageFond;
    private BufferedImage[] imagesFourmiA = new BufferedImage[4];
    private BufferedImage[] imagesFourmiB = new BufferedImage[4];
    protected static final int TAILLE_FOURMI = 20;
    protected static final int TAILLE_FOURMILIERE = 40;
    protected static final int TAILLE_NOURRITURE = 30;

    // Variables du timer par défaut
    private Timer timer;

    // Réglages
    private static boolean affichagePheromones ; // Doit-on visualiser les phéromones ou non
    private static int compteur = 0; // Compteur qui indique le nombre de boucles effectuées pour pouvoir espacer les phéromones
    private static final int COMPTEUR_MAX = 20; // Espacement des phéromones
    private static final double DISTANCE_PROCHE = 5; // Distance minimale à laquelle on peut placer une nouvelle phéromone par rapport à une ancienne

    // Attributs permettant de savoir si l'utilisateur déplace une fourmilière ou de la nourriture
    private static boolean deplaceFourmiliere = false;
    private static Nourriture deplaceNourriture;



    /*
    ** CONSTRUCTEUR ET MÉTHODES LIÉES
    */

    public Carte(int dt, int nombreFourmis, boolean phero) {
        affichagePheromones = phero;
        this.addMouseListener(this);

        importerImages();
        initialiserTerrain(nombreFourmis);
        genererObstacles();

        timer = new Timer(dt, this);
        timer.start();
        
        setVisible(true);
        repaint();
    }

    // Importation et redimensionnement des images qu'on importe en tant que BufferedImage
    private void importerImages() {
        try {
            // On importe les images
            imagesFourmiA[0] = ImageIO.read(new File("assets/FourmiA2.png")); 
            imagesFourmiA[1] = ImageIO.read(new File("assets/FourmiA3.png")); 
            imagesFourmiA[2] = ImageIO.read(new File("assets/FourmiA2.png")); 
            imagesFourmiA[3] = ImageIO.read(new File("assets/FourmiA1.png"));

            imagesFourmiB[0] = ImageIO.read(new File("assets/FourmiB2.png")); 
            imagesFourmiB[1] = ImageIO.read(new File("assets/FourmiB3.png")); 
            imagesFourmiB[2] = ImageIO.read(new File("assets/FourmiB2.png")); 
            imagesFourmiB[3] = ImageIO.read(new File("assets/FourmiB1.png"));

            imageFourmiliere = ImageIO.read(new File("assets/Fourmiliere.png")); 
            imageNourriture = ImageIO.read(new File("assets/Nourriture.png")); 
            imageFond = ImageIO.read(new File("assets/Fond.png")); 

            // On leur donne la taille désirée
            for (int i = 0; i < 4; i++) {
                imagesFourmiA[i] = redimensionner(imagesFourmiA[i], TAILLE_FOURMI);
                imagesFourmiB[i] = redimensionner(imagesFourmiB[i], TAILLE_FOURMI);
            }
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
    private void initialiserTerrain(int nombreFourmis) {
        fourmiliere = new Fourmiliere(300, 200, TAILLE_FOURMILIERE);
        nourritures.add(new Nourriture(600, 200, TAILLE_NOURRITURE));
        for (int i = 0; i < nombreFourmis; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
    }

    // Générations des murs
    private void genererObstacles() {
        // Créations des murs extérieurs qui sont en fait 4 obstacles collés
        Vecteur[] bordureNord = {new Vecteur(0,0), new Vecteur(80,80), new Vecteur(200,40), new Vecteur(350,30), new Vecteur(500,40), new Vecteur(650,55), new Vecteur(810,50), new Vecteur(945,80), new Vecteur(1030,0)};
        Vecteur[] bordureEst = {new Vecteur(1030,0), new Vecteur(945,80), new Vecteur(975,180), new Vecteur(970,300), new Vecteur(955,400), new Vecteur(975,520), new Vecteur(945,600), new Vecteur(1030, 700)};
        Vecteur[] bordureSud = {new Vecteur(945,600), new Vecteur(810,640), new Vecteur(650,660), new Vecteur(500,660), new Vecteur(350,630), new Vecteur(200,640), new Vecteur(80,600), new Vecteur(0, 700), new Vecteur(1030, 700)};
        Vecteur[] bordureOuest = {new Vecteur(80,600), new Vecteur(55,520), new Vecteur(70,400), new Vecteur(65,300), new Vecteur(50,180), new Vecteur(80,80), new Vecteur(0,0), new Vecteur(0, 700)};
        obstacles.add(new Obstacle(bordureNord));
        obstacles.add(new Obstacle(bordureEst));
        obstacles.add(new Obstacle(bordureSud));
        obstacles.add(new Obstacle(bordureOuest));

        Vecteur[] obstacle1 = {new Vecteur(400,200), new Vecteur(500,200), new Vecteur(500,800), new Vecteur(400,800)};
        obstacles.add(new Obstacle(obstacle1));

        // Vecteur[] obstacle2 = {new Vecteur(200,0), new Vecteur(200,300), new Vecteur(800,300), new Vecteur(800,400), new Vecteur(200,400)};
        // obstacles.add(new Obstacle(obstacle2));

        // Vecteur[] obstacle3 = {new Vecteur(800,0), new Vecteur(800,400)};
        // obstacles.add(new Obstacle(obstacle3));
    }

    // Méthode paint modifiée (on utilise les graphics2D pour pouvoir faire des rotations d'éléments)
    public void paint (Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        Toolkit.getDefaultToolkit().sync();
        
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
                f.dessine(g,imagesFourmiA);
            } else {
                f.dessine(g,imagesFourmiB);
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
            if (p.getTaux()<=5) {
                tauxTropBasAller++;
            }
            p.estompe();
        }
        for (Pheromone p : pheromonesRetour) {
            if (p.getTaux()<=5) {
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
    /* 
     * Lorsque les fourmis créent un chemin, les phéromones sont très concentrées.
     * Et puisque cela engendre beaucoup de calculs, on observe de grands ralentissements.
     * Donc pour réduire ce lag, on place une phéromone seulement si elle est assez loin des phéromones déjà existantes.
    */
    private void ajoutPheromones() {
        if (compteur>COMPTEUR_MAX) {
            for (Fourmi f : fourmis) {
                boolean assezLoin = true; // Si la phéromone à plcaer est assez loin des autres phéromones existantes
                if (f.getClass() == FourmiA.class) {
                    FourmiA fA = (FourmiA) f;
                    for (Pheromone p : pheromonesAller) {
                        if (f.position.distance(p.position) < DISTANCE_PROCHE) {
                            assezLoin = false;
                            break;
                        }
                    }
                    if (assezLoin) {
                        pheromonesAller.add(fA.deposerPheromoneAller());
                    }
                } else {
                    FourmiB fB = (FourmiB) f;
                    for (Pheromone p : pheromonesRetour) {
                        if (f.position.distance(p.position) < DISTANCE_PROCHE) {
                            assezLoin = false;
                            break;
                        }
                    }
                    if (assezLoin) {
                        pheromonesRetour.add(fB.deposerPheromoneRetour());
                    }
                }
            }
            compteur=0;
        }
        compteur++;
    }

    // Les fourmis changent d'état si elles ont atteint leur objectif (nourriture/fourmilière)
    private void changementFourmis() {
    
        // On stocke les indices de toutes les fourmis à changer
        LinkedList<Integer> indices = new LinkedList<Integer>();
        
        // On parcout la LinkedList de fourmis à la recherche d'une fourmiA qui a trouvé de la nourriture
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiA.class) {
                for (Nourriture n : nourritures) {
                    if (f.getPosition().distance(n.getPosition()) < 1.5*n.getRayon()) {
                        indices.add(fourmis.indexOf(f));
                    }
                }
            }
        }

        // On change le type de ces fourmis (on ne peut pas le faire à l'intérieur du for each donc on a recours aux indices)
        for (Integer i : indices) {
            Vecteur pos = fourmis.get(i).getPosition();
            Vecteur dir = fourmis.get(i).getDirection(); // Il faut conserver la direction initiale de la fourmi
            dir.inverser(); // Puis il faut l'inverser pour que la fourmi reparte en arrière
            fourmis.remove((int)i);
            fourmis.add(new FourmiB(pos,dir));
        }
        indices.clear();

        // On reparcourt la LinkedList de fourmis à la recherche d'une fourmiB qui a atteint la fourmilière
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiB.class) {
                if (f.getPosition().distance(fourmiliere.getPosition()) < 1.0) {
                    indices.add(fourmis.indexOf(f));
                }
            }
        }

        // On change le type de ces fourmis
        for (Integer i : indices) {
            fourmiliere.depot(); // La fourmi dépose la nourriture dans la fourmilière
            Vecteur pos = fourmis.get(i).getPosition();
            LinkedList<Segment> murs = new LinkedList<Segment>();
            for (Obstacle o : obstacles) {
                murs.addAll(o.getMurs());
            }
            Vecteur dir = fourmis.get(i).calculAttractionPheromones(pheromonesRetour, murs, true);
            if ((dir.x == 0)&(dir.y == 0)) { // S'il n'y a aucune phéromone autour de la fourmi
                dir = new Vecteur(Math.random(),Math.random());
                dir.unitaire();
            }
            fourmis.remove((int)i); 
            fourmis.add(new FourmiA(pos,dir));
        }
        indices.clear();
        
        /* 
         * Il n'est pas possible de faire les deux tris en parallèle.
         * Imaginons que nous soyons dans le cas où une fourmi 1 arrive à la fourmilière en même qu'une fourmi 2 trouve de la nourriture.
         * Il faudrait alors supprimer la fourmi 1, d'incide i, puis ensuite la fourmi 2, d'incide j.
         * Or, supprimer la fourmi i décale vers la gauche tous les éléments de la liste.
         * Ainsi, si elle se trouvait après la fourmi 1 dans la LinkedList, la fourmi 2 se trouverait alors à l'indice j-1.
         * Ce serait donc la fourmi après elle qui serait supprimée à sa place.
        */
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

    // Arrête les calculs d'une ancienne instance
    public void stop(){
        timer.stop();
    }

}

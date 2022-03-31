import javax.swing.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
    private BufferedImage imageFourmiA, imageFourmiB, imageFourmiliere, imageNourriture, imageFond;
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
    private static final double PORTEE_VUE_INITIALE = 60; // Portée de vue des fourmis qui sortent de la fourmilière

    // Attributs permettant de savoir si l'utilisateur déplace une fourmilière ou de la nourriture
    private static boolean deplaceFourmiliere = false;
    private static Nourriture deplaceNourriture;

    // Accesseurs
    public Timer getTimer() {
        return timer;
    }
    public void setObstacles(LinkedList<Obstacle> o){
        obstacles = o;
        // Ajouts des bordures invisibles
        LecteurCarte borduresInvisibles = new LecteurCarte("assets/cartes/borduresInvisibles.txt");
        for(Obstacle obs : borduresInvisibles.getObstacles()){
            obstacles.add(obs);
        }
    }


    /*
    ** CONSTRUCTEUR ET MÉTHODES LIÉES
    */

    public Carte(int dt, int nombreFourmis, boolean phero, LinkedList<Obstacle> obs, LinkedList<Nourriture> nour, Fourmiliere fourm) {
        affichagePheromones = phero;
        this.addMouseListener(this);
        fourmiliere = fourm;
        nourritures = nour;

        importerImages();
        initialiserTerrain(nombreFourmis);
        setObstacles(obs);

        timer = new Timer(dt, this);
        timer.start();
        
        setVisible(true);
        repaint();
    }

    // Importation et redimensionnement des images qu'on importe en tant que BufferedImage
    private void importerImages() {
        try {
            // On importe les images
            imageFourmiA = ImageIO.read(new File("assets/images/FourmiA.png")); 
            imageFourmiB = ImageIO.read(new File("assets/images/FourmiB.png")); 
            imageFourmiliere = ImageIO.read(new File("assets/images/Fourmiliere.png")); 
            imageNourriture = ImageIO.read(new File("assets/images/Nourriture.png")); 
            imageFond = ImageIO.read(new File("assets/images/Fond.png")); 

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
    private void initialiserTerrain(int nombreFourmis) {
        for (int i = 0; i < nombreFourmis; i++) {
            fourmis.add(new FourmiA(fourmiliere.getPosition()));
        }
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
        LinkedList<Pheromone> pheromonesAllerSup = new LinkedList<Pheromone>();
        LinkedList<Pheromone> pheromonesRetourSup = new LinkedList<Pheromone>();

        // On fait s'estomper les phéromones et on stocke les phéromones avec un taux trop bas
        for (Pheromone p : pheromonesAller) {
            if (p.getTaux()<=5) {
                pheromonesAllerSup.add(p);
            }
            p.estompe();
        }
        for (Pheromone p : pheromonesRetour) {
            if (p.getTaux()<=5) {
                pheromonesRetourSup.add(p);
            }
            p.estompe();
        }

        // Les phéromones qui ont un taux trop faible sont supprimées
        for (Pheromone p : pheromonesAllerSup) {
            pheromonesAller.remove(p);
        }
        for (Pheromone p : pheromonesRetourSup) {
            pheromonesRetour.remove(p);
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
                boolean assezLoin = true; // Si la phéromone à placer est assez loin des autres phéromones existantes
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
                        PheroRetour p = fB.deposerPheromoneRetour();
                        if (p.getTaux() > 5) {
                            pheromonesRetour.add(p);
                        }
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
        LinkedList<Fourmi> fourmisSup = new LinkedList<Fourmi>();
        
        // On parcout la LinkedList de fourmis à la recherche d'une fourmiA qui a trouvé de la nourriture
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiA.class) {
                for (Nourriture n : nourritures) {
                    if (f.getPosition().distance(n.getPosition()) < 1.5*n.getRayon()) {
                        fourmisSup.add(f);
                    }
                }
            }
        }

        // On fait un bruitage si une fourmi mange de la nourriture
        if (fourmisSup.size() > 0) {
            jouerSon("crunch.wav", compteur);
        }

        // On change le type de ces fourmis (on ne peut pas le faire à l'intérieur du for each donc on a recours aux indices)
        for (Fourmi f : fourmisSup) {
            Vecteur pos = f.getPosition();
            Vecteur dir = f.getDirection(); // Il faut conserver la direction initiale de la fourmi
            dir.inverser(); // Puis il faut l'inverser pour que la fourmi reparte en arrière
            fourmis.remove(f);
            fourmis.add(new FourmiB(pos,dir));
        }
        fourmisSup.clear();

        // On reparcourt la LinkedList de fourmis à la recherche d'une fourmiB qui a atteint la fourmilière
        for (Fourmi f : fourmis) {
            if (f.getClass() == FourmiB.class) {
                if (f.getPosition().distance(fourmiliere.getPosition()) < 1.0) {
                    fourmisSup.add(f);
                }
            }
        }

        // On stocke dans des LinkedList les phéromones autour de la fourmilière à cet instant (utile pour après)
        LinkedList<Pheromone> pheromones = new LinkedList<Pheromone>();
        double distance;
            for (Pheromone p : pheromonesAller) {
            distance = fourmiliere.getPosition().distance(p.position);
            if ((distance < PORTEE_VUE_INITIALE)&&(distance > fourmiliere.getRayon())) {
                pheromones.add(p);
            }
        }
        
        LinkedList<Segment> murs = new LinkedList<Segment>();
        // On change le type de ces fourmis
        for (Fourmi fS : fourmisSup) {
            fourmiliere.depot(); // La fourmi dépose la nourriture dans la fourmilière
            fourmis.remove(fS); // On supprime l'ancienne fourmi
            
            // On crée la nouvelle FourmiA à la position de la fourmilière
            FourmiA f = new FourmiA(fourmiliere.getPosition());
            fourmis.add(f);

            // On détermine la direction initiale en fonction des phéromones et des murs
            f.setDirection(f.calculAttractionPheromones(pheromones, murs));
            
        }
        fourmisSup.clear();
        
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

    public static synchronized void jouerSon(final String url, int dt) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Clip son = AudioSystem.getClip();
                    AudioInputStream flux = AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("assets/sons/"+url));
                    son.open(flux);
                    son.start(); 
                } catch (Exception e) {
                }
            }
        }).start();
    }

    

}

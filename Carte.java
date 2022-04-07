import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.geom.*;

public class Carte extends JPanel implements ActionListener, MouseListener {

    // Tous les éléments du terrain, qui sont contenus séparément dans des listes
    private LinkedList<Fourmi> fourmis = new LinkedList<Fourmi>();
    private LinkedList<Pheromone> pheromonesAller = new LinkedList<Pheromone>();
    private LinkedList<Pheromone> pheromonesRetour = new LinkedList<Pheromone>();
    private LinkedList<Nourriture> nourritures = new LinkedList<Nourriture>();
    private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
    private LinkedList<Segment> murs = new LinkedList<Segment>();
    private Fourmiliere fourmiliere;
    int nombreFourmis; // Nombre indiqué par l'utilisateur
    int compteurFourmis; // Nombre actuel de fourmis, qui augmente jusqu'à nombreFourmis

    // Variables du timer par défaut
    private Timer timer;

    // Réglages
    public static boolean affichagePheromones = true; // Doit-on visualiser les phéromones ou non
    public static boolean afficherFourmis = true; // Si on affiche les fourmis pour le mode éditeur
    public static boolean bruitages = false; // Si les bruitages sont activés
    private static int compteur = 0; // Compteur qui indique le nombre de boucles effectuées pour pouvoir espacer les phéromones
    private static final int COMPTEUR_MAX = 20; // Espacement des phéromones
    private static final double DISTANCE_PROCHE = 5; // Distance minimale à laquelle on peut placer une nouvelle phéromone par rapport à une ancienne
    private static final double PORTEE_VUE_INITIALE = 60; // Portée de vue des fourmis qui sortent de la fourmilière
    private static final int DELAI_DEPLOIEMENT = 10; // Vitesse à laquelle les fourmis sortent de la fourmilère à l'initalisation

    // Attributs permettant de savoir si l'utilisateur déplace une fourmilière ou de la nourriture
    private static boolean deplaceFourmiliere = false;
    private static Nourriture deplaceNourriture;

    // Attributs du mode éditeur
    private boolean obstacleEnCours = false;
    private LinkedList<Vecteur> points = new LinkedList<Vecteur>();
    private final int RAYON_POINT = 5;

    // Accesseurs
    public Timer getTimer() {
        return timer;
    }
    public void setObstacles(LinkedList<Obstacle> obs) {
        obstacles = obs;
        // Ajouts des bordures invisibles
        LecteurCarte borduresInvisibles = new LecteurCarte("assets/cartes/borduresInvisibles.txt");
        for (Obstacle o : borduresInvisibles.getObstacles()) {
            o.setVide(false);
            obstacles.add(o);
        }
    }



    /*
     ** CONSTRUCTEUR ET MÉTHODES LIÉES
     */

    public Carte(LinkedList<Obstacle> obs, LinkedList<Nourriture> nour, Fourmiliere fourm) {
        this.addMouseListener(this);
        nombreFourmis = Parametres.getNombreFourmis();
        compteurFourmis = 0;
        fourmiliere = fourm;
        nourritures = nour;
        setObstacles(obs);

        // On remplit l'ArrayList murs
        for (Obstacle o : obstacles) {
            for (Segment s : o.getMurs()) {
                murs.add(s);
            }
        }

        timer = new Timer(Parametres.getDt(), this);
        timer.start();

        setVisible(true);
        repaint();
    }

    // Méthode paint modifiée (on utilise les graphics2D pour pouvoir faire des rotations d'éléments)
    public void paint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        Toolkit.getDefaultToolkit().sync();

        g.setColor(new Color(120, 100, 80));
        g.drawImage(Parametres.imageFond, 0, 0, null); // Taille de l'image : 1024x698

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
            n.dessine(g, Parametres.imageNourriture);
        }
        fourmiliere.dessine(g, Parametres.imageFourmiliere);
        for (Fourmi f : fourmis) {
            if (f instanceof FourmiA) {
                f.dessine(g, Parametres.imageFourmiA);
            } else {
                f.dessine(g, Parametres.imageFourmiB);
            }
        }

        // Mode éditeur
        for(Vecteur v : points){
            if (v == points.get(0)){
                g.setColor(new Color(0, 0, 0, 150));
            }
            else{
                g.setColor(new Color(0, 0, 0, 70));
            }
            g.fill(new Ellipse2D.Double(v.x-RAYON_POINT, v.y-RAYON_POINT, 2*RAYON_POINT, 2*RAYON_POINT));
        }
    }



    /*
     * MÉTHODES DÉCRIVANT LE COMPORTEMENT DES FOURMIS
     */

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (Parametres.modeEditeur){
                afficherFourmis = false;
            } else {
                afficherFourmis = true;
            }
            if (afficherFourmis){
                ajoutInitialFourmis(); // Rajoute des fourmis dans la liste jusqu'à atteindre le nombre indiqué
                updatePheromones(); // Mise à jour des phéromones actuelles
                ajoutPheromones(); // On ajoute les nouvelles phéromones
                changementFourmis(); // Changement des fourmis en type A ou B si elles ont atteint la fourmilière/nourriture
                deplacementFourmis(); // Déplacement des fourmis selon leur type et gestion des murs
            }
            repaint();
        }
    }

    /*
     * Méthode qui permet d'ajouter une fourmi toutes les 'VITESSE_DEPLOIEMENT'
     * itérations de la boucle (pour que toutes les fourmis ne partent pas en même
     * temps de la fourmilière).
     */
    private void ajoutInitialFourmis() {
        if (compteurFourmis < DELAI_DEPLOIEMENT * nombreFourmis) {
            compteurFourmis++;
            if (compteurFourmis % DELAI_DEPLOIEMENT == 0) {
                fourmis.add(new FourmiA(fourmiliere.getPosition()));
            }
        }
    }

    // Les phéromones disparaissent si leur taux est trop faible
    private void updatePheromones() {
        LinkedList<Pheromone> pheromonesAllerSup = new LinkedList<Pheromone>();
        LinkedList<Pheromone> pheromonesRetourSup = new LinkedList<Pheromone>();

        // On fait s'estomper les phéromones et on stocke les phéromones avec un taux trop bas
        for (Pheromone p : pheromonesAller) {
            if (p.getTaux() <= 5) {
                pheromonesAllerSup.add(p);
            }
            p.estompe();
        }
        for (Pheromone p : pheromonesRetour) {
            if (p.getTaux() <= 5) {
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
    
    /*
     * On rajoute des phéromones toutes les COMPTEUR_MAX itérations de la boucle.
     * Lorsque les fourmis créent un chemin, les phéromones sont très concentrées.
     * Et puisque cela engendre beaucoup de calculs, on observe de grands ralentissements.
     * Donc pour réduire ce lag, on place une phéromone seulement si elle est assez
     * loin des phéromones déjà existantes.
     */
    private void ajoutPheromones() {
        if (compteur > COMPTEUR_MAX) {
            for (Fourmi f : fourmis) {
                boolean assezLoin = true; // Si la phéromone à placer est assez loin des autres phéromones existantes
                if (f instanceof FourmiA) {
                    FourmiA fA = (FourmiA) f;
                    for (Pheromone p : pheromonesAller) {
                        if (f.position.distance(p.getPosition()) < DISTANCE_PROCHE) {
                            assezLoin = false;
                            break;
                        }
                    }
                    if (assezLoin) {
                        Pheromone p = fA.deposerPheromone();
                        if (p.getTaux() > 5) {
                            pheromonesAller.add(p);
                        }
                    }
                } else {
                    FourmiB fB = (FourmiB) f;
                    for (Pheromone p : pheromonesRetour) {
                        if (f.position.distance(p.getPosition()) < DISTANCE_PROCHE) {
                            assezLoin = false;
                            break;
                        }
                    }
                    if (assezLoin) {
                        Pheromone p = fB.deposerPheromone();
                        if (p.getTaux() > 5) {
                            pheromonesRetour.add(p);
                        }
                    }
                }
            }
            compteur = 0;
        }
        compteur++;
    }

    // Les fourmis changent d'état si elles ont atteint leur objectif (nourriture/fourmilière)
    private void changementFourmis() {

        // On stocke les indices de toutes les fourmis à changer
        LinkedList<Fourmi> fourmisSup = new LinkedList<Fourmi>();

        // On parcout la LinkedList de fourmis à la recherche d'une fourmiA qui a trouvé de la nourriture
        for (Fourmi f : fourmis) {
            if (f instanceof FourmiA) {
                for (Nourriture n : nourritures) {
                    if (f.getPosition().distance(n.getPosition()) < 1.5 * n.getRayon()) {
                        fourmisSup.add(f);
                    }
                }
            }
        }

        // On change le type de ces fourmis (on ne peut pas le faire à l'intérieur du for each)
        for (Fourmi f : fourmisSup) {
            Vecteur pos = f.getPosition();
            Vecteur dir = f.getDirection(); // Il faut conserver la direction initiale de la fourmi
            dir.inverser(); // Puis il faut l'inverser pour que la fourmi reparte en arrière
            fourmis.remove(f);
            fourmis.add(new FourmiB(pos, dir));
            jouerSon("bop");
        }
        fourmisSup.clear();

        // On reparcourt la LinkedList de fourmis à la recherche d'une fourmiB qui a atteint la fourmilière
        for (Fourmi f : fourmis) {
            if (f instanceof FourmiB) {
                if (f.getPosition().distance(fourmiliere.getPosition()) < 1.0) {
                    fourmisSup.add(f);
                }
            }
        }

        // On stocke dans des LinkedList les phéromones autour de la fourmilière à cet instant (utile pour après)
        LinkedList<Pheromone> pheromones = new LinkedList<Pheromone>();
        double distance;
        for (Pheromone p : pheromonesRetour) {
            distance = fourmiliere.getPosition().distance(p.getPosition());
            if ((distance < PORTEE_VUE_INITIALE) && (distance > fourmiliere.getRayon())) {
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
            jouerSon("tik");

            // On détermine la direction initiale en fonction des phéromones
            f.setDirection(f.calculAttractionPheromones(pheromones, murs));

        }
        fourmisSup.clear();
    }

    // Déplacement des fourmis selon leur type et gestion des murs
    private void deplacementFourmis() {
        for (Fourmi f : fourmis) {
            if (f instanceof FourmiA) {
                f.avancer(nourritures, fourmiliere, pheromonesRetour, murs);
            } else {
                f.avancer(nourritures, fourmiliere, pheromonesAller, murs);
            }
        }
    }



    /*
     ** MÉTHODES PERMETTANT L'INTERACTION DE L'UTILISATEUR AVEC LA CARTE
     */

    // Si l'utilisateur clique sur un objet, on stocke cette information dans la carte
    public void mousePressed(MouseEvent e) {
        Vecteur sourisPos = new Vecteur(e.getX(), e.getY());
        if (sourisPos.distance(fourmiliere.getPosition()) < fourmiliere.getRayon()) {
            deplaceFourmiliere = true;
        }
        for (Nourriture n : nourritures) {
            if (sourisPos.distance(n.getPosition()) < n.getRayon()) {
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
        int x = e.getX();
        int y = e.getY();
        Vecteur positionClic = new Vecteur(x,y);
        if (Parametres.modeEditeur){
            // Création d'un ensemble de points pour former un obstacle
            if (obstacleEnCours){
                if(points.get(0).distance(positionClic) < 10){ // Clique sur le premier point de départ
                    Obstacle nouvelObstacle = new Obstacle(points);
                    points.clear();
                    obstacles.add(nouvelObstacle);
                    for(Segment s : nouvelObstacle.getMurs()){
                        murs.add(s);
                    }
                    obstacleEnCours = false;
                }
                else{
                    points.add(positionClic);
                }
            }
            // Initialisation de l'ensemble de points qui va former un obstacle
            else {
                obstacleEnCours = true;
                points.add(positionClic);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    // Relance une instance avec les paramètres actuels
    public void reinitialiser() {
        pheromonesAller.clear();
        pheromonesRetour.clear();
        fourmis.clear();
        compteurFourmis = 0;
        fourmiliere.resetNourriture();
    }

    // Permet de jouer un son
    public void jouerSon(String s) {
        if (bruitages) {
            try {
                File fichier = new File("assets/sons/" + s + ".wav");
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(fichier));
                clip.start();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

}

import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public abstract class Fourmi extends Element {
    
    // La fourmi possède aussi un vecteur() position et une couleur, héritées de la classe mère Element
    protected double vitesse;

    //////////////////////// Vecteurs
    protected Vecteur direction;
    protected Vecteur errance;

    //////////////////////// Tous les coefficients appliqués aux vecteurs (caractéristiques de la fourmi)
    protected static final double COEFF_ERRANCE = 0.1; // Poids du vecteur errance
    protected static final double AMPLITUDE_ERRANCE = 5; // Amplitude max de la variation du vecteur errance
    protected static final double COEFF_ATTRACTION_NOURRITURE = 10; // Poids du vecteur force d'attraction de la nourriture
    protected static final double COEFF_ATTRACTION_FOURMILIERE = 10; // Poids du vecteur force d'attraction de la nourriture
    protected static final double COEFF_ATTRACTION_PHEROMONES = 1; // Poids du vecteur force d'attraction des phéromones
    protected static final double PORTEE_VUE = 70; // Distance à laquelle les fourmis peuvent voir les nourritures, pheromones, la fourmilière etc..
    protected static final double ANGLE_VUE = 1; // Angle de vision des fourmis (en radians)

    public Fourmi(double x, double y) {
        position = new Vecteur(x,y);
        vitesse = 2.0;
        direction = new Vecteur(2*Math.random()-1,2*Math.random()-1);
        direction.unitaire();
        errance = direction;
    }

    public Fourmi(double x, double y, Vecteur dir) {
        this(x,y);
        direction = dir;
    }

    public Vecteur getDirection() {
        double x = direction.x;
        double y = direction.y;
        return new Vecteur(x,y);
    }

    public void avancer(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, ArrayList<Pheromone> pheromones) {
        calculErrance();
        calculNouvelleDirection(nourritures, fourmiliere, pheromones);
        position.x += vitesse*direction.x;
        position.y += vitesse*direction.y;
    }

    // Fait varier le vecteur errance
    private void calculErrance() {
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
    }

    // Renvoie true si la fourmi a des phéromones dans son champ de vision
    protected boolean pheromonesEnVue(ArrayList<Pheromone> pheromones) {
        boolean rep = false;
        Vecteur distance = new Vecteur(); // Vecteur qui va de la fourmi à la phéromone en question
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            // Met rep à true si l'angle entre la direction des la fourmi et ce vecteur distance est inférieur à ANGLE_VUE
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&(direction.angle(distance) < ANGLE_VUE)) {
                rep = true;
                break; // On utilise un break pour réduire les calculs ; il n'est pas possible d'utiliser un while puisque l'on traverse une arraylist
            }
        }
        return rep;
    }

    // Calcul de l'attraction d'une fourmiA à une nourriture dans son champ de vision
    protected Vecteur calculAttractionPheromones(ArrayList<Pheromone> pheromones, boolean initial) {
        Vecteur rep = new Vecteur();
        Vecteur distance = new Vecteur(); // Vecteur qui va de la fourmi à la phéromone en question
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            // Augmente rep si l'angle entre la direction des la fourmi et ce vecteur distance est inférieur à ANGLE_VUE
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&((direction.angle(distance) < ANGLE_VUE)||(initial))) {
                rep = rep.somme(p.getPosition().soustrait(getPosition()),1,p.getTaux()/100);
            }
        }
        rep.unitaire();
        return rep;
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    protected abstract void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, ArrayList<Pheromone> pheromones);

    public void dessine(Graphics g, BufferedImage imageFourmi) {
        double r = imageFourmi.getWidth()/2; // Le rayon de la fourmi est égal à la moitié de la hauteur de son image
        g.setColor(Color.BLUE);
        g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*direction.x),(int)(position.y+50*direction.y));
        g.drawImage(orienterFourmi(imageFourmi), (int)(position.x-r), (int)(position.y-r), null);
    }
    
    // Méthode hérité de Element qui ne sert pas car on souhaite avoir imageFourmi en attribut de dessine()
    public void dessine(Graphics g) {}

    // Fait tourner l'image de fourmi de manière à ce qu'elle soit dirigée dans le sens du vecteur direction
    public BufferedImage orienterFourmi(BufferedImage imageFourmi) {
        BufferedImage img = imageFourmi;
        int largeur = img.getWidth();
        int hauteur = img.getHeight();
        int type = img.getType();

        BufferedImage nouvelleImage = new BufferedImage(largeur, hauteur, type);
        Graphics2D g2 = nouvelleImage.createGraphics();

        // On doit distinguer les cas où l'angle est compris dans [0;pi] ou [-pi;0]
        if (direction.x<=0) {
            g2.rotate(direction.angle(new Vecteur(0,100))+Math.PI, largeur/2, hauteur/2);
        } else {
            g2.rotate(-direction.angle(new Vecteur(0,100))+Math.PI, largeur/2, hauteur/2);
        }
        g2.drawImage(img, null, 0, 0);
        
        return nouvelleImage;
    }
}
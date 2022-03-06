import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Fourmi extends Element {

    // Vecteurs et variables (la fourmi possède aussi un vecteur position, hérité de la classe mère Element)
    protected Vecteur direction;
    protected Vecteur errance;
    protected double vitesse;

    // Tous les coefficients des forces (leur poids)
    protected static final double COEFF_ERRANCE = 0.1;
    protected static final double COEFF_ATTRACTION_PHEROMONES = 1;
    protected static final double COEFF_ATTRACTION_NOURRITURE = 10;
    protected static final double COEFF_ATTRACTION_FOURMILIERE = 10;
    
    // Grandeurs définies
    protected static final double AMPLITUDE_ERRANCE = 5; // Amplitude max de la variation du vecteur errance
    protected static final double PORTEE_VUE = 70; // Distance à laquelle les fourmis peuvent voir les nourritures, pheromones, la fourmilière etc..
    protected static final double ANGLE_VUE = 1; // Angle de vision des fourmis (en radians)

    private static final boolean AFFICHAGE_DIRECTION = false; // Doit-on visualiser la direction de la fourmi

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
        return new Vecteur(direction.x,direction.y);
    }

    // Fait avancer la fourmi dans la nouvelle direction qui est déterminée selon son environnement
    public void avancer(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, ArrayList<Pheromone> pheromones) {
        calculErrance();
        calculNouvelleDirection(nourritures, fourmiliere, pheromones);
        position.x += vitesse*direction.x;
        position.y += vitesse*direction.y;
    }

    // Calcule la nouvelle orientation du vecteur errance
    private void calculErrance() { 
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    protected abstract void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, ArrayList<Pheromone> pheromones);
    
    // Indique si la fourmi a des phéromones dans son champ de vision
    protected boolean pheromonesEnVue(ArrayList<Pheromone> pheromones) { 
        boolean rep = false;
        Vecteur distance = new Vecteur();
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&(direction.angle(distance) < ANGLE_VUE)) {
                // Met rep à true si la phéromone se trouve dans le champ de visions de la fourmi
                rep = true;
                break;
            }
        }
        return rep;
    }

    // Calcule l'attraction d'une fourmi aux nourritures dans son champ de vision
    protected Vecteur calculAttractionPheromones(ArrayList<Pheromone> pheromones, boolean initial) {
        Vecteur rep = new Vecteur();
        Vecteur distance = new Vecteur();
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&((direction.angle(distance) < ANGLE_VUE)||(initial))) {
                // Augmente rep si la phéromone se trouve dans le champ de vision de la fourmi
                rep = rep.somme(p.getPosition().soustrait(getPosition()),1,p.getTaux()/100);
            }
        }
        rep.unitaire();
        return rep;
    }

    // Dessine une fourmi à la position de la fourmi
    public void dessine(Graphics g, BufferedImage imageFourmi) {
        double r = imageFourmi.getWidth()/2; // Le rayon de la fourmi est égal à la moitié de la hauteur de son image
        if (AFFICHAGE_DIRECTION) {
            g.setColor(Color.BLUE);
            g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*direction.x),(int)(position.y+50*direction.y));
        }
        g.drawImage(orienterFourmi(imageFourmi), (int)(position.x-r), (int)(position.y-r), null);
    }
    
    // Méthode héritée de Element qui ne sert pas car on souhaite avoir imageFourmi en attribut de dessine()
    public void dessine(Graphics g) {}

    // Fait tourner l'image de fourmi de manière à ce qu'elle soit dirigée dans le sens du vecteur direction
    private BufferedImage orienterFourmi(BufferedImage imageFourmi) {
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
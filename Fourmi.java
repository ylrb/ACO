import java.awt.*;
import java.util.ArrayList;

public abstract class Fourmi {

    protected Vecteur position; // Position de la fourmi
    protected Color couleur;
    protected double vitesse;

    //////////////////////// Vecteurs
    protected Vecteur direction;
    protected Vecteur errance;

    //////////////////////// Tous les coefficients appliqués aux vecteurs (caractéristiques de la fourmi)
    protected static final double COEFF_ERRANCE = 0.1; // Poids du vecteur errance
    protected static final double AMPLITUDE_ERRANCE = 10; // Amplitude max de la variation du vecteur errance
    protected static final double COEFF_ATTRACTION_NOURRITURE = 10; // Poids du vecteur force d'attraction de la nourriture
    protected static final double COEFF_ATTRACTION_FOURMILIERE = 10; // Poids du vecteur force d'attraction de la nourriture
    protected static final double PORTEE_VUE = 50; // à quelle distance les fourmis peuvent voir les nourritures, pheromones, la fourmilière etc..

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

    public Vecteur getPosition() {
        double x = position.x;
        double y = position.y;
        return new Vecteur(x,y);
    }

    public Vecteur getDirection() {
        return direction;
    }

    public void avancer(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere) {
        calculErrance();
        calculNouvelleDirection(nourritures, fourmiliere);
        position.x += vitesse*direction.x;
        position.y += vitesse*direction.y;
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    public abstract void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere);

    // Fait varier le vecteur errance
    public void calculErrance() {
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
    }

    // Indique s'il y a des phéromones en vue
    public abstract boolean pheromonesEnVue();

    public void dessine (Graphics g) {
        double r = 7;
        g.setColor(Color.BLUE);
        g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*direction.x),(int)(position.y+50*direction.y));
        g.setColor(couleur);
        g.fillOval((int)(position.x-r), (int)(position.y-r), (int)(2*r), (int)(2*r));
    }

    // Retourne la distance à un point défini par son vecteur position 'point'
    public double distanceA(Vecteur point) {
        return (Math.sqrt((position.x-point.x)*(position.x-point.x)+(position.y-point.y)*(position.y-point.y)));
    }
}
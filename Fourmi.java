import java.awt.*;
import java.util.ArrayList;

public class Fourmi {

    private double x;
    private double y;
    private double vitesse;
    private boolean porteNourriture; // Si la fourmi porte de la nourriture
    private Color couleur = Color.RED;

    //////////////////////// Vecteurs
    private Vecteur direction;
    private Vecteur errance;
    private Vecteur attractionNourriture;

    //////////////////////// Tous les coefficients appliqués au vecteur (~caracteristique de la fourmi)
    private static final double COEFF_ERRANCE = 0.1; // Poids du vecteur errance
    private static final double AMPLITUDE_ERRANCE = 10; // Amplitude max de la variation du vecteur errance
    private static final double COEFF_ATTRACTION_NOURRITURE = 10; // Poids du vecteur force d'attraction de la nourriture
    private static final double PORTEE_VUE = 50; // à quelle distance les fourmis peuvent voir les nourritures, pheromones, la fourmilière etc..

    ///////////////////////// Liste des éléments au alentour de la fourmi
    ArrayList<Nourriture> nourritureProche = new ArrayList<Nourriture>();

    public Fourmi(double X, double Y) {
        x = X;
        y = Y;  
        vitesse = 2.0;
        porteNourriture = false;
        direction = new Vecteur(Math.random(),Math.random()); // La direction initiale de la fourmi est aléatoire
        direction.unitaire();
        errance = direction;
        attractionNourriture = new Vecteur();
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public void avancer(ArrayList<Nourriture> n) {
        calculErrance();
        calculNouvelleDirection(n);
        x += vitesse*direction.x;
        y += vitesse*direction.y;
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    public void calculNouvelleDirection(ArrayList<Nourriture> n) {
        if (!porteNourriture) {         //la fourmi ne porte pas de nourriture, elle est donc a la recherche de nourriture
            if (nourritureEnVue(n)) {           // la fourmi a vu de la nourriture et donc se diriger vers elle
                direction = direction.somme(calculAttractionNourriture(), 1, COEFF_ATTRACTION_NOURRITURE);
                direction.unitaire();
            } else if (pheroRetourEnVue()) {    // la fourmi voit pas de nourriture mais elle a vu des pheromones retour et donc les "suivre"

            } else {                             // La fourmi est en mode recherche (elle ne voit pas de nourriture ou de mur)
                direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
                direction.unitaire();
            }
        } else if (fourmilièreEnVue()) { // la fourmi porte de la nourriture et recherche alors la fourmiliere, ici elle le voit et va donc se diriger vers ce dernier

        } else if (pheroAllerEnVue()){              // le fourmi ne voit pas la fourmiliere mais a vu des pheromone aller et va donc les suivre

        } else {                                    // la fourmi ne voit ni la fourmiliere ni des pheromones aller et va donc errer
            direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
            direction.unitaire();
        }
    }



    // Fais varier le vecteur errance
    public Vecteur calculErrance() {
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
        return errance;
    }
    public Vecteur calculAttractionNourriture() {
        Vecteur rep = new Vecteur();
        for (Nourriture n : nourritureProche) {
            rep = rep.somme(n.position.soustrait(new Vecteur(x, y)));
        }
        rep.unitaire();
        return rep;
    }

    // Indique s'il y a des phéromones en vue
    public boolean pheroRetourEnVue() {         // A ECRIRE // en parametre : liste des pheromones retour sur la map
        return false;
    }
    private boolean pheroAllerEnVue() {         // A ECRIRE // en parametre : liste des pheromones aller sur la map
        return false;
    }
    // indique si ya la foumiliere en vue
    private boolean fourmilièreEnVue() {         // A ECRIRE // en parametre : position de la fourmiliere
        return false;
    }
    // Indique s'il y a de la nourriture en vue
    public boolean nourritureEnVue(ArrayList<Nourriture> nourritures) {
        boolean rep = false;
        for ( Nourriture n : nourritures) {
            if (this.distanceA(n.position) < PORTEE_VUE) {
                nourritureProche.add(n);
                rep = true;
            }
        }
        return rep;
    }

    public void dessine (Graphics g) {
        double r = 7;
        g.setColor(Color.BLUE);
        g.drawLine((int)x, (int)y,(int)(x+50*direction.x),(int)(y+50*direction.y));
        g.setColor(couleur);
        g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
    }

    public double distanceA(Vecteur APoint) {           // retourne la distance à un point dont la position est donnéee par le vecteur APoint
        return (Math.sqrt((this.x-APoint.x)*(this.x-APoint.x)+(this.y-APoint.y)*(this.y-APoint.y)));
    }
    public double distanceA(int x, int y) {           // retourne la distance à un point dont la position est donnéee les coord ENTIERE x et y
        return (Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y)));
    }
}
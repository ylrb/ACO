import java.awt.*;
import java.util.ArrayList;

public class Fourmi {

    private Vecteur position; // Position de la fourmi
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

    public Fourmi(double x, double y) {
        position = new Vecteur(x,y);
        vitesse = 2.0;
        porteNourriture = false;
        direction = new Vecteur(Math.random(),Math.random());
        direction.unitaire();
        errance = direction;
        attractionNourriture = new Vecteur();
    }

    public Vecteur getPosition() {
        return position;
    }

    public void avancer(ArrayList<Nourriture> n) {
        calculErrance();
        calculNouvelleDirection(n);
        position.x += vitesse*direction.x;
        position.y += vitesse*direction.y;
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    public void calculNouvelleDirection(ArrayList<Nourriture> n) {
        
        // Cas où la fourmi ne porte pas de nourriture : elle est donc a la recherche de nourriture
        if (!porteNourriture) {
            if (nourritureEnVue(n)) {           
                direction = direction.somme(calculAttractionNourriture(), 1, COEFF_ATTRACTION_NOURRITURE);
                direction.unitaire();
            } else if (pheroRetourEnVue()) {
                //
            } else {                             
                direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
                direction.unitaire();
            }
        }

        // La fourmi porte de la nourriture et recherche alors la fourmilière, ici elle le voit et va donc se diriger vers ce dernier
        else if (fourmilièreEnVue()) { 
            //
        } 
        
        // La fourmi ne voit pas la fourmiliere mais a vu des pheromone aller et va donc les suivre
        else if (pheroAllerEnVue()){     
            //         
        }
        
        // La fourmi ne voit ni fourmiliere ni pheromones aller et va donc errer
        else {                                    
            direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
            direction.unitaire();
        }
    }

    // Fait varier le vecteur errance
    public Vecteur calculErrance() {
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
        return errance;
    }

    // Calcul de l'attraction d'une fourmi à une nourriture dans son champ de vision
    public Vecteur calculAttractionNourriture() {
        Vecteur rep = new Vecteur();
        for (Nourriture n : nourritureProche) {
            rep = rep.somme(n.position.soustrait(new Vecteur(position.x, position.y)));
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

    // Indique s'il y a la foumiliere en vue
    private boolean fourmilièreEnVue() {         // A ECRIRE // en parametre : position de la fourmiliere
        return false;
    }

    // Indique s'il y a de la nourriture en vue
    public boolean nourritureEnVue(ArrayList<Nourriture> nourritures) {
        boolean rep = false;
        for (Nourriture n : nourritures) {
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
        g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*direction.x),(int)(position.y+50*direction.y));
        g.setColor(couleur);
        g.fillOval((int)(position.x-r), (int)(position.y-r), (int)(2*r), (int)(2*r));
    }

    // Retourne la distance à un point défini par son vecteur position 'point'
    public double distanceA(Vecteur point) {
        return (Math.sqrt((position.x-point.x)*(position.x-point.x)+(position.y-point.y)*(position.y-point.y)));
    }
}
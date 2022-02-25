import java.awt.*;
import java.util.ArrayList;

public class FourmiA extends Fourmi {
    
    public FourmiA(double x, double y) {
        super(x,y);
        couleur = Color.RED;
    }

    public FourmiA(Vecteur pos) {
        this(pos.x,pos.y);
    }

    public FourmiA(double x, double y, Vecteur dir) {
        super(x,y,dir);
        couleur = Color.RED;
    }

    protected void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, ArrayList<Pheromone> pheromones) {
        if (nourritureEnVue(nourritures)) {           
            direction = direction.somme(calculAttractionNourriture(nourritures), 1, COEFF_ATTRACTION_FOURMILIERE);
            direction.unitaire();
        } else if (pheromonesEnVue(pheromones)) {
            direction = direction.somme(calculAttractionPheromones(pheromones), 1, COEFF_ATTRACTION_PHEROMONES);
            direction.unitaire();
        } else {                             
            direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapproche du vecteur errance
            direction.unitaire();
        }
    }

    // Calcul de l'attraction d'une fourmiA à une nourriture dans son champ de vision
    private Vecteur calculAttractionNourriture(ArrayList<Nourriture> nourritures) {
        Vecteur rep = new Vecteur();
        for (Nourriture n : nourritures) {
            if (this.distanceA(n.getPosition()) < PORTEE_VUE) {
                rep = rep.somme(n.getPosition().soustrait(getPosition()));
            }
        }
        rep.unitaire();
        return rep;
    }

    // Indique s'il y a de la nourriture en vue
    private boolean nourritureEnVue(ArrayList<Nourriture> nourritures) {
        boolean rep = false;
        for (Nourriture n : nourritures) {
            if (this.distanceA(n.getPosition()) < PORTEE_VUE) {
                rep = true;
            }
        }
        return rep;
    }

}

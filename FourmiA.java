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

    public void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere) {
        if (nourritureEnVue(nourritures)) {           
                direction = direction.somme(calculAttractionNourriture(nourritures, fourmiliere), 1, COEFF_ATTRACTION_FOURMILIERE);
                direction.unitaire();
        } else if (pheromonesEnVue()) {
                //
        } else {                             
                direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
                direction.unitaire();
        }
    }

    // Calcul de l'attraction d'une fourmiA à une nourriture dans son champ de vision
    public Vecteur calculAttractionNourriture(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere) {
        Vecteur rep = new Vecteur();
        for (Nourriture n : nourritures) {
            if (this.distanceA(n.getPosition()) < PORTEE_VUE) {
                rep = rep.somme(n.getPosition().soustrait(new Vecteur(position.x, position.y)));
            }
        }
        rep.unitaire();
        return rep;
    }

    public boolean pheromonesEnVue() {
        return false;
    }

    // Indique s'il y a de la nourriture en vue
    public boolean nourritureEnVue(ArrayList<Nourriture> nourritures) {
        boolean rep = false;
        for (Nourriture n : nourritures) {
            if (this.distanceA(n.getPosition()) < PORTEE_VUE) {
                rep = true;
            }
        }
        return rep;
    }

}

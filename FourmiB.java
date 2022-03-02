import java.awt.*;
import java.util.ArrayList;

public class FourmiB extends Fourmi {
    
    public FourmiB(double x, double y) {
        super(x,y);
        couleur = Color.ORANGE;
    }

    public FourmiB(Vecteur pos) {
        this(pos.x,pos.y);
    }

    public FourmiB(double x, double y, Vecteur dir) {
        super(x,y,dir);
        couleur = Color.ORANGE;
    }

    protected void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, ArrayList<Pheromone> pheromones) {
        if (fourmiliereEnVue(fourmiliere)) {           
                direction = direction.somme(calculAttractionFourmiliere(fourmiliere), 1, COEFF_ATTRACTION_NOURRITURE);
                direction.unitaire();
        } else if (pheromonesEnVue(pheromones)) {
                direction = direction.somme(calculAttractionPheromones(pheromones, false), 1, COEFF_ATTRACTION_PHEROMONES);
                direction.unitaire();
        } else {                             
                direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
                direction.unitaire();
        }
    }

    // Indique s'il y a la foumiliere en vue
    private boolean fourmiliereEnVue(Fourmiliere fourmiliere) {
        boolean rep = false;
        if (this.distanceA(fourmiliere.getPosition()) < PORTEE_VUE) {
            rep = true;
        }
        return rep;
    }

    // Calcul de l'attraction d'une fourmiB à la fourmiliere dans son champ de vision
    private Vecteur calculAttractionFourmiliere(Fourmiliere fourmiliere) {
        Vecteur rep = new Vecteur();
        rep = rep.somme(fourmiliere.getPosition().soustrait(new Vecteur(position.x, position.y)));
        rep.unitaire();
        return rep;
    }
}

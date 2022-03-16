import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class FourmiB extends Fourmi {
    
    public FourmiB(double x, double y) {
        super(x,y);
    }

    public FourmiB(Vecteur pos) {
        this(pos.x,pos.y);
    }

    public FourmiB(double x, double y, Vecteur dir) {
        super(x,y,dir);
    }

    protected void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheromones, ArrayList<Obstacle> obstacles) {
        ArrayList<Segment> mursProches = mursSecants(obstacles);
        if (mursProches.size()>0) {
            if (sensRotation != 0) {
                angleRotationMur(segmentLePlusProche(mursProches));
            }
            direction.tourner(sensRotation*ANGLE_ROTATION);
        } else {
            if (fourmiliereEnVue(fourmiliere)) {           
                direction = direction.somme(calculAttractionFourmiliere(fourmiliere), 1, COEFF_ATTRACTION_NOURRITURE);
                direction.unitaire();
            } else {
                if (pheromonesEnVue(pheromones)) {
                    direction = direction.somme(calculAttractionPheromones(pheromones, false), 1, COEFF_ATTRACTION_PHEROMONES);
                }
                direction = direction.somme(errance, 1, COEFF_ERRANCE);
                direction.unitaire();
            }
        }
    }

    // Indique s'il y a la foumiliere en vue
    private boolean fourmiliereEnVue(Fourmiliere fourmiliere) {
        boolean rep = false;
        if (position.distance(fourmiliere.getPosition()) < PORTEE_VUE) {
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

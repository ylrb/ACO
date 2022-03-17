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

    protected void calculNouvelleDirection(LinkedList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheromones, LinkedList<Obstacle> obstacles) {
        LinkedList<Segment> mursProches = mursSecants(obstacles);
        if (mursProches.size()>0) {
            if (sensRotation == 0) {
                angleRotationMur(segmentLePlusProche(mursProches));
            }
            direction.tourner(sensRotation*ANGLE_ROTATION);
            errance = direction;
        } else {
            Vecteur forceAttractionFourmiliere = calculAttractionFourmiliere(fourmiliere);
            if ((forceAttractionFourmiliere.x!=0)&&(forceAttractionFourmiliere.y!=0)) {           
                direction = direction.somme(forceAttractionFourmiliere, 1, COEFF_ATTRACTION_NOURRITURE);
                direction.unitaire();
            } else {
                if (pheromonesEnVue(pheromones)) {
                    direction = direction.somme(calculAttractionPheromones(pheromones, obstacles, false), 1, COEFF_ATTRACTION_PHEROMONES);
                    direction.unitaire();
                } else {
                    direction = direction.somme(errance, 1, COEFF_ERRANCE);
                    direction.unitaire();
                }
            }
        }
    }

    // Calcul de l'attraction d'une fourmiB à la fourmiliere dans son champ de vision
    private Vecteur calculAttractionFourmiliere(Fourmiliere fourmiliere) {
        Vecteur rep = new Vecteur();
        if (position.distance(fourmiliere.getPosition()) < PORTEE_VUE) {
            rep = rep.somme(fourmiliere.getPosition().soustrait(new Vecteur(position.x, position.y)));
            rep.unitaire();
        }
        return rep;
    }
}

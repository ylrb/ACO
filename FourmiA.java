import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class FourmiA extends Fourmi {
    
    public FourmiA(double x, double y) {
        super(x,y);
    }

    public FourmiA(Vecteur pos) {
        this(pos.x,pos.y);
    }

    public FourmiA(double x, double y, Vecteur dir) {
        super(x,y,dir);
    }

    protected void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheromones, ArrayList<Obstacle> obstacles) {
        ArrayList<Segment> mursProches = mursSecants(obstacles);
        if (mursProches.size()>0) {
            if (sensRotation == 0) {
                angleRotationMur(segmentLePlusProche(mursProches));
            }
            direction.tourner(sensRotation*ANGLE_ROTATION);
            errance = direction;
        } else {
            sensRotation = 0;
            if (nourritureEnVue(nourritures)) {           
                direction = direction.somme(calculAttractionNourriture(nourritures), 1, COEFF_ATTRACTION_FOURMILIERE);
                direction.unitaire();
            } else { 
                if (pheromonesEnVue(pheromones)) {
                    direction = direction.somme(calculAttractionPheromones(pheromones, false), 1, COEFF_ATTRACTION_PHEROMONES);
                    direction.unitaire();
                } else {
                    direction = direction.somme(errance, 1, COEFF_ERRANCE);
                    direction.unitaire();
                }
            }
        }
        
    }

    // Indique s'il y a de la nourriture en vue
    private boolean nourritureEnVue(ArrayList<Nourriture> nourritures) {
        boolean rep = false;
        for (Nourriture n : nourritures) {
            if (position.distance(n.getPosition()) < PORTEE_VUE) {
                rep = true;
                break;
            }
        }
        return rep;
    }

    // Calcul de l'attraction d'une fourmiA aux nourritures dans son champ de vision
    private Vecteur calculAttractionNourriture(ArrayList<Nourriture> nourritures) {
        Vecteur rep = new Vecteur();
        for (Nourriture n : nourritures) {
            if (position.distance(n.getPosition()) < PORTEE_VUE) {
                rep = rep.somme(n.getPosition().soustrait(getPosition()));
            }
        }
        rep.unitaire();
        return rep;
    }

}

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
        if (nourritureEnVue(nourritures)) {           
            direction = direction.somme(calculAttractionNourriture(nourritures), 1, COEFF_ATTRACTION_FOURMILIERE);
            direction.unitaire();
        } else { 
            if (mursEnVue(obstacles)) {

                // On calcule la résultante des forces de répulsion des murs
                Vecteur forceRepulsive = calculRepulsionMur(obstacles);
                direction = direction.somme(forceRepulsive, 1, COEFF_REPULSION_MURS);
                errance = direction;

                // Si une fourmi arrive perpendiculairement à un mur (angle inférieure à ANGLE_MIN_MUR), on augmente la composante parallèle au mur pour éviter qu'elle "rebondisse" sur le mur
                forceRepulsive.inverser();
                if (direction.angle(forceRepulsive) < Math.toRadians(ANGLE_MIN_MUR)) {
                    if (forceRepulsive.x == 0) {
                        direction.x = 1.2*direction.x;
                    } else if (forceRepulsive.y == 0) {
                        direction.y = 1.2*direction.y;
                    }
                }
                
            } else if (pheromonesEnVue(pheromones)) {
                direction = direction.somme(calculAttractionPheromones(pheromones, false), 1, COEFF_ATTRACTION_PHEROMONES);
            }
            direction = direction.somme(errance, 1, COEFF_ERRANCE);
            direction.unitaire();
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

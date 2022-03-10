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
        if (fourmiliereEnVue(fourmiliere)) {           
            direction = direction.somme(calculAttractionFourmiliere(fourmiliere), 1, COEFF_ATTRACTION_NOURRITURE);
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

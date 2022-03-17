import java.util.LinkedList;

public class FourmiB extends Fourmi {
    private int tempsRetour = 0;
    private static final double BAISSE_TAUX_INITIAL = 0.1;

    public FourmiB(double x, double y) {
        super(x,y);
    }

    public FourmiB(Vecteur pos) {
        this(pos.x,pos.y);
    }

    public FourmiB(Vecteur pos, Vecteur dir) {
        super(pos.x,pos.y,dir);
    }

    public PheroRetour deposerPheromoneRetour() {
        double tauxInitial = 100.0;
        if (100 - tempsRetour/10 > 5) {
            tauxInitial -= BAISSE_TAUX_INITIAL*tempsRetour;
        } else {
            return null;
        }
        return new PheroRetour(getPosition(), tauxInitial);
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
        tempsRetour++;
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

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

    // Pour les fourmiA, la force spéciale est la force d'attraction à la fourmilière
    protected Vecteur calculForceSpeciale(Fourmiliere fourmiliere, LinkedList<Nourriture> nourritures) {
        return calculAttractionFourmiliere(fourmiliere);
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
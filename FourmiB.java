import java.util.LinkedList;

public class FourmiB extends Fourmi {

    public FourmiB(double x, double y) {
        super(x,y);
    }

    public FourmiB(Vecteur pos) {
        this(pos.x,pos.y);
    }

    public FourmiB(Vecteur pos, Vecteur dir) {
        super(pos.x,pos.y,dir);
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
import java.util.LinkedList;

public class FourmiA extends Fourmi {

    public FourmiA(double x, double y) {
        super(x, y);
    }

    public FourmiA(Vecteur pos) {
        this(pos.x, pos.y);
    }

    public FourmiA(Vecteur pos, Vecteur dir) {
        super(pos.x, pos.y, dir);
    }

    // Pour les fourmiA, la force spéciale est la force d'attraction à la nourriture
    protected Vecteur calculForceSpeciale(Fourmiliere fourmiliere, LinkedList<Nourriture> nourritures) {
        return calculAttractionNourriture(nourritures);
    }

    // Calcul de l'attraction d'une fourmiA aux nourritures dans son champ de vision
    private Vecteur calculAttractionNourriture(LinkedList<Nourriture> nourritures) {
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

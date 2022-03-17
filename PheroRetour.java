import java.awt.*;

public class PheroRetour extends Pheromone {
    
    public PheroRetour(Vecteur pos) {
        position = pos;
        couleur = Color.YELLOW;
    }

    public PheroRetour(Vecteur pos, double tauxInitial) {
        this(pos);
        taux = tauxInitial;
    }

}

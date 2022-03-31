import java.awt.*;

public class PheroRetour extends Pheromone {
    
    public PheroRetour(Vecteur pos) {
        position = pos;
        couleur = Color.YELLOW;
    }

    public PheroRetour(Vecteur pos, int compteur) {
        position = pos;
        taux = 100 - compteur;
        couleur = Color.YELLOW;
    }

}

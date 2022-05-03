import java.awt.*;
import java.awt.image.*;

public class Pheromone extends Element {
    private final boolean type; // Si la phéromone est une phéromone aller (type = 0) ou retour (type = 1)
    private Color couleur;
    private double taux = 100.0;
    private static final double REDUCTION = 0.08; // Vitesse de disparition des phéromone
    private static final double AFFAIBLISSEMENT = 3.5; // Vitesse de réduction du taux initial

    public Pheromone(Vecteur pos, int compteur, boolean t) {
        position = pos;
        type = t;
        if (type) {
            couleur = Color.RED;
        } else {
            couleur = Color.BLUE;
        }
        taux = 100 - compteur * AFFAIBLISSEMENT;
    }

    public double getTaux() {
        return taux;
    }

    // Les phéromones s'estompent à chaque itération
    public void estompe() {
        taux -= REDUCTION;
    }

    public void dessine(Graphics2D g, BufferedImage b) {
        couleur = new Color(couleur.getRed(), couleur.getGreen(), couleur.getBlue(), (int) (2.5 * taux)); // On rend plus transparentes les phéromones selon leur taux
        g.setColor(couleur);
        g.drawLine((int) position.x, (int) position.y, (int) position.x + 1, (int) position.y);
    }
}

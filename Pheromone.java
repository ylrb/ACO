import java.awt.*;

public class Pheromone {
    protected Vecteur position;
    protected double taux = 100.0;
    protected Color couleur;

    public void dessine(Graphics g) {
        int r = 5;
        couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue(),(int)(2.5*taux)); // On rend plus transparentes les phéromones selon leur teux
        g.setColor(couleur);
        g.fillOval((int)(position.x-r), (int)(position.y-r), 2*r, 2*r);
    }

    public double getTaux() {
        return taux;
    }

    // Les phéromones s'estompent à chaque itération
    public void estompe() {
        taux -= 0.5;
    }
}

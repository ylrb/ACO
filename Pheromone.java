import java.awt.*;

public class Pheromone {
    protected double x;
    protected double y;
    protected double taux = 100.0;
    protected Color couleur;

    public void dessine(Graphics g) {
        int r = 5;
        couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue(),(int)(2.5*taux));
        g.setColor(couleur);
        g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
    }

    public void estompe() {
        taux -= 0.5;
    }

    public double getTaux() {
        return taux;
    }
}

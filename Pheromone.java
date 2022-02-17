import java.awt.*;

public class Pheromone {
    protected double x;
    protected double y;
    protected double taux = 100.0;
    protected Color couleur;

    protected static final double reductionTaux = 0.2;
    protected static final int rayonFourmi = 5;

    public void dessine(Graphics g) {
        couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue(),(int)(2.5*taux));
        g.setColor(couleur);
        g.fillOval((int)(x-rayonFourmi), (int)(y-rayonFourmi), 2*rayonFourmi, 2*rayonFourmi);
    }

    public void estompe() {
        taux -= reductionTaux;
    }

    public double getTaux() {
        return taux;
    }
}

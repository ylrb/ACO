import java.awt.*;

public class Fourmiliere extends Element {
    protected Color couleur;
    private int nourriture;
    private static double rayon = 10; // Rayon du disque dessiné lors de la méthode dessine()

    public Fourmiliere(double x, double y) {
        position = new Vecteur(x,y);
        couleur = new Color(139,69,19);
    }

    public double getRayon() {
        return rayon;
    }
    
    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.fillOval((int) (position.x-rayon), (int) (position.y-rayon), (int)(2*rayon), (int)(2*rayon));
    }

    public void depot() {
        nourriture += 1;
        rayon += 0.05;
    }

}

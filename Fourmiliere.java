import java.awt.*;

public class Fourmiliere extends Element {
    private static int RAYON = 15; // Rayon du disque dessiné lors de la méthode dessine()

    public Fourmiliere(double x, double y) {
        position = new Vecteur(x,y);
        couleur = Color.BLACK;
    }
    
    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.fillOval((int) position.x-RAYON, (int) position.y-RAYON, 2*RAYON, 2*RAYON);
    }

}

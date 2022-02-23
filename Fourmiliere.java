import java.awt.*;

public class Fourmiliere {
    private Vecteur position;
    private Color couleur;

    private static int RAYON = 15; // Rayon du disque dessiné lors de la méthode dessine()

    public Fourmiliere(double x, double y) {
        position = new Vecteur(x,y);
        couleur = Color.BLACK;
    }

    public Vecteur getPosition() {
        return position;
    }
    
    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.fillOval((int) position.x-RAYON, (int) position.y-RAYON, 2*RAYON, 2*RAYON);
    }

}

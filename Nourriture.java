import java.awt.*;
import java.awt.image.BufferedImage;

public class Nourriture extends Element {

    private int rayon;

    public Nourriture(Vecteur pos, int taille) {
        position = pos;
        rayon = taille / 2;
    }

    public Nourriture(double x, double y, int taille) {
        this(new Vecteur(x, y), taille);
    }

    public int getRayon() {
        return rayon;
    }

    public void dessine(Graphics2D g, BufferedImage imageNourriture) {
        g.drawImage(imageNourriture, (int) (position.x - rayon), (int) (position.y - rayon), null);
    }
}

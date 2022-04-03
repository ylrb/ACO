import java.awt.*;
import java.awt.image.BufferedImage;

public class Nourriture {

    private Vecteur position;
    private int rayon;

    public Nourriture(Vecteur pos, int taille) {
        position = pos;
        rayon = taille / 2;
    }

    public Nourriture(double x, double y, int taille) {
        this(new Vecteur(x, y), taille);
    }

    public void setPosition(Vecteur nouvellePosition) {
        position = nouvellePosition;
    }

    public Vecteur getPosition() {
        return new Vecteur(position.x, position.y);
    }

    public int getRayon() {
        return rayon;
    }

    public void dessine(Graphics2D g, BufferedImage imageNourriture) {
        g.drawImage(imageNourriture, (int) (position.x - rayon), (int) (position.y - rayon), null);
    }
}

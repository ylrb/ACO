import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Element {
    protected Vecteur position;

    public Vecteur getPosition() {
        return new Vecteur(position.x, position.y);
    }

    public void setPosition(Vecteur nouvellePosition) {
        position = nouvellePosition;
    }

    public abstract void dessine(Graphics2D g, BufferedImage image);
}

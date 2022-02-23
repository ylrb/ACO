import java.awt.*;

public abstract class Element {
    protected Vecteur position;
    protected Color couleur;

    public Vecteur getPosition() { // On ne peut pas simplement return un vecteur car sinon les deux vecteurs sont liés : il faut en créer un nouveau
        double x = position.x;
        double y = position.y;
        return new Vecteur(x,y);
    }

    public abstract void dessine(Graphics g);
}

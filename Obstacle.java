import java.awt.*;
import java.awt.image.*;
import java.util.LinkedList;
import java.awt.geom.*;

public class Obstacle extends Element {
    private boolean vide;
    private LinkedList<Segment> murs = new LinkedList<Segment>();

    public Obstacle(LinkedList<Vecteur> points) {
        for (int i = 0; i < points.size(); i++) {
            murs.add(new Segment(points.get(i), points.get((i + 1) % points.size())));
        }
    }

    public LinkedList<Segment> getMurs() {
        return murs;
    }

    public void setVide(boolean v) {
        vide = v;
    }

    public boolean estVide(){
        return vide;
    }

    public void dessine(Graphics2D g, BufferedImage b) {
        int[] X = new int[murs.size()];
        int[] Y = new int[murs.size()];
        int i = 0;
        for (Segment m : murs) {
            X[i] = (int) m.pointA.x;
            Y[i] = (int) m.pointA.y;
            i++;
        }
        Area masque = new Area(new Polygon(X, Y, X.length));
        if (vide) { // Si l'obstacle est vide, on inverse le masque (la texture se dessine a l'extérieur du masque)
            Rectangle2D cadreCarte = new Rectangle2D.Double(-20,-20,1100, 800);
            Area cadreArea = new Area(cadreCarte);
            Area masqueArea = new Area(masque);
            masque.exclusiveOr(cadreArea); // on soustrait au cadre l'ancien masque => l'ancien masque est inversé
        }
        MainWindow.carte.masqueTotal.add(masque);
    }
}

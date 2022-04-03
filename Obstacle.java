import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Obstacle {
    private Color couleur;
    private ArrayList<Segment> murs = new ArrayList<Segment>();

    public Obstacle (LinkedList<Vecteur> points) {
        for (int i = 0; i < points.size(); i++) {
            murs.add(new Segment(points.get(i),points.get((i+1)%points.size())));
        }
        couleur = new Color(140,70,20);
    }
    
    public ArrayList<Segment> getMurs() {
        return murs;
    }

    public void dessine(Graphics2D g) {
        int[] X = new int[murs.size()];
        int[] Y = new int[murs.size()];
        int i = 0;
        for (Segment m : murs) {
            X[i] = (int)m.pointA.x;
            Y[i] = (int)m.pointA.y;
            i++;
        }
        g.setColor(couleur);
        g.fillPolygon(X, Y, X.length);
    }
}

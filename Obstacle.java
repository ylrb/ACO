import java.awt.*;
import java.util.ArrayList;

public class Obstacle {
    private Color couleur;
    private ArrayList<Segment> murs = new ArrayList<Segment>();
    boolean exterieur;

    public Obstacle (Vecteur[] points, boolean ext) {
        for (int i = 0; i < points.length; i++) {
            murs.add(new Segment(points[i],points[(i+1)%points.length]));
        }
        couleur = new Color(140,70,20);
        exterieur = ext;
    }
    
    public ArrayList<Segment> getMurs() {
        return murs;
    }

    public void dessine(Graphics2D g) {
        g.setColor(couleur);
        int[] X = new int[murs.size()];
        int[] Y = new int[murs.size()];
        int i = 0;
        for (Segment m : murs) {
            X[i] = (int)m.pointA.x;
            Y[i] = (int)m.pointA.y;
            i++;
        }
        if (exterieur) {
            g.drawPolygon(X, Y, X.length);
        } else {
            g.fillPolygon(X, Y, X.length);
        }
        
    }

}

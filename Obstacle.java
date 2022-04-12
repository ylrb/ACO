import java.awt.*;
import java.awt.image.*;
import java.util.LinkedList;

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

    public void dessine(Graphics2D g, BufferedImage b) {
        int[] X = new int[murs.size()];
        int[] Y = new int[murs.size()];
        int i = 0;
        for (Segment m : murs) {
            X[i] = (int) m.pointA.x;
            Y[i] = (int) m.pointA.y;
            i++;
        }
        g.setColor(new Color(100,97,92));
        if (!vide) {
            g.fillPolygon(X, Y, X.length);
        } else {
            g.drawPolygon(X, Y, X.length);
        }
    }
}

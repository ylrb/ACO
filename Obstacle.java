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
        Shape masque = new Polygon(X, Y, X.length);
        if (vide) { // Si l'obstacle est vide, on inverse le masque (la texture se dessine a l'extérieur du masque)
            Rectangle2D cadreCarte = new Rectangle2D.Double(-20,-20,1100, 800);
            Area cadreArea = new Area(cadreCarte);
            Area masqueArea = new Area(masque);
            cadreArea.subtract(masqueArea); // on soustrait au cadre l'ancien masque => l'ancien masque est inversé
            masque = cadreArea; 
        }
        g.setClip(masque); // on définit un masque au graphics2D
        g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // on définit des contours
        g.drawImage(MainWindow.imageTexture, 0, 0, null); // on dessine l'image sur le masque
        g.setPaint(new Color(0,0,0,80));
        g.draw(masque); // on dessine les contours
        g.setStroke(new BasicStroke());
    }
}

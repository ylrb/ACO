import java.awt.*;

public class Obstacle {
    private Color couleur;
    private Mur[] murs;

    public Obstacle (Vecteur[] coins) {
        murs = new Mur[4];
        for (int i = 0; i<4; i++) {
            murs[i] = new Mur(i, coins[i], coins[(i+1)%4]);
        }
        couleur = Color.YELLOW;
    }

    public Obstacle(Mur[] murs) {
        murs = new Mur[4];
        couleur = Color.YELLOW;
    }
    
    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.drawRect((int)murs[0].point1.x,(int)murs[0].point1.y,(int)(murs[2].point2.x-murs[0].point1.x),(int)(murs[2].point2.y-murs[0].point1.y));
    }
}

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

    // Cette version du constructeur sert à initialiser les contours de la carte uniquement
    public Obstacle() {
        Vecteur[] coins = {new Vecteur(10,10), new Vecteur(1010,10), new Vecteur(1010,688), new Vecteur(10,688)};
        murs = new Mur[4];
        for (int i = 0; i<4; i++) {
            murs[i] = new Mur((i+2)%4, coins[i], coins[(i+1)%4]);
        }
        couleur = Color.YELLOW;
    }

    public Mur[] getMur() {
        return murs;
    }
    
    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.drawRect((int)murs[0].point1.x,(int)murs[0].point1.y,(int)(murs[2].point2.x-murs[0].point1.x),(int)(murs[2].point2.y-murs[0].point1.y));
    }

    
}

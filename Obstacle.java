import java.awt.*;

public class Obstacle {
    private Color couleur;
    private Mur[] murs;

    // public Obstacle (Vecteur[] coins) {
        
    //     murs = new Mur[4];
    //     for (int i = 0; i<4; i++) {
    //         Mur[i] = new Mur(i, coins[i], coins[(i+1)%4]);
    //     }
    //     couleur = Color.YELLOW;
    // }


    public Obstacle() {
        murs = new Mur[4];
        couleur = Color.YELLOW;
    }
    
    public void dessine(Graphics g) {
        //
    }
}

import java.awt.*;

public class Obstacle extends Element {
    private Color couleur;
    private double[] coins;

    public Obstacle(double[] points) {
        coins = points;
        couleur = Color.YELLOW;
    }

    public void dessine(Graphics g) {
        //
    }
}

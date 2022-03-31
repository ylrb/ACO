import java.awt.*;

public class Pheromone {
    protected Vecteur position;
    protected Color couleur;
    protected double taux = 100.0;
    protected static final double REDUCTION = 0.2; // Vitesse de disparition des phéromones
    
    public Vecteur getPosition() {
        return new Vecteur(position.x,position.y);
    }  

    public double getTaux() {
        return taux;
    }


    /*
    public Pheromone (double coeffiecient) {

    }
    */

    // Les phéromones s'estompent à chaque itération
    public void estompe() {
        taux -= REDUCTION;
    }

    public void dessine(Graphics2D g) {
        int alpha;
        if (taux >= 100) {
            alpha = 255;
        } else {
            alpha = (int)(2.5*taux);
        }
        couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue(),alpha); // On rend plus transparentes les phéromones selon leur teux
        g.setColor(couleur);
        g.drawLine((int)position.x,(int)position.y,(int)position.x+1,(int)position.y);
    }

    public void rafraichir() {
        taux += 100;
        if(taux >= 300) {
            taux = 300;
        }
    }

    public void rafraichir(int t) {
        if (t <= 100) {
            taux += (100-t);
        }
        if(taux >= 100) {
            taux = 100;
        }
    }
}

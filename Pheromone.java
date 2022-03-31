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

    // Les phéromones s'estompent à chaque itération
    public void estompe() {
        taux -= REDUCTION;
    }

    public void dessine(Graphics2D g) {
        couleur = new Color(couleur.getRed(),couleur.getGreen(),couleur.getBlue(),(int)(2.5*taux)); // On rend plus transparentes les phéromones selon leur teux
        g.setColor(couleur);
        g.drawLine((int)position.x,(int)position.y,(int)position.x+1,(int)position.y);
    }
}

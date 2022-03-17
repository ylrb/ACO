import java.awt.*;
import java.awt.image.BufferedImage;

public class Fourmiliere {
    
    private Vecteur position;
    private int nourriture;
    private int rayon;

    public Fourmiliere(double x, double y, int taille) {
        position = new Vecteur(x,y);
        rayon = taille/2;
    }

    public Vecteur getPosition() {
        return new Vecteur(position.x,position.y);
    }  

    public void setPosition(Vecteur newPos){
        position = newPos;
    }

    public double getRayon() {
        return rayon;
    }
    
    public void dessine(Graphics2D g, BufferedImage imageFourmiliere) {

        g.drawImage(imageFourmiliere, (int)(position.x-rayon), (int)(position.y-rayon), null);
    }

    public void depot() {
        nourriture += 1;
    }

}

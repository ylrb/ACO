import java.awt.*;
import java.awt.image.BufferedImage;

public class Fourmiliere {

    private Vecteur position;
    private int nourriture;
    private int rayon;

    public Fourmiliere(double x, double y, int taille) {
        position = new Vecteur(x, y);
        rayon = taille / 2;
    }

    public Vecteur getPosition() {
        return new Vecteur(position.x, position.y);
    }

    public void setPosition(Vecteur newPos) {
        position = newPos;
    }

    public double getRayon() {
        return rayon;
    }
    
    public void depot() {
        nourriture += 1;
    }

    public void dessine(Graphics2D g, BufferedImage imageFourmiliere) {
        g.drawImage(imageFourmiliere, (int) (position.x - rayon), (int) (position.y - rayon), null);
        g.setColor(Color.BLACK);

        // On récupère le nombre de chiffres dans 'nourriture' grâce au logarithme, et on définit la longueur du texte en fonction de cette valeur.
        int longueur;
        if (nourriture > 0) {
            longueur = 4 * ((int) Math.log10(nourriture) + 1);
        } else {
            longueur = 4;
        }

        String nombre = Integer.toString(nourriture);
        g.drawString(nombre, (int) position.x - longueur, (int) position.y - 30);
    }

    public void resetNourriture () {nourriture = 0; }      // Remet à 0 le compteur de nourriture.

}

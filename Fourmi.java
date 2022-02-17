import java.awt.*;

public class Fourmi {

    private double x;
    private double y;
    private double vitesse;
    private boolean porteNourriture; //si la fourmi porte de la nourriture
    private Color couleur = Color.RED;

    ////////////////////////vecteurs
    private Vecteur direction;
    private Vecteur errance;

    ////////////////////////tous les coefficients appliqués au vecteur
    private static final double coeffErrance = 0.3; //poids du vecteur errance
    private static final double amplitudeErrance = 20; //amplitude max de la variation du vecteur errance

    public Fourmi(double X, double Y) {
        x = X;
        y = Y;
        vitesse = 2.0;
        porteNourriture = false;
        direction = new Vecteur(Math.random(),Math.random());
        direction.unitaire();
        errance = direction;
    }

    public double getx() {
        return x;
    }

    public double gety() {
        return y;
    }

    public void avancer() {
        calculErrance();
        calculNouvelleVitesse();
        x += vitesse*direction.x;
        y += vitesse*direction.y;
    }

    public void calculNouvelleVitesse() {
        if (!pheroRetourEnVue() && !nourritureEnVue()) { // la fourmi est en mode recherche
            direction = direction.somme(errance, 1, coeffErrance);
            direction.unitaire();
        }
    }

    public Vecteur calculErrance() {
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*amplitudeErrance); //amplitude en degré convertie en radians
        //errance.unitaire();       //normalement y en a pas bsn
        return errance;
    }


    public boolean pheroRetourEnVue() { //retourne si il y a des phero en vue
        return false;

    }

    public boolean nourritureEnVue() {  //retourne si il y a des nourritures en vue
        return false;
    }

    public void dessine (Graphics g) {
        int r = 7;
        g.setColor(couleur);
        g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
    }

}
import java.awt.*;

public class Fourmi {

    private double x;
    private double y;
    private double vitesse;
    private boolean porteNourriture; // Si la fourmi porte de la nourriture
    private Color couleur = Color.RED;

    //////////////////////// Vecteurs
    private Vecteur direction;
    private Vecteur errance;

    //////////////////////// Tous les coefficients appliqués au vecteur
    private static final double COEFF_ERRANCE = 0.3; // Poids du vecteur errance
    private static final double AMPLITUDE_ERRANCE = 20; // Amplitude max de la variation du vecteur errance

    public Fourmi(double X, double Y) {
        x = X;
        y = Y;  
        vitesse = 2.0;
        porteNourriture = false;
        direction = new Vecteur(Math.random(),Math.random()); // La direction initiale de la fourmi est aléatoire
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
        calculNouvelleDirection();
        x += vitesse*direction.x;
        y += vitesse*direction.y;
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    public void calculNouvelleDirection() {
        if (!pheroRetourEnVue() && !nourritureEnVue()) { // La fourmi est en mode recherche (elle ne voit pas de nourriture ou de mur)
            direction = direction.somme(errance, 1, COEFF_ERRANCE); // Le vecteur directeur se rapporche du vecteur errance
            direction.unitaire();
        }
    }

    // Fais varier le vecteur errance
    public Vecteur calculErrance() {
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
        return errance;
    }

    // Indique s'il y a des phéromones en vue
    public boolean pheroRetourEnVue() { 
        return false;

    }

    // Indique s'il y a de la nourriture en vue
    public boolean nourritureEnVue() {  
        return false;
    }

    public void dessine (Graphics g) {
        int r = 7;
        g.setColor(couleur);
        g.fillOval((int)(x-r), (int)(y-r), 2*r, 2*r);
    }

}
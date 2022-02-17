public class Fourmi {

    private double x;
    private double y;
    private double vitesse; //vitesse de la fourmi
    private boolean porteNourriture; //si la fourmi porte de la nourriture
    //////////////////////// tout les vecteurs
    private Vecteur direction;              // vecteur vitesse normalisé
    //private Vecteur nouvelleVitesse;      //essayer de ne pas en avoir bsn
    private Vecteur errance;
    //////////////////////// tout les coefficients appliqués au vecteur
    private final double coefErrance = 0.3;


    public Fourmi(double X, double Y) {
        x = X;
        y = Y;
        vitesse = 10.0;
        porteNourriture = false;
        direction = new Vecteur(Math.random(),Math.random());
        direction.unitaire();
        errance = direction;
    }
    //test

    public void avancer() {
        x += vitesse*direction.x;
        y += vitesse*direction.y;
    }

    public void calculNouvelleVitesse() {
        if (!pheroRetourEnVue() && !nourritureEnVue()) { // la fourmi est en mode recherche
            direction = direction.somme(errance, 1, coefErrance);
            direction.unitaire();
        }
    }

    public Vecteur calculErrance() {
        double amplitude = 30; //angle maximal de rotation
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*amplitude); //amplitude en degré convertie en radians
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
        int r = 5;
        g.fillOval((int)(x-r), (int)(y-r), 2*5, 2*5)
    }

}
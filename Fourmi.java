public class Fourmi {

    private double x;
    private double y;
    private boolean porteNourriture;
    //////////////////////// tout les vecteurs
    private Vecteur direction;              // vecteur vitesse normalisé
    //private Vecteur nouvelleVitesse;      //essayer de ne pas en avoir bsn
    private Vecteur errance;
    //////////////////////// tout les coefficients appliqués au vecteur
    private double coefErrance = 0.3;


    public Fourmi(double X, double Y) {
        this.x = X;
        this.y = Y;
        porteNourriture = false;
    }

    public void calculNouvelleVitesse() {
        if (!pheroRetourEnVue && !nourritureEnVue) { // la fourmi est en mode recherche
            vitesse = vitesse.somme(errance, 1, coefErrance);
            vitesse.unitaire();
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
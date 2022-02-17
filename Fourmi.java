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
        }
    }

    public Vecteur calculErrance() {
        errance = errance.tourner((Math.random()-0.5)*Math.PI*30/90); // le 30 represente l'amplitude en degré
        //errance.unitaire();       //normalement y en a pas bsn
        return errance;
    }


    public boolean pheroRetourEnVue() { //
        return false;

    }

}
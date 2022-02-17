public class Vecteur {
    public double x;
    public double y;

    public Vecteur(double X, double Y) {
        x = X;
        y = Y;
    }

    //redéfinit les coordonnées d'un vecteur
    public void set(double X, double Y) {
        double norme = Math.sqrt(Math.pow(X,2)+Math.pow(Y,2));
        x = X/norme;
        y = Y/norme;
    }

    //somme des vecteurs v1 et v2 coefficientés
    public Vecteur somme(Vecteur v2, double coeff1, double coeff2) {
        double X = coeff1*x + coeff2*v2.x;
        double Y = coeff1*y + coeff2*v2.y;
        return new Vecteur(X,Y);
    }

    //rend le vecteur unitaire
    public void unitaire() {
        double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        x = x/norme;
        y = y/norme;
    }

    //tourne le vecteur d'un angle
    public void tourner(double angle) {
        double X = x*Math.cos(angle) - y*Math.sin(angle);
        double Y = x*Math.sin(angle) + y*Math.cos(angle);
        x = X;
        y = Y;
    }

}
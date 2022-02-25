public class Vecteur {
    public double x;
    public double y;

    public Vecteur(double X, double Y) {
        x = X;
        y = Y;
    }

    public Vecteur(Vecteur vecteur) { // Crée un vecteur à partir d'un autre vecteur, mais de manière à ce qu'ils ne soient pas liés
        this(vecteur.x,vecteur.y);
    }

    public Vecteur() {
        this(0,0);
    }

    // Redéfinit les coordonnées d'un vecteur
    public void set(double X, double Y) {
        double norme = Math.sqrt(Math.pow(X,2)+Math.pow(Y,2));
        x = X/norme;
        y = Y/norme;
    }

    // Somme des vecteurs v1 et v2 coefficientés
    public Vecteur somme(Vecteur v2, double coeff1, double coeff2) {
        double X = coeff1*x + coeff2*v2.x;
        double Y = coeff1*y + coeff2*v2.y;
        return new Vecteur(X,Y);
    }
    public Vecteur somme(Vecteur v2) {
        return somme(v2,1.0,1.0);
    }
    public Vecteur soustrait(Vecteur v2) {
        return somme(new Vecteur(-v2.x,-v2.y));
    }

    // Rend le vecteur unitaire
    public void unitaire() {
        double norme = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        x = x/norme;
        y = y/norme;
    }

    // Tourne le vecteur d'un angle
    public void tourner(double angle) {
        double X = x*Math.cos(angle) - y*Math.sin(angle);
        double Y = x*Math.sin(angle) + y*Math.cos(angle);
        x = X;
        y = Y;
    }

    private double scalaire(Vecteur v2) {
        return x*v2.x + y*v2.y;
    }
    
    // Renvoie l'angle entre deux vecteurs (en radians)
    public double angle(Vecteur v2) {
        double V1 = Math.sqrt(Math.pow(this.x,2)+Math.pow(this.y,2)); // Norme de v1
        double V2 = Math.sqrt(Math.pow(v2.x,2)+Math.pow(v2.y,2)); // Norme de v2
        return Math.acos(scalaire(v2)/(V1*V2)); // cos(theta) = u.v/(||u||*||v||)
    }

    public void inverser() {
        x = -x;
        y = -y;
    }

    public void inverserVertical() {
        x = -x;
    }

    public void inverserHorizontal() {
        y = -y;
    }

    public String toString() {
        return "Vecteur{" + "x=" + x + ", y=" + y + '}';
    }
}
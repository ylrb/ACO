public class Vecteur {
    public double x;
    public double y;

    public Vecteur(double X, double Y) {
        x = X;
        y = Y;
    }

    public Vecteur(Vecteur vecteur) {
        this(vecteur.x,vecteur.y);
    }

    public Vecteur() {
        this(0,0);
    }

    private static double norme(double X, double Y) {
        double rep = Math.sqrt(Math.pow(X,2) + Math.pow(Y,2));
        return rep;
    }

    public double norme() {
        return norme(x,y);
    }    

    // Rend le vecteur unitaire
    public void unitaire() {
        double norme = norme(x,y);
        x = x/norme;
        y = y/norme;
    }

    // Retourne la distance à un autre vecteur (pour les vecteurs positions)
    public double distance(Vecteur p2) {
        return norme(x-p2.x,y-p2.y);
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

    // Renvoie l'angle entre deux vecteurs (en radians) (en valeur absolue)
    public double angle(Vecteur v2) {
        double normeV1 = norme(x,y);
        double normeV2 = norme(v2.x,v2.y);
        return Math.acos(scalaire(v2)/(normeV1*normeV2)); // NB : Arccos > 0
    }

    // Tourne le vecteur d'un angle
    public void tourner(double angle) {
        double X = x*Math.cos(angle) - y*Math.sin(angle);
        double Y = x*Math.sin(angle) + y*Math.cos(angle);
        x = X;
        y = Y;
    }

    // Produit scalaire avec un autre vecteur
    public double scalaire(Vecteur v2) {
        return x*v2.x + y*v2.y;
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

    public void perpendiculaire() {
        double X = x;
        x = y;
        y = X;
    }
}
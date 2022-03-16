public class Segment {
    Vecteur pointA;
    Vecteur pointB;

    public Segment(Vecteur v1, Vecteur v2) {
        pointA = v1;
        pointB = v2;
    }

    public boolean colineaire(Segment s2) {
        Vecteur v1 = new Vecteur(this.pointB.x-this.pointA.x, this.pointB.y-this.pointA.y);
        Vecteur v2 = new Vecteur(s2.pointB.x-s2.pointA.x, s2.pointB.y-s2.pointA.y);
        return(0.05 > Math.abs(v1.angle(v2))); // Angle inférieur à 2,5° environ
    }

    public boolean secante(Segment s2) {
        if (colineaire(s2)) {
            return false;
        } else { 
            // On détermine les coefficients des deux droites
            double A1 = coeffs()[0];
            double B1 = coeffs()[1];
            double A2 = s2.coeffs()[0];
            double B2 = s2.coeffs()[1];
            
            // On calcule les coordonnées du point d'intersection des 2 droites
            double X = (B2-B1)/(A1-A2);
            double Y = A1*X + B1;

            // On vérifie que le point trouvé appartient aux deux segments
            return ((appartientSegment(X,Y))&&(s2.appartientSegment(X,Y)));
        }

    }
    
    // Permet de déterminer le coefficient directeur et l'ordonnée à l'origine de la droite
    public double[] coeffs() {
        Vecteur directeur = new Vecteur(pointB.x-pointA.x, pointB.y-pointA.y);
        
        double rep[] = new double[2];
        double C = directeur.y*pointA.x - directeur.x*pointA.y;
        
        rep[0] = directeur.y/directeur.x; // A = b/a
        rep[1] = -C/directeur.x; // B = -C/a

        return rep;
    }

    // La méthode renvoie true si le point (X,Y) appartient au segment
    public boolean appartientSegment(double X, double Y) {  
        // On détermine les intervalles auxquels le point doit appartenir
        double xmin;
        double xmax;
        double ymin;
        double ymax;
        if (pointA.x < pointB.x) {
            xmin = pointA.x;
            xmax = pointB.x;
        } else {
            xmin = pointB.x;
            xmax = pointA.x;
        }
        if (pointA.y < pointB.y) {
            ymin = pointA.y;
            ymax = pointB.y;
        } else {
            ymin = pointB.y;
            ymax = pointA.y;
        }

        // On vérifie que le point d'intersection appartient au segment (au rectangle formé par le vecteur)
        return (((ymin<Y)&&(Y<ymax))&&((xmin<X)&&(X<xmax)));
    }

}  
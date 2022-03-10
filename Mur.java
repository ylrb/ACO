public class Mur {
    int orientation; // 0 à 3 pour Haut, Droite, Bas, Gauche
    Vecteur point1;
    Vecteur point2;

    private static final double LARGEUR = 20;

    public Mur(int o, Vecteur a, Vecteur b) {
        orientation = o;
        point1 = a;
        point2 = b;
        // if (orientation%2 == 0) {
        //     if (a.x < b.x) {

        //     }
        // }


    }

    public Vecteur proche(Vecteur position) {
        double dx = 0;
        double dy = 0;
        switch (orientation) {
            case 0:
                if (position.x >= point1.x-LARGEUR && position.x <= point1.x+LARGEUR && position.y >= point1.y-LARGEUR && position.y <= point1.y) {
                    dy = position.y - point1.y;
                }
                break;
            case 1:
                if (position.y >= point1.y-LARGEUR && position.y <= point2.y+LARGEUR && position.x >= point1.x && position.x <= point1.x+LARGEUR) {
                    dx = position.x - point1.x;
                }
                break;
            case 2:
                if (position.x >= point1.x-LARGEUR && position.x <= point1.x+LARGEUR && position.y <= point1.y+LARGEUR && position.y >= point1.y) {
                    dy = position.y - point1.y;
                }
                break;
            case 3:
                if (position.y >= point1.y-LARGEUR && position.y <= point2.y+LARGEUR && position.x >= point1.x-LARGEUR && position.x <= point1.x) {
                    dx = position.x-point1.x;
                }
                break;
        }
        return (new Vecteur(dx, dy));
    }

    
}
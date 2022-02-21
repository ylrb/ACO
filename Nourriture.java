import java.awt.*;

public class Nourriture {
    Vecteur position;           // vecteur position de la nourriture
    int rayon;                  // rayon du disque dessiné lors de la méthode dessine()
    Color couleur;              // couleur de la nourriture
    int quantité;               // le nombre de fois que cette source de nourriture peut encore fournir de la nourriture
    int quantitéInitiale;       // le nombre initial de fois que cette source de nourriture pouvait fournir de la nourriture

    ////////////////CONSTRUCTEUR///////////////
    // plusieurs constructeur en fonction de comment on veut declarer notre nourriture

    //de base, en ENTRANT UN VECTEUR ET UNE COULEUR
    public Nourriture(Vecteur position, int rayon, Color couleur, int quantité) {
        this.position = position;
        this.rayon = rayon;
        this.couleur = couleur;
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }
    //en ENTRANT UN VECTEUR ET PAS DE COULEUR, COULEUR "DE BASE" deja choisie (vert pétant)
    public Nourriture(Vecteur position, int rayon, int quantité) {
        this.position = position;
        this.rayon = rayon;
        this.couleur = new Color(64, 255, 0);
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }
    //en ENTRANT UN VECTEUR ET UNE COULEUR
    public Nourriture(double x, double y, int rayon, Color couleur, int quantité) {
        this.position = new Vecteur(x, y);
        this.rayon = rayon;
        this.couleur = couleur;
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }
    //en ENTRANT des COORD x et y (double) ET PAS DE COULEUR, COULEUR "DE BASE" deja choisie (vert pétant)
    public Nourriture(double x, double y, int rayon, int quantité) {
        this.position = new Vecteur(x, y);
        this.rayon = rayon;
        this.couleur = new Color(64, 255, 0);
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }

    public void servi() {           // quand une fourmi vient piocher dans cette nourriture, on appelle cette methode qui traduit cette action
        if (quantité>0) {
            quantité --;
            couleur = new Color((int)(64-(48*(quantité/quantitéInitiale))),(int)(255-(191*(quantité/quantitéInitiale))),0);     // la couleur va "se degrader" et tendre vers un vert un peu moche
        }
        if (quantité<=0) {
            couleur =  new Color(123, 138, 29);
        }
    }

    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.fillOval((int) position.x-rayon, (int) position.y-rayon, 2*rayon, 2*rayon);
    }
}

import java.awt.*;

public class Nourriture {
    Vecteur position;           // Vecteur position de la nourriture
    int rayon;                  // Rayon du disque dessiné lors de la méthode dessine()
    Color couleur;              // Couleur de la nourriture
    int quantité;               // Le nombre de fois que cette source de nourriture peut encore fournir de la nourriture
    int quantitéInitiale;       // Le nombre initial de fois que cette source de nourriture pouvait fournir de la nourriture

    //////////////// CONSTRUCTEUR ///////////////
    // Plusieurs constructeur en fonction de comment on veut déclarer notre nourriture

    // Par défaut, en entrant un vecteur et une couleur
    public Nourriture(Vecteur position, int rayon, Color couleur, int quantité) {
        this.position = position;
        this.rayon = rayon;
        this.couleur = couleur;
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }

    // En entrant un vecteur et pas de couleur, couleur "de base" deja choisie (vert pétant)
    public Nourriture(Vecteur position, int rayon, int quantité) {
        this.position = position;
        this.rayon = rayon;
        this.couleur = new Color(64, 255, 0);
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }

    // En entrant un vecteur et une couleur
    public Nourriture(double x, double y, int rayon, Color couleur, int quantité) {
        this.position = new Vecteur(x, y);
        this.rayon = rayon;
        this.couleur = couleur;
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }

    // En entrant des coordonnées x et y (double) et pas de couleur, couleur "de base" deja choisie (vert pétant)
    public Nourriture(double x, double y, int rayon, int quantité) {
        this.position = new Vecteur(x, y);
        this.rayon = rayon;
        this.couleur = new Color(64, 255, 0);
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }

    // Quand une fourmi vient piocher dans cette nourriture, on appelle cette methode qui traduit cette action
    public void servi() {           
        if (quantité > 0) {
            quantité--;
            couleur = new Color((int)(64-(48*(quantité/quantitéInitiale))),(int)(255-(191*(quantité/quantitéInitiale))),0); // La couleur va "se dégrader" et tendre vers un vert pâle
        }
        if (quantité <= 0) {
            couleur =  new Color(123, 138, 29);
        }
    }

    public void dessine(Graphics g) {
        g.setColor(couleur);
        g.fillOval((int) position.x-rayon, (int) position.y-rayon, 2*rayon, 2*rayon);
    }
}

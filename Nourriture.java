import java.awt.*;

public class Nourriture extends Element {
    protected Color couleur;
    private int quantité; // Le nombre de fois que cette source de nourriture peut encore fournir de la nourriture
    private int quantitéInitiale; // Le nombre initial de fois que cette source de nourriture pouvait fournir de la nourriture
    private static int RAYON = 10; // Rayon du disque dessiné lors de la méthode dessine()

    // En entrant un vecteur
    public Nourriture(Vecteur position, int quantité) {
        this.position = position;
        this.couleur = Color.YELLOW;
        this.quantité = quantité;
        this.quantitéInitiale = quantité;
    }

    // En entrant des coordonnées x et y (double)
    public Nourriture(double x, double y, int quantité) {
        this(new Vecteur(x,y), quantité);
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
        g.fillOval((int) position.x-RAYON, (int) position.y-RAYON, 2*RAYON, 2*RAYON);
    }
}

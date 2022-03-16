import java.awt.*;

public class Nourriture {

    protected Vecteur position;
    protected Color couleur;
    private int quantité; // Le nombre de fois que cette source de nourriture peut encore fournir de la nourriture
    private int quantitéInitiale; // Le nombre initial de fois que cette source de nourriture pouvait fournir de la nourriture
    
    private static final int RAYON = 10;

    public Nourriture(Vecteur pos, int qte) {
        position = pos;
        couleur = Color.YELLOW;
        quantité = qte;
        quantitéInitiale = quantité;
    }

    public Nourriture(double x, double y, int quantité) {
        this(new Vecteur(x,y), quantité);
    }

    public void setPosition(Vecteur newPos){
        position = newPos;
    }
    
    public Vecteur getPosition() {
        return new Vecteur(position.x,position.y);
    }  

    public int getRayon() {
        return RAYON;
    }

    // Quand une fourmi vient piocher dans cette nourriture, on appelle cette méthode qui gère les variables
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

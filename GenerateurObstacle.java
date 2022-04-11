import java.util.LinkedList;
import java.util.Random;

public class GenerateurObstacle {

    private int largeurMax;
    private LinkedList<Vecteur> points = new LinkedList<Vecteur>();
    private LinkedList<Vecteur> contour = new LinkedList<Vecteur>();
    private Random aleatoire = new Random();

    public GenerateurObstacle(int nombrePoints, int largeur) {
        largeurMax = largeur;

        // On génère un nuage de points
        nuagePoints(nombrePoints);

        // Remplissage de la liste contour (enveloppe convexe)
        Vecteur p0 = pointMin();
        contour.add(p0);
        Vecteur point = prochainPoint(p0);
        while (!(point.equals(p0))) { // Tant que le point actuel n'est pas p0 on détermine le point suivant
            contour.add(point);
            point = prochainPoint(point);
        }
    }

    // Génération aléatoire de points dans un zone de largeur definie
    private void nuagePoints(int nombre) {
        for (int i = 0; i < nombre; i++) {
            points.add(new Vecteur(largeurMax/4*aleatoire.nextGaussian(),largeurMax/4*aleatoire.nextGaussian()));
        }
    }

    // Récupère le point le plus à gauche
    private Vecteur pointMin() {
        Vecteur min = points.get(0);
        for (Vecteur p : points) {
            if (p.x < min.x) {
                min = p;
            }
        }
        return min;
    }

    // Récupère les points de l'enveloppe convexe par l'algorithme du papier cadeau
    private Vecteur prochainPoint(Vecteur p) {
        LinkedList<Vecteur> points2 = new LinkedList<Vecteur>();
        for (Vecteur point : points) {
            points2.add(point);
        }
        points2.remove(p);

        // Détermination q0
        Vecteur q0 = points2.get(0);

        // Boucle 
        for (Vecteur q : points2) {
            if (det(p,q0,q) > 0) {
                q0 = q;
            }
        }
        return q0;     
    }

    // Permet de récupérer l'obstacle créé
    public Obstacle generer(Vecteur coin) {
        for (Vecteur p : contour) {
            p.x += coin.x;
            p.y += coin.y;
        }
        return new Obstacle(contour);
    }

    // Déterminant pour 3 vecteurs
    private static double det(Vecteur p, Vecteur q, Vecteur r) {
        return det(q,r)-det(p,r)+det(p,q);
    }

    // Déterminant pour 2 vecteurs
    private static double det(Vecteur p, Vecteur q) {
        return p.x*q.y-p.y*q.x;
    }
}

import java.nio.file.*;
import java.util.List;
import java.util.LinkedList;

public class LecteurCarte {
    // Éléments du terrain que l'on souhaite initiliaser
    private LinkedList<Obstacle> obstacles;
    private Fourmiliere fourmiliere;
    private LinkedList<Nourriture> nourriture;

    // Attributs pour la lecture du fichier
    private List<String> fichier;
    private static final String DEBUT_OBSTACLES = "// DEBUT OBSTACLE //";
    private static final String FIN_OBSTACLES = "// FIN OBSTACLE //";
    private static final String SEPARATION_OBSTACLES = "// NOUVEL OBSTACLE";
    private static final String FOURMILIERE = "// FOURMILIERE //";
    private static final String NOURRITURE = "// NOURRITURE //";

    public LinkedList<Obstacle> getObstacles() {
        return obstacles;
    }

    public Fourmiliere getFourmiliere() {
        return fourmiliere;
    }

    public LinkedList<Nourriture> getNourriture() {
        return nourriture;
    }

    public LecteurCarte() {
        obstacles = new LinkedList<Obstacle>();
        fourmiliere = new Fourmiliere(0, 0, 0);
        nourriture = new LinkedList<Nourriture>();
    }

    public LecteurCarte(String chemin) {
        this();
        try {
            // Définition des variables pour la lecture
            fichier = Files.readAllLines(Paths.get(chemin));
            int ligneDebutObstacle = fichier.indexOf(DEBUT_OBSTACLES) + 1;
            int ligneFinObstacle = fichier.indexOf(FIN_OBSTACLES) - 1;
            int ligneFourmiliere = fichier.indexOf(FOURMILIERE) + 1; // Retourne 0 si il n'y a pas de fourmiliere
            int ligneNourriture = fichier.indexOf(NOURRITURE) + 1; // Idem

            // Boucle pour définir les différents obstacles de la carte
            LinkedList<Vecteur> points = new LinkedList<Vecteur>();
            for (int i = ligneDebutObstacle; i <= ligneFinObstacle; i++) {
                String ligne = fichier.get(i); // On itère sur chaque ligne du fichier texte
                if (ligne.startsWith(SEPARATION_OBSTACLES) && (i != ligneDebutObstacle)) {
                    obstacles.add(new Obstacle(points)); // On crée un nouvel obstacle qu'on ajoute dans la liste
                    points = new LinkedList<Vecteur>(); // On vide la liste des points
                } else if (ligne != "" && (i != ligneDebutObstacle)) {
                    String[] partiesLigne = ligne.split(",");
                    int x = Integer.parseInt(partiesLigne[0]);
                    int y = Integer.parseInt(partiesLigne[1]);
                    points.add(new Vecteur(x, y));
                }
            }
            obstacles.add(new Obstacle(points)); // On ajoute les derniers points dans la liste des obstacles

            // Initialisation de la fourmilière
            if (ligneFourmiliere != 0) {
                int[] coordonnéesFourmiliere = coordonnées(ligneFourmiliere);
                fourmiliere = new Fourmiliere(coordonnéesFourmiliere[0], coordonnéesFourmiliere[1], Parametres.TAILLE_FOURMILIERE);
            }

            // Initialisation de la nourriture
            if (ligneNourriture != 0) {
                int[] coordonnéesNourriture = coordonnées(ligneNourriture);
                nourriture = new LinkedList<Nourriture>();
                nourriture.add(new Nourriture(coordonnéesNourriture[0], coordonnéesNourriture[1], Parametres.TAILLE_NOURRITURE));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Méthode qui récupère les coordonnées d'une ligne, séparées par des virgules
    private int[] coordonnées(int index) {
        int[] rep = new int[3];
        String[] texte = fichier.get(index).split(",");
        for (int i = 0; i < texte.length; i++) {
            rep[i] = Integer.parseInt(texte[i]);
        }
        return rep;
    }

}

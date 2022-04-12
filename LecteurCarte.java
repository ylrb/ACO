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
        fourmiliere = new Fourmiliere(300, 300, MainWindow.TAILLE_FOURMILIERE);
        nourriture = new LinkedList<Nourriture>();
        nourriture.add(new Nourriture(400, 300, MainWindow.TAILLE_NOURRITURE));
    }

    public LecteurCarte(String chemin) {
        this();
        try {
            // Définition des variables pour la lecture
            fichier = Files.readAllLines(Paths.get(chemin));
            int ligneDebutObstacle = fichier.indexOf("// DEBUT OBSTACLE //") + 1;
            int ligneFinObstacle = fichier.indexOf("// FIN OBSTACLE //") - 1;
            int ligneFourmiliere = fichier.indexOf("// FOURMILIERE //") + 1; // Retourne 0 si il n'y a pas de fourmiliere
            int ligneNourriture = fichier.indexOf("// NOURRITURE //") + 1; // Idem

            // Boucle pour définir les différents obstacles de la carte
            LinkedList<Vecteur> points = new LinkedList<Vecteur>();
            for (int i = ligneDebutObstacle; i <= ligneFinObstacle; i++) {
                String ligne = fichier.get(i); // On itère sur chaque ligne du fichier texte
                if (ligne.startsWith("// NOUVEL OBSTACLE") && (i != ligneDebutObstacle)) {
                    Obstacle nouvelObstacle = new Obstacle(points); // On crée un nouvel obstacle qu'on ajoute dans la liste
                    if (fichier.get(fichier.indexOf(ligne)-points.size()-1).contains("vide")) {
                        nouvelObstacle.setVide(true);
                    }
                    obstacles.add(nouvelObstacle); 
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
                int[] coordonnéesFourmiliere = coordonnees(ligneFourmiliere);
                fourmiliere = new Fourmiliere(coordonnéesFourmiliere[0], coordonnéesFourmiliere[1], MainWindow.TAILLE_FOURMILIERE);
            }

            // Initialisation de la nourriture
            if (ligneNourriture != 0) {
                int[] coordonnéesNourriture = coordonnees(ligneNourriture);
                nourriture = new LinkedList<Nourriture>();
                nourriture.add(new Nourriture(coordonnéesNourriture[0], coordonnéesNourriture[1], MainWindow.TAILLE_NOURRITURE));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Méthode qui récupère les coordonnées d'une ligne, séparées par des virgules
    private int[] coordonnees(int ligne) {
        int[] rep = new int[3];
        String[] texte = fichier.get(ligne).split(",");
        for (int i = 0; i < texte.length; i++) {
            rep[i] = Integer.parseInt(texte[i]);
        }
        return rep;
    }

}

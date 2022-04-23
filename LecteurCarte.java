import java.util.List;
import java.util.LinkedList;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

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
            fichier = Files.readAllLines(Paths.get(chemin), StandardCharsets.UTF_8);
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

    // Méthode qui convertis une Carte en une liste 
    private static List<String> carteVersListe(Carte c){
        List<String> res = new LinkedList<String>();
        Vecteur posFourmiliere = c.getFourmiliere().getPosition();
        Vecteur posNourriture = c.getNourriture().get(0).getPosition();
        res.add("*");
        res.add("* Ceci est une carte exportée");
        res.add("*");
        res.add("// FOURMILIERE //");
        res.add((int)posFourmiliere.x + "," + (int)posFourmiliere.y);
        res.add("// NOURRITURE //");
        res.add((int)posNourriture.x + "," + (int)posNourriture.y);
        res.add("// DEBUT OBSTACLE //");
        int nbObstacles = c.getObstacles().size();
        for(Obstacle o : c.getObstacles()){
            boolean bordureInvisible = (o == c.getObstacles().get(nbObstacles-1)) || (o == c.getObstacles().get(nbObstacles-2)) || (o == c.getObstacles().get(nbObstacles-3)) || (o == c.getObstacles().get(nbObstacles-4));
            if (!bordureInvisible){
                if (o.estVide()){
                    res.add("// NOUVEL OBSTACLE (vide)");
                } else {
                    res.add("// NOUVEL OBSTACLE");
                }
                for (Segment mur : o.getMurs()){
                    res.add((int)mur.pointA.x + "," + (int)mur.pointA.y);
                }
            }
        }
        res.add("// FIN OBSTACLE //");
        return res;
    }

    public static void exporter(Carte c, File f){
        List<String> lignes = carteVersListe(c);
        try {
            File nvFichier = f;
            if (!nvFichier.exists()) {
                nvFichier.createNewFile();
            }
            FileWriter fw = new FileWriter(nvFichier);
            BufferedWriter out = new BufferedWriter(fw);
            for (String ligne : lignes) {
                out.write(ligne);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}

import javax.swing.plaf.synth.SynthSeparatorUI;
import java.nio.file.*;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class LecteurCarte {
    private List<String> fichier;
    private LinkedList<Obstacle> obstacles;
    private static final String DEBUT_OBSTACLES = "// DEBUT OBSTACLE";
    private static final String FIN_OBSTACLES = "// FIN OBSTACLE";
    private static final String SEPARATION_OBSTACLES = "// NOUVEL OBSTACLE";

    public LecteurCarte(String chemin){
        try {
            fichier = Files.readAllLines(Paths.get(chemin));
            int ligneDebutObstacle = fichier.indexOf(DEBUT_OBSTACLES);
            int ligneFinObstacle = fichier.indexOf(FIN_OBSTACLES);

            // Boucle pour définir les obstacles de la carte
            obstacles = new LinkedList<Obstacle>();
            LinkedList<Vecteur> points = new LinkedList<Vecteur>();
            for(int i = ligneDebutObstacle+1; i<ligneFinObstacle; i++){
                String ligne = fichier.get(i); // on itère sur chaque ligne du fichier texte
                if (ligne.startsWith(SEPARATION_OBSTACLES) && (i != ligneDebutObstacle+1)){
                    obstacles.add(new Obstacle(points)); // on crée un nouvel obstacle qu'on ajoute dans la liste
                    points = new LinkedList<Vecteur>();  // on vide la liste des points
                }
                else if (ligne != "" && (i != ligneDebutObstacle+1)) {
                    String[] partiesLigne = ligne.split(",");
                    int x = Integer.parseInt(partiesLigne[0]);
                    int y = Integer.parseInt(partiesLigne[1]);
                    points.add(new Vecteur(x,y));
                }
            }
            obstacles.add(new Obstacle(points)); // On ajoute les derniers points dans la liste des obstacles
        }
        catch (Exception exc) {
            throw new RuntimeException("La carte n'a pas pu être lue!");
        }
    }

    public LinkedList<Obstacle> getObstacles(){
        return obstacles;
    }
}

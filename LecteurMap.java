import java.util.List;
import java.nio.file.*;

public class LecteurMap {
    private static final String DEBUT_OBSTACLE = "// DEBUT OBSTACLE";
    private static final String FIN_OBSTACLE = "// FIN OBSTACLE";
    public LecteurMap(String chemin){
        try {
            List<String> fichier = Files.readAllLines(Paths.get(chemin));
            int ligneDebutObstacle = fichier.lastIndexOf(DEBUT_OBSTACLE);
            int ligneFinObstacle = fichier.lastIndexOf(FIN_OBSTACLE);
        }
        catch (Exception exc) {
            throw new RuntimeException("La carte n'a pas pu être lue!");
        }
    }
}

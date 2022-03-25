import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import java.nio.file.*;
import java.util.LinkedList;

public class LecteurMap {
    private List<String> fichier;
    private static final String DEBUT_OBSTACLE = "// DEBUT OBSTACLE";
    private static final String FIN_OBSTACLE = "// FIN OBSTACLE";
    public LecteurMap(String chemin){
        try {
            fichier = Files.readAllLines(Paths.get(chemin));
        }
        catch (Exception exc) {
            throw new RuntimeException("La carte n'a pas pu être lue!");
        }
    }

    public LinkedList<Obstacle> getObstacles(){
        LinkedList<Obstacle> res = new LinkedList<Obstacle>();
        int ligneDebutObstacle = fichier.indexOf(DEBUT_OBSTACLE);
        int ligneFinObstacle = fichier.indexOf(FIN_OBSTACLE);
        for(int i = ligneDebutObstacle+1; i<ligneFinObstacle; i++){

        }
        return res;

    }
}

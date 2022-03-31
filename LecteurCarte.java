import javax.swing.plaf.synth.SynthSeparatorUI;
import java.nio.file.*;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class LecteurCarte {
    private List<String> fichier;
    private LinkedList<Obstacle> obstacles;
    private Fourmiliere fourmiliere;
    private LinkedList<Nourriture> nourriture;
    private static final String DEBUT_OBSTACLES = "// DEBUT OBSTACLE //";
    private static final String FIN_OBSTACLES = "// FIN OBSTACLE //";
    private static final String SEPARATION_OBSTACLES = "// NOUVEL OBSTACLE";
    private static final String FOURMILIERE = "// FOURMILIERE //";
    private static final String NOURRITURE = "// NOURRITURE //";

    public LinkedList<Obstacle> getObstacles(){
        return obstacles;
    }
    public Fourmiliere getFourmiliere(){
        return fourmiliere;
    }
    public LinkedList<Nourriture> getNourriture(){
        return nourriture;
    }
    
    public LecteurCarte(){
        obstacles = new LinkedList<Obstacle>();
        fourmiliere = new Fourmiliere(0,0,0);
        nourriture = new LinkedList<Nourriture>();
    }

    public LecteurCarte(String chemin){
        this();
        try {
            fichier = Files.readAllLines(Paths.get(chemin));
            int ligneDebutObstacle = fichier.indexOf(DEBUT_OBSTACLES)+1;
            int ligneFinObstacle = fichier.indexOf(FIN_OBSTACLES)-1;
            int ligneFourmiliere = fichier.indexOf(FOURMILIERE)+1; // Retourne -1 si il n'y a pas de fourmiliere
            int ligneNourriture = fichier.indexOf(NOURRITURE)+1; // idem

            // Boucle pour définir les obstacles de la carte
            LinkedList<Vecteur> points = new LinkedList<Vecteur>();
            for(int i = ligneDebutObstacle; i<=ligneFinObstacle; i++){
                String ligne = fichier.get(i); // on itère sur chaque ligne du fichier texte
                if (ligne.startsWith(SEPARATION_OBSTACLES) && (i != ligneDebutObstacle)){
                    obstacles.add(new Obstacle(points)); // on crée un nouvel obstacle qu'on ajoute dans la liste
                    points = new LinkedList<Vecteur>();  // on vide la liste des points
                }
                else if (ligne != "" && (i != ligneDebutObstacle)) {
                    String[] partiesLigne = ligne.split(",");
                    int x = Integer.parseInt(partiesLigne[0]);
                    int y = Integer.parseInt(partiesLigne[1]);
                    points.add(new Vecteur(x,y));
                }
            }
            obstacles.add(new Obstacle(points)); // On ajoute les derniers points dans la liste des obstacles
            
            // Fourmiliere
            if (ligneFourmiliere != 0){
                int[] tabFourmiliere = parties(ligneFourmiliere);
                int xFourmiliere = tabFourmiliere[0];
                int yFourmiliere = tabFourmiliere[1];
                int tailleFourmiliere = tabFourmiliere[2];
                fourmiliere = new Fourmiliere(xFourmiliere, yFourmiliere, tailleFourmiliere);
            }

            // Nourriture
            if (ligneNourriture != 0){
                int[] tabNourriture = parties(ligneNourriture);
                int xNourriture = tabNourriture[0];
                int yNourriture = tabNourriture[1];
                int tailleNourriture = tabNourriture[2];
                nourriture = new LinkedList<Nourriture>();
                nourriture.add(new Nourriture(xNourriture, yNourriture, tailleNourriture));
            }
        }
        catch (Exception exc) {
            System.out.println(exc);
        }
    }

    private int[] parties(int index){
        int[] res = new int[3];
        String[] tab = fichier.get(index).split(",");
        for(int i = 0; i<tab.length; i++){
            res[i]= Integer.parseInt(tab[i]);
        }
        return res;
    }

}

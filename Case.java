import java.awt.*;

public abstract class Case {
    
    //Coordonnées et couleur d'une case
    protected int x;
    protected int y;
    protected Color couleur;

    private Color getCouleur() {
        return couleur;
    }
}

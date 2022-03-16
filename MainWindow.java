import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final int LARGEUR = 1280;
    private final int HAUTEUR = 720;

    public MainWindow() {
        // Création de l'interface graphique
        this.setSize(LARGEUR, HAUTEUR);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        Insets insets = getInsets();
        
        JPanel conteneur = (JPanel)this.getContentPane();
        // Création de l'objet Carte
        Carte carte = new Carte();
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));

        Parametres parametres = new Parametres();
        parametres.setPreferredSize(new Dimension((int)(0.2*LARGEUR), HAUTEUR-insets.top));

        conteneur.add(parametres, BorderLayout.WEST);
        conteneur.add(carte, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }

}
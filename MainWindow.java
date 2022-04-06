import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{
    private static final int LARGEUR = 1280;
    private static final int HAUTEUR = 720;
    private JButton reinitialiser, valider, editer;
    private Color fondBouton = new Color(234, 234, 234);
    private static JPanel conteneur;
    private static Insets insets;
    private static Parametres param;
    public static Carte carte;

    public MainWindow() {
        // Création de l'interface graphique
        this.setSize(LARGEUR, HAUTEUR);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Ant Colony Optimization");
        insets = getInsets();
        conteneur = (JPanel)this.getContentPane();

        // Création du JPanel Parametres
        param = new Parametres();
        param.setPreferredSize(new Dimension((int)(0.2*LARGEUR), HAUTEUR-insets.top));

        // Création du premier objet Carte
        carte = new Carte(param.getObstacles(), param.getNourriture(), param.getFourmiliere());
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));

        // Création des boutons permettant l'interaction entre les objets Carte/Parametres
        JPanel boutons = new JPanel();
        boutons.setBackground(Color.red);
        boutons.setLayout(new FlowLayout(FlowLayout.CENTER));
        boutons.setBackground(param.getFond());
        boutons.setMaximumSize(new Dimension(getWidth(), 20));
        param.add(boutons);

        reinitialiser = new JButton("Réinitialiser");
        reinitialiser.addActionListener(this);
        reinitialiser.setBackground(fondBouton);
		boutons.add(reinitialiser);

        valider = new JButton("Valider");
        valider.addActionListener(this);
        valider.setBackground(fondBouton);
        boutons.add(valider);

        // Ajouts des JPanel
        conteneur.add(param, BorderLayout.WEST);
        conteneur.add(carte, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }

    // Gestions des interactions avec l'utilisateur
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reinitialiser) {
            carte.reinitialiser();
        }
        if (e.getSource() == valider) {
            modifierCarte();
        }
    }

    public static void modifierCarte(){
        conteneur.remove(carte);
        carte.getTimer().stop();
        carte = new Carte(param.getObstacles(), param.getNourriture(), param.getFourmiliere());
        conteneur.add(carte, BorderLayout.EAST);
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));
        conteneur.revalidate();
        conteneur.repaint();
    }

}
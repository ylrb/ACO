import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{
    private final int LARGEUR = 1280;
    private final int HAUTEUR = 720;
    private JButton reinitialiser, valider;
    private JPanel conteneur;
    private Insets insets;
    private Parametres param;
    private Carte carte;

    public MainWindow() {
        // Création de l'interface graphique
        this.setSize(LARGEUR, HAUTEUR);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        insets = getInsets();
        conteneur = (JPanel)this.getContentPane();

        // Création du JPanel Parametres
        param = new Parametres();
        param.setPreferredSize(new Dimension((int)(0.2*LARGEUR), HAUTEUR-insets.top));

        // Création du premier objet Carte
        carte = new Carte(param.getDt(), param.getNbFourmis(), param.getAffichagePhero());
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));

        // Création des boutons permettant l'interaction entre les objets Carte/Parametres
        JPanel boutons = new JPanel();
        boutons.setLayout(new FlowLayout(FlowLayout.CENTER));
        boutons.setBackground(param.getFond());
        boutons.setMaximumSize(new Dimension(getWidth(), 20));
        param.add(boutons);

        reinitialiser = new JButton("Réinitialiser");
        reinitialiser.addActionListener(this);
		boutons.add(reinitialiser);

        valider = new JButton("Valider");
        valider.addActionListener(this);
        boutons.add(valider);

        // Ajouts des JPanel
        conteneur.add(param, BorderLayout.WEST);
        conteneur.add(carte, BorderLayout.EAST);

        // Fin
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
            conteneur.remove(carte);
            carte.stop();
            carte = new Carte(param.getDt(), param.getNbFourmis(), param.getAffichagePhero());
            conteneur.add(carte, BorderLayout.EAST);
            carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));
            conteneur.revalidate();
            conteneur.repaint();
        }
    }


}
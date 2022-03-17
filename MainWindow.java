import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{
    private final int LARGEUR = 1280;
    private final int HAUTEUR = 720;
    private Carte carte;
    private JSpinner champDt, champNombreFourmis;
    private JButton reinitialiser, valider;
    private JCheckBox cocherPheromones;
    private int dt = 10;
    private int nombreFourmis = 30;
    private boolean afficherPheromones = true;

    public MainWindow() {
        // Création de l'interface graphique
        this.setSize(LARGEUR, HAUTEUR);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        Insets insets = getInsets();
        JPanel conteneur = (JPanel)this.getContentPane();

        // Création de l'objet Carte
        carte = new Carte();
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));

        // PARAMETRES
        Parametres parametres = new Parametres();
        parametres.setPreferredSize(new Dimension((int)(0.2*LARGEUR), HAUTEUR-insets.top));

        JLabel texteChampDt = new JLabel("Période de rafraichissement");
        parametres.add(texteChampDt);
        champDt = new JSpinner(new SpinnerNumberModel(dt, 0, 100, 1));
		champDt.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                dt = (int)((JSpinner)e.getSource()).getValue();
            }
        });
        champDt.setMaximumSize( new Dimension(100,20) );
		parametres.add(champDt);

        JLabel texteChampNombreFourmis = new JLabel("Nombre de fourmis");
        parametres.add(texteChampNombreFourmis);
        champNombreFourmis = new JSpinner(new SpinnerNumberModel(nombreFourmis, 0, 50, 5));
		champNombreFourmis.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                nombreFourmis = (int)((JSpinner)e.getSource()).getValue();
            }
        });
        champNombreFourmis.setMaximumSize( new Dimension(100,20) );
		parametres.add(champNombreFourmis);

        cocherPheromones = new JCheckBox("Affichage des phéromones", true);
        cocherPheromones.addActionListener(this);
        parametres.add(cocherPheromones);

        parametres.add(Box.createVerticalGlue());

        reinitialiser = new JButton("Réinitialiser");
        reinitialiser.addActionListener(this);
		parametres.add(reinitialiser);

        valider = new JButton("Valider");
        valider.addActionListener(this);
        parametres.add(valider);

        // Ajouts des JPanel
        conteneur.add(parametres, BorderLayout.WEST);
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
            carte.valider(dt, nombreFourmis, afficherPheromones);
        }
        if(e.getSource() == cocherPheromones){
            JCheckBox c = (JCheckBox)e.getSource();
            if (c.isSelected()){
                afficherPheromones = true;
            } else{
                afficherPheromones = false;
            }
        }
        /*
        if(e.getSource()== champNombreFourmis){
            int k = Integer.parseInt(champNombreFourmis.getValue());
            carte.changechampNombreFourmis(k);
        } */     
    }


}
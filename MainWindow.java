import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{
    private final int LARGEUR = 1280;
    private final int HAUTEUR = 720;
    private Carte carte;
    private JSpinner champDt, champNbFourmis;
    private JButton reinitialiser, valider;
    private JCheckBox cocherPhero;
    private int dt = 10;
    private int nbFourmis = 30;
    private boolean afficherPhero = true;

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

        JLabel txtChampDt = new JLabel("Période de rafraichissement");
        parametres.add(txtChampDt);
        champDt = new JSpinner(new SpinnerNumberModel(dt, 0, 100, 1));
		champDt.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                dt = (int)((JSpinner)e.getSource()).getValue();
            }
        });
        champDt.setMaximumSize( new Dimension(100,20) );
		parametres.add(champDt);

        JLabel txtChampNbFourmis = new JLabel("Nombre de fourmis");
        parametres.add(txtChampNbFourmis);
        champNbFourmis = new JSpinner(new SpinnerNumberModel(nbFourmis, 0, 100, 5));
		champNbFourmis.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                nbFourmis = (int)((JSpinner)e.getSource()).getValue();
            }
        });
        champNbFourmis.setMaximumSize( new Dimension(100,20) );
		parametres.add(champNbFourmis);

        cocherPhero = new JCheckBox("Affichage des phéromones", true);
        cocherPhero.addActionListener(this);
        parametres.add(cocherPhero);

        parametres.add(Box.createVerticalGlue());

        reinitialiser = new JButton("Réinitialiser");
        reinitialiser.addActionListener(this);
		parametres.add(reinitialiser);

        valider = new JButton("Valider");
        valider.addActionListener(this);
        parametres.add(valider);

        conteneur.add(parametres, BorderLayout.WEST);
        conteneur.add(carte, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==reinitialiser) {
            carte.reinitialiser();
        }
        if (e.getSource()==valider) {
            carte.valider(dt, nbFourmis, afficherPhero);
        }
        if(e.getSource() == cocherPhero){
            JCheckBox c = (JCheckBox)e.getSource();
            if (c.isSelected()){
                afficherPhero = true;
            } else{
                afficherPhero = false;
            }
        }
        /*
        if(e.getSource()== champNbFourmis){
            int k = Integer.parseInt(champNbFourmis.getValue());
            carte.changechampNbFourmis(k);
        } */     
    }


}
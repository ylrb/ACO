import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{
    private final int LARGEUR = 1280;
    private final int HAUTEUR = 720;
    public Carte carte;
    public JSpinner dt;
    public JSpinner nbFourmis;
    public JButton reset;

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

        JLabel txtDt = new JLabel("Période de rafraichissement (en ms)");
        parametres.add(txtDt);
        dt = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
		dt.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                //int k = Integer.parseInt(dt.getValue());
                //carte.changeDt(k);
            }
        });
        dt.setMaximumSize( new Dimension(100,20) );
        dt.setAlignmentX(Component.CENTER_ALIGNMENT);
		parametres.add(dt);

        JLabel txtNbFourmis = new JLabel("Nombre de fourmis");
        parametres.add(txtNbFourmis);
        nbFourmis = new JSpinner(new SpinnerNumberModel(30, 0, 100, 5));
		//nbFourmis.addActionListener(this);
        nbFourmis.setMaximumSize( new Dimension(100,20) );
        nbFourmis.setAlignmentX(Component.CENTER_ALIGNMENT);
		parametres.add(nbFourmis);

        parametres.add(Box.createVerticalGlue());

        reset = new JButton("Reset");
        reset.addActionListener(this);
		parametres.add(reset);

        conteneur.add(parametres, BorderLayout.WEST);
        conteneur.add(carte, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==reset) {
            carte.reset();
        }
        /*
        if(e.getSource()== dt && dt.getText() != null){
            int k = Integer.parseInt(dt.getValue());
            carte.changeDt(k);
        }
        if(e.getSource()== nbFourmis){
            int k = Integer.parseInt(nbFourmis.getValue());
            carte.changeNbFourmis(k);
        } */     
    }


}
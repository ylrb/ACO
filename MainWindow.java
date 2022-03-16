import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements ActionListener{
    private final int LARGEUR = 1280;
    private final int HAUTEUR = 720;
    public Carte carte;
    public JTextField dt;
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

        Parametres parametres = new Parametres();
        parametres.setPreferredSize(new Dimension((int)(0.2*LARGEUR), HAUTEUR-insets.top));

        dt = new JTextField("5");
		dt.addActionListener(this);
		parametres.add(dt);

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
            int k = Integer.parseInt(dt.getText());
            carte.reset();
        }
        if(e.getSource()== dt){
            int k = Integer.parseInt(dt.getText());
            carte.changeDt(k);
        }
    }


}
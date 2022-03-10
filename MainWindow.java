import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final int largeur = 1280;
    private final int hauteur = 720;

    public MainWindow() {
        this.setSize(largeur,hauteur);

        //Conteneur principal
        JPanel conteneur = new JPanel();
        //conteneur.setBounds(100,100,getWidth()-getInsets().left-getInsets().right,getHeight()-getInsets().top-getInsets().bottom);
        Carte carte = new Carte();
        //Parametres parametres = new Parametres();
        JPanel parametres = new JPanel();
        parametres.setBackground(Color.red);
        parametres.setLayout(null);
        parametres.setBounds(0,0,(int)(0.2*getWidth()), (int)getHeight());

        carte.setBounds((int)(0.2*getWidth()), 0, (int)(0.8*getWidth()), getHeight());

        conteneur.setLayout(null);
        conteneur.add(parametres);
        conteneur.add(carte);
    
        this.setResizable(false);
        this.setContentPane(conteneur);
        this.setPreferredSize(new Dimension(largeur, hauteur));
        this.setResizable(false);
        this.add(conteneur);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

}
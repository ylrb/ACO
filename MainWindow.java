import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final int largeur = 1280;
    private final int hauteur = 720;

    public MainWindow() {

        //Conteneur principal
        JSplitPane conteneur = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        conteneur.setBackground(Color.black);
        
        Carte carte = new Carte();
        JPanel parametres = new JPanel();
        parametres.setBackground(Color.red);
        //carte.setBounds((int)(0.2*containerWidth), 0, (int)(0.8*containerWidth), containerHeight);
        conteneur.setRightComponent(carte);
        conteneur.setLeftComponent(parametres);
        //container.setDividerLocation(0.2);
        conteneur.setResizeWeight(0.2);
        conteneur.setEnabled(false);

        this.add(conteneur, BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(largeur, hauteur));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        repaint();
    }

}
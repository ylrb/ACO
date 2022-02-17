import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final int width = 1000;
    private final int height = 700;

    public MainWindow() {

        //Conteneur principale
        JSplitPane container = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        container.setBackground(Color.black);
        
        Carte carte = new Carte();
        JPanel settings = new JPanel();
        settings.setBackground(Color.red);
        //carte.setBounds((int)(0.2*containerWidth), 0, (int)(0.8*containerWidth), containerHeight);
        container.setRightComponent(carte);
        container.setLeftComponent(settings);
        //container.setDividerLocation(0.2);
        container.setResizeWeight(0.2);
        container.setEnabled(false);

        this.add(container, BorderLayout.CENTER);
        this.setMinimumSize(new Dimension(width, height));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        repaint();
    }

}
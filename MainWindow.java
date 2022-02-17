import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final int width = 1000;
    private final int height = 700;

    public MainWindow() {
        this.setBounds(100,100,1000,700);

        JPanel container = new JPanel();
        container.setLayout(null);
        container.setBackground(Color.black);
        container.setSize(getWidth(), getHeight());
        int containerHeight = this.getHeight();
        int containerWidth = this.getWidth();
        System.out.println(containerHeight + containerWidth);
        this.add(container);
        
        Carte carte = new Carte();
        carte.setBounds((int)(0.2*containerWidth), 0, (int)(0.8*containerWidth), containerHeight);
        container.add(carte);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        repaint();
    }

}
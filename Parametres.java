import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class Parametres extends JPanel implements ActionListener{

	public Parametres(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		setBackground(new Color(214,214,214));

		JLabel titre = new JLabel("ANT COLONY OPTIMIZATION");
		titre.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titre);

	}
	
	public void actionPerformed(ActionEvent e) {
        
    }
}
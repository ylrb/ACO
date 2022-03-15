import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

public class Parametres extends JPanel{

	public Parametres(){
		Box box = new Box(BoxLayout.Y_AXIS);
		add(box);
		setBackground(new Color(214,214,214));

		box.add(Box.createVerticalStrut(30));

		JLabel titre = new JLabel("ANT COLONY OPTIMIZATION");
		titre.setAlignmentX(CENTER_ALIGNMENT);
		this.add(titre);
	}
	
}
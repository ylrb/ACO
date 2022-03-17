import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class Parametres extends JPanel implements ActionListener{
	private final Color FOND_PARAM = new Color(214,214,214);

	public Color getFond(){
		return FOND_PARAM;
	}

	public Parametres(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		setBackground(FOND_PARAM);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titre = new JLabel("ANT COLONY OPTIMIZATION");
		titre.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titre);

	}
	
	public void actionPerformed(ActionEvent e) {
        
    }
}
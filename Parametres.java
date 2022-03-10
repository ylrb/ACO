import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;

public class Parametres extends JPanel{

	public Parametres(){
		setLayout(null);
		setVisible(true);
	}
	
	public void paint(Graphics g){
		g.setColor(Color.red);
		g.fillRect(0,0,getWidth(),getHeight());
	}
}
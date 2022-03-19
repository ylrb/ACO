import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class Parametres extends JPanel implements ActionListener{
	private final Color FOND_PARAM = new Color(214,214,214);
	private JCheckBox cocherPheromones;
	// Paramètres par défaut de la Carte
	private int dt = 0;
    private int nombreFourmis = 50;
    private boolean afficherPheromones = true;

	public int getDt(){
		return dt;
	}

	public int getNbFourmis(){
		return nombreFourmis;
	}

	public boolean getAffichagePhero(){
		return afficherPheromones;
	}

	public Color getFond(){
		return FOND_PARAM;
	}

	public Parametres(){
		// Initialisation du JPanel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		setBackground(FOND_PARAM);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel titre = new JLabel("ANT COLONY OPTIMIZATION");
		titre.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titre);
		add(Box.createVerticalStrut(20));


		// Créations des champs permettant de modifier les paramètres de la carte
		JPanel champs = new JPanel();
        champs.setLayout(new BoxLayout(champs, BoxLayout.Y_AXIS));
        champs.add(Box.createHorizontalGlue());
        champs.setBackground(FOND_PARAM);
        champs.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(champs);


        JLabel texteChampDt = new JLabel("Période de rafraichissement");
        texteChampDt.setAlignmentX(Component.LEFT_ALIGNMENT);
        champs.add(texteChampDt);
        JSpinner champDt = new JSpinner(new SpinnerNumberModel(dt, 0, 100, 1));
		champDt.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                dt = (int)((JSpinner)e.getSource()).getValue();
            }
        });
        champDt.setMaximumSize( new Dimension(100,30) );
        champDt.setAlignmentX(Component.LEFT_ALIGNMENT);
		champs.add(champDt);
		champs.add(Box.createVerticalStrut(20));

        JLabel texteChampNbFourmis = new JLabel("Nombre de fourmis");
        texteChampNbFourmis.setAlignmentX(Component.LEFT_ALIGNMENT);
        champs.add(texteChampNbFourmis);
        JSpinner champNombreFourmis = new JSpinner(new SpinnerNumberModel(nombreFourmis, 0, 100, 5));
		champNombreFourmis.addChangeListener(new ChangeListener() {      
            public void stateChanged(ChangeEvent e) {
                nombreFourmis = (int)((JSpinner)e.getSource()).getValue();
            }
        });
        champNombreFourmis.setMaximumSize( new Dimension(100,30) );
        champNombreFourmis.setAlignmentX(Component.LEFT_ALIGNMENT);
		champs.add(champNombreFourmis);
		add(Box.createVerticalStrut(20));

        cocherPheromones = new JCheckBox("Affichage des phéromones", true);
        cocherPheromones.addActionListener(this);
        cocherPheromones.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(cocherPheromones);
        add(Box.createVerticalGlue());

	}
	
	// Gestion des interactions avec l'utilisateurs
	public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cocherPheromones){
            JCheckBox c = (JCheckBox)e.getSource();
            if (c.isSelected()){
                afficherPheromones = true;
            } else{
                afficherPheromones = false;
            }
        }
    }
}
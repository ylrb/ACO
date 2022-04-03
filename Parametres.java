import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.LinkedList;

public class Parametres extends JPanel implements ActionListener{
	private final Color FOND_PARAM = new Color(214,214,214);
	private JCheckBox cocherPheromones;
	// Paramètres par défaut de la carte
	private int dt = 0;
    private int nombreFourmis = 50;
    private boolean afficherPheromones = true;
    private String[] listMap = { "Carte par défaut", "Labyrinthe", "Double Pont", "Map 4", "Map 5" };
    private JComboBox<String> selectMap = new JComboBox<String>(listMap);
    private LinkedList<Obstacle> obstacles = (new LecteurCarte("assets/cartes/bordures.txt")).getObstacles();
    private Fourmiliere fourmiliere = (new LecteurCarte("assets/cartes/bordures.txt")).getFourmiliere();
    private LinkedList<Nourriture> nourritures = (new LecteurCarte("assets/cartes/bordures.txt")).getNourriture();

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

    public LinkedList<Obstacle> getObstacles() {
        return obstacles;
    }
    
    public LinkedList<Nourriture> getNourriture(){
        return nourritures;
    }

    public Fourmiliere getFourmiliere(){
        return fourmiliere;
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
        champDt.setMaximumSize(new Dimension(100,30) );
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
        add(Box.createVerticalStrut(20));

        selectMap.setMaximumSize(selectMap.getPreferredSize());
        selectMap.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectMap.setSelectedIndex(0);
        selectMap.addActionListener(this);
        add(selectMap);

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

        if(e.getSource() == selectMap){
            int k = selectMap.getSelectedIndex();
            LecteurCarte newCarte = new LecteurCarte();
            switch(k){
                case 0: 
                    newCarte = new LecteurCarte("assets/cartes/bordures.txt");
                    break;
                case 1:
                    newCarte = new LecteurCarte("assets/cartes/labyrinthe.txt");
                    break;
                case 2:
                    newCarte = new LecteurCarte("assets/cartes/pont.txt");
                    break;
                default:
                    newCarte = new LecteurCarte("assets/cartes/bordures.txt");
                    break;
            }
            obstacles = newCarte.getObstacles();
            nourritures = newCarte.getNourriture();
            fourmiliere = newCarte.getFourmiliere();
        }
    }
}
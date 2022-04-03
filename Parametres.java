import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.util.LinkedList;
import java.awt.image.BufferedImage;
import java.io.*;

public class Parametres extends JPanel implements ActionListener {
    // Variables pour l'affichage
    private final Color FOND_PARAM = new Color(214, 214, 214);
    private JCheckBox cocherPheromones;

    // Paramètres par défaut de la nouvelle carte
    private static int dt = 0;
    private static int nombreFourmis = 50;
    private String[] listeCartes = { "Carte par défaut", "Labyrinthe", "Double Pont", "Map 4", "Map 5" };
    private JComboBox<String> selectionCartes = new JComboBox<String>(listeCartes);

    // Élements du terrain
    private LinkedList<Obstacle> obstacles = (new LecteurCarte("assets/cartes/bordures.txt")).getObstacles();
    private Fourmiliere fourmiliere = (new LecteurCarte("assets/cartes/bordures.txt")).getFourmiliere();
    private LinkedList<Nourriture> nourritures = (new LecteurCarte("assets/cartes/bordures.txt")).getNourriture();

    // Images et tailles
    static BufferedImage imageFourmiA, imageFourmiB, imageFourmiliere, imageNourriture, imageFond;
    static final int TAILLE_FOURMI = 20;
    static final int TAILLE_FOURMILIERE = 40;
    static final int TAILLE_NOURRITURE = 30;

    public static int getDt() {
        return dt;
    }

    public static int getNombreFourmis() {
        return nombreFourmis;
    }

    public Color getFond() {
        return FOND_PARAM;
    }

    public LinkedList<Obstacle> getObstacles() {
        return obstacles;
    }

    public LinkedList<Nourriture> getNourriture() {
        return nourritures;
    }

    public Fourmiliere getFourmiliere() {
        return fourmiliere;
    }

    public Parametres() {
        importerImages();

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
                dt = (int) ((JSpinner) e.getSource()).getValue();
            }
        });
        champDt.setMaximumSize(new Dimension(100, 30));
        champDt.setAlignmentX(Component.LEFT_ALIGNMENT);
        champs.add(champDt);
        champs.add(Box.createVerticalStrut(20));

        JLabel texteChampNbFourmis = new JLabel("Nombre de fourmis");
        texteChampNbFourmis.setAlignmentX(Component.LEFT_ALIGNMENT);
        champs.add(texteChampNbFourmis);
        JSpinner champNombreFourmis = new JSpinner(new SpinnerNumberModel(nombreFourmis, 0, 100, 5));
        champNombreFourmis.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                nombreFourmis = (int) ((JSpinner) e.getSource()).getValue();
            }
        });
        champNombreFourmis.setMaximumSize(new Dimension(100, 30));
        champNombreFourmis.setAlignmentX(Component.LEFT_ALIGNMENT);
        champs.add(champNombreFourmis);
        add(Box.createVerticalStrut(20));

        cocherPheromones = new JCheckBox("Affichage des phéromones", true);
        cocherPheromones.addActionListener(this);
        cocherPheromones.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(cocherPheromones);
        add(Box.createVerticalStrut(20));

        selectionCartes.setMaximumSize(selectionCartes.getPreferredSize());
        selectionCartes.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectionCartes.setSelectedIndex(0);
        selectionCartes.addActionListener(this);
        add(selectionCartes);

        add(Box.createVerticalGlue());
    }

    // Gestion des interactions avec l'utilisateurs
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cocherPheromones) {
            JCheckBox c = (JCheckBox) e.getSource();
            if (c.isSelected()) {
                Carte.setAffichagePheromones(true);
            } else {
                Carte.setAffichagePheromones(false);
            }
        }
        if (e.getSource() == selectionCartes) {
            LecteurCarte nouvelleCarte = new LecteurCarte();
            switch (selectionCartes.getSelectedIndex()) {
                case 0:
                    nouvelleCarte = new LecteurCarte("assets/cartes/bordures.txt");
                    break;
                case 1:
                    nouvelleCarte = new LecteurCarte("assets/cartes/labyrinthe.txt");
                    break;
                case 2:
                    nouvelleCarte = new LecteurCarte("assets/cartes/pont.txt");
                    break;
                default:
                    nouvelleCarte = new LecteurCarte("assets/cartes/bordures.txt");
                    break;
            }
            obstacles = nouvelleCarte.getObstacles();
            nourritures = nouvelleCarte.getNourriture();
            fourmiliere = nouvelleCarte.getFourmiliere();
        }
    }

    // Importation et redimensionnement des images qu'on importe en tant que BufferedImage
    private void importerImages() {
        try {
            // On importe les images
            imageFourmiA = ImageIO.read(new File("assets/images/FourmiA.png"));
            imageFourmiB = ImageIO.read(new File("assets/images/FourmiB.png"));
            imageFourmiliere = ImageIO.read(new File("assets/images/Fourmiliere.png"));
            imageNourriture = ImageIO.read(new File("assets/images/Nourriture.png"));
            imageFond = ImageIO.read(new File("assets/images/Fond.png"));

            // On leur donne la taille désirée
            imageFourmiA = redimensionner(imageFourmiA, TAILLE_FOURMI);
            imageFourmiB = redimensionner(imageFourmiB, TAILLE_FOURMI);
            imageFourmiliere = redimensionner(imageFourmiliere, TAILLE_FOURMILIERE);
            imageNourriture = redimensionner(imageNourriture, TAILLE_NOURRITURE);
            imageFond = redimensionner(imageFond, 1025);

        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire les fichiers images.");
        }
    }

    // Redimensionne l'image de fourmi à la taille désirée
    private static BufferedImage redimensionner(BufferedImage img, int largeurVoulue) {
        int largeur = img.getWidth();
        int hauteur = img.getHeight();
        int hauteurVoulue = (largeurVoulue * hauteur) / largeur; // Simple produit en croix

        // On crée une nouvelle image vide la taille désirée
        BufferedImage nouvelleImage = new BufferedImage(largeurVoulue, hauteurVoulue, img.getType());
        Graphics2D g = nouvelleImage.createGraphics();
 
        // On place l'image dans cette nouvelle image de manière à ce qu'elle la remplisse, par interpolation
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, largeurVoulue, hauteurVoulue, 0, 0, largeur, hauteur, null);
        g.dispose();

        return nouvelleImage;
    }
}
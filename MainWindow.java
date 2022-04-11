import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainWindow extends JFrame implements ActionListener{
    private static final int LARGEUR = 1280;
    private static final int HAUTEUR = 720;
    private static final Color FONDBOUTON = new Color(234, 234, 234);
    private JButton reinitialiser, valider;
    private static JPanel conteneur;
    private static Insets insets;
    private static Parametres param;
    public static Carte carte;
    
    // images et tailles
    public static BufferedImage imageFourmiA, imageFourmiB, imageFourmiliere, imageNourriture, imageFond;
    public static final int TAILLE_FOURMI = 20;
    public static final int TAILLE_FOURMILIERE = 40;
    public static final int TAILLE_NOURRITURE = 30;

    public MainWindow() {
        importerImages();

        // Création de l'interface graphique
        this.setSize(LARGEUR, HAUTEUR);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Ant Colony Optimization");
        insets = getInsets();
        conteneur = (JPanel)this.getContentPane();

        // Création du JPanel Parametres
        param = new Parametres();
        param.setPreferredSize(new Dimension((int)(0.2*LARGEUR), HAUTEUR-insets.top));

        // Création du premier objet Carte
        carte = new Carte(param.getObstacles(), param.getNourriture(), param.getFourmiliere());
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));

        // Création des boutons permettant l'interaction entre les objets Carte/Parametres
        JPanel boutons = new JPanel();
        boutons.setBackground(Color.red);
        boutons.setLayout(new FlowLayout(FlowLayout.CENTER));
        boutons.setBackground(param.getFond());
        boutons.setMaximumSize(new Dimension(getWidth(), 20));
        param.add(boutons);

        reinitialiser = new JButton("Réinitialiser");
        reinitialiser.addActionListener(this);
        reinitialiser.setBackground(FONDBOUTON);
		boutons.add(reinitialiser);

        valider = new JButton("Valider");
        valider.addActionListener(this);
        valider.setBackground(FONDBOUTON);
        boutons.add(valider);

        // Ajouts des JPanel
        conteneur.add(param, BorderLayout.WEST);
        conteneur.add(carte, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        repaint();
    }

    // Gestions des interactions avec l'utilisateur
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reinitialiser) {
            carte.reinitialiser();
        }
        if (e.getSource() == valider) {
            modifierCarte();
        }
    }

    public static void modifierCarte(){
        conteneur.remove(carte);
        carte.getTimer().stop();
        carte = new Carte(param.getObstacles(), param.getNourriture(), param.getFourmiliere());
        conteneur.add(carte, BorderLayout.EAST);
        carte.setPreferredSize(new Dimension((int)(0.8*LARGEUR), HAUTEUR-insets.top));
        conteneur.revalidate();
        conteneur.repaint();
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
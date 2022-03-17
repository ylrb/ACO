import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Fourmi {

    // Vecteurs propres à la fourmi
    protected Vecteur position;
    protected Vecteur direction;
    protected Vecteur errance;

    // Autres
    protected int sensRotation; // 0 si pas de rotation, sinon -1 ou 1
    protected ArrayList<Vecteur> contactMurs = new ArrayList<Vecteur>();

    // Tous les coefficients des forces (leur poids)
    protected static final double COEFF_ERRANCE = 0.1;
    protected static final double COEFF_ATTRACTION_PHEROMONES = 1;
    protected static final double COEFF_ATTRACTION_NOURRITURE = 10;
    protected static final double COEFF_ATTRACTION_FOURMILIERE = 10;
    protected static final double COEFF_REPULSION_MURS = 2.5;
    
    // Grandeurs définies
    protected static final double AMPLITUDE_ERRANCE = 5; // Amplitude max de la variation du vecteur errance
    protected static final double PORTEE_VUE = 70; // Distance à laquelle les fourmis peuvent voir les nourritures, pheromones, la fourmilière etc..
    protected static final double PORTEE_VUE_MUR = 70; // Distance à laquelle les fourmis considère les murs devant elles
    protected static final double ANGLE_VUE = 45; // Angle de vision des fourmis (en degrés)
    protected static final double ANGLE_MIN_MUR = 40; // Angle critique dans le cas des murs (cf. calcul de la force de répulsion) (en degrés)
    protected static final double PONDERATION_TAUX = 10; // Plus cette valeur est grande, moins la pondération des attractions aux phéromones par rapport aux taux est importante
    protected static final double ANGLE_ROTATION = 0.5;

    private static final boolean AFFICHAGE_DIRECTION = false; // Doit-on visualiser la direction de la fourmi


    public Fourmi(double x, double y) {
        position = new Vecteur(x,y);
        direction = new Vecteur(2*Math.random()-1,2*Math.random()-1);
        direction.unitaire();
        errance = direction;
    }

    public Fourmi(double x, double y, Vecteur dir) {
        this(x,y);
        direction = dir;
    }

    public Vecteur getPosition() {
        return new Vecteur(position.x,position.y);
    }

    public void setPosition(Vecteur nvPosition){
        position = nvPosition;
    }

    public Vecteur getDirection() {
        return new Vecteur(direction.x,direction.y);
    }

    // Fait avancer la fourmi dans la nouvelle direction qui est déterminée selon son environnement
    public void avancer(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheromones, ArrayList<Obstacle> obstacles) {
        calculErrance();
        calculNouvelleDirection(nourritures, fourmiliere, pheromones, obstacles);
        position.x += 2*direction.x;
        position.y += 2*direction.y;
    }

    // Calcule la nouvelle orientation du vecteur errance
    private void calculErrance() { 
        errance.tourner((2*Math.random()-1)*(Math.PI/180)*AMPLITUDE_ERRANCE); // Amplitude en degré convertie en radians, comprise dans un intervalle défini
    }

    // Détermine la nouvelle direction de la fourmi en fonction des éléments de son environnement
    protected abstract void calculNouvelleDirection(ArrayList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheromones, ArrayList<Obstacle> obstacles);
    
    // Indique si la fourmi a des phéromones dans son champ de vision
    protected boolean pheromonesEnVue(LinkedList<Pheromone> pheromones) { 
        boolean rep = false;
        Vecteur distance = new Vecteur();
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&(direction.angle(distance) < Math.toRadians(ANGLE_VUE))) {
                // Met rep à true si la phéromone se trouve dans le champ de visions de la fourmi
                rep = true;
                break;
            }
        }
        return rep;
    }

    // Calcule l'attraction d'une fourmi aux nourritures dans son champ de vision
    protected Vecteur calculAttractionPheromones(LinkedList<Pheromone> pheromones, boolean initial) {
        Vecteur rep = new Vecteur();
        Vecteur distance = new Vecteur();
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&((direction.angle(distance) < Math.toRadians(ANGLE_VUE))||(initial))) {
                // Augmente rep si la phéromone se trouve dans le champ de vision de la fourmi
                rep = rep.somme(p.getPosition().soustrait(getPosition()),1,p.getTaux()/100+PONDERATION_TAUX);
            }
        }
        rep.unitaire();
        return rep;
    }

    // On détermine dans quels murs va la fourmi
    public ArrayList<Segment> mursSecants(ArrayList<Obstacle> obstacles) {
        Vecteur p2 = new Vecteur(getPosition().x+PORTEE_VUE_MUR*direction.x,getPosition().y+PORTEE_VUE_MUR*direction.y);
        Segment segmentVue = new Segment(getPosition(),p2);
        ArrayList<Segment> murs = new ArrayList<Segment>();
        Vecteur pointSecant = new Vecteur();
        contactMurs.clear();
        for (Obstacle o : obstacles) {
            for (Segment s : o.getMurs()) {
                pointSecant = segmentVue.secante(s);
                if (pointSecant != null) {
                    murs.add(s);
                    contactMurs.add(pointSecant);
                }
            }
        }
        return murs;
    }

    // On isole le segment de mur le plus proche de la fourmi
    public Segment segmentLePlusProche(ArrayList<Segment> murs) {
        int min = 0; // Indice du point le plus proche de la fourmi
        int i = 0;
        double distanceMin = 1000; // Distance minimale à la fourmi
        for (Vecteur v : contactMurs) { 
            if (position.distance(v) < distanceMin) {
                distanceMin = position.distance(v);
                min = i;
            }
            i++;
        }
        return murs.get(min);
    }

    // On détermine le sens rotation de la fourmi en calculant l'augmentation d'angle par rapport à une direction hypothétique
    public void angleRotationMur(Segment s) {
        Vecteur direction2 = direction.tourner2(0.1);
        Vecteur mur = s.pointA.soustrait(s.pointB);
        double angle1 = mur.angle(direction);
        double angle2 = mur.angle(direction2);
        if (Math.abs(angle1-Math.PI/2) < Math.abs(angle2-Math.PI/2)) {
            sensRotation = 1; // 1 : il faut tourner dans le sens indirect
        } else {
            sensRotation = -1; // -1 : il faut tourner dans le sens direct
        }
    }

    // Dessine une fourmi à la position de la fourmi
    public void dessine(Graphics g, BufferedImage imageFourmi) {
        double r = imageFourmi.getWidth()/2; // Le rayon de la fourmi est égal à la moitié de la hauteur de son image
        if (AFFICHAGE_DIRECTION) {
            g.setColor(Color.BLUE);
            g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*direction.x),(int)(position.y+50*direction.y));
            // g.setColor(Color.GREEN);
            // g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*errance.x),(int)(position.y+50*errance.y));
        }
        g.drawImage(orienterFourmi(imageFourmi), (int)(position.x-r), (int)(position.y-r), null);
    }

    // Fait tourner l'image de fourmi de manière à ce qu'elle soit dirigée dans le sens du vecteur direction
    private BufferedImage orienterFourmi(BufferedImage imageFourmi) {
        BufferedImage img = imageFourmi;
        int largeur = img.getWidth();
        int hauteur = img.getHeight();
        int type = img.getType();

        BufferedImage nouvelleImage = new BufferedImage(largeur, hauteur, type);
        Graphics2D g2 = nouvelleImage.createGraphics();

        // On doit distinguer les cas où l'angle est compris dans [0;pi] ou [-pi;0]
        if (direction.x<=0) {
            g2.rotate(direction.angle(new Vecteur(0,100))+Math.PI, largeur/2, hauteur/2);
        } else {
            g2.rotate(-direction.angle(new Vecteur(0,100))+Math.PI, largeur/2, hauteur/2);
        }
        g2.drawImage(img, null, 0, 0);

        return nouvelleImage;
    }

}
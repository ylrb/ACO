import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Fourmi {

    // Vecteurs propres à la fourmi
    protected Vecteur position;
    protected Vecteur direction;
    protected Vecteur errance;

    // Autres
    protected byte sensRotation; // 0 si pas de rotation, sinon -1 ou 1
    protected ArrayList<Vecteur> contactMurs = new ArrayList<Vecteur>();
    protected static final double DISTANCE_COINS = 15;

    // Tous les coefficients des forces (leur poids)
    protected static final double COEFF_ERRANCE = 0.1;
    protected static final double COEFF_ATTRACTION_PHEROMONES = 1;
    protected static final double COEFF_ATTRACTION_FOURMILIERE_NOURRITURE = 10;
    
    // Grandeurs définies
    protected static final double AMPLITUDE_ERRANCE = 5; // Amplitude max de la variation du vecteur errance
    protected static final double PORTEE_VUE = 50; // Distance à laquelle les fourmis peuvent voir les nourritures, pheromones, la fourmilière etc..
    protected static final double PORTEE_VUE_MUR = 70; // Distance à laquelle les fourmis considère les murs devant elles
    protected static final double ANGLE_VUE = 50; // Angle de vision des fourmis (en degrés)
    protected static final double ANGLE_MIN_MUR = 40; // Angle critique dans le cas des murs (cf. calcul de la force de répulsion) (en degrés)
    protected static final double PONDERATION_TAUX = 10; // Plus cette valeur est grande, moins la pondération des attractions aux phéromones par rapport aux taux est importante
    protected static final double ANGLE_ROTATION = 0.5;

    // Grandeurs liées à l'affichage
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
    public void avancer(LinkedList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheromones, LinkedList<Obstacle> obstacles) {
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
    protected void calculNouvelleDirection(LinkedList<Nourriture> nourritures, Fourmiliere fourmiliere, LinkedList<Pheromone> pheros, LinkedList<Obstacle> obstacles) {
        
        // On ne considère que les éléments qui sont dans le champ de vision de la fourmi (portion de cercle dans sa direction)
        LinkedList<Pheromone> pheromones = pheromonesEnVue(pheros);
        LinkedList<Segment> murs = obstaclesEnVue(obstacles);
        
        // On stocke les murs dans la direction de la fourmi
        LinkedList<Segment> mursProches = mursSecants(murs);

        // Hiérarchie des actions
        if (mursProches.size() > 0) {
            if (sensRotation == 0) {
                angleRotationMur(segmentLePlusProche(mursProches));
            }
            direction.tourner(sensRotation*ANGLE_ROTATION);
            direction = direction.somme(errance, 1, COEFF_ERRANCE);
        } else {
            if (sensRotation != 0) {
                errance = getDirection();
                errance.tourner(sensRotation);
                sensRotation = 0;
            }
            Vecteur forceAttractionSpeciale = calculForceSpeciale(fourmiliere, nourritures);
            if ((forceAttractionSpeciale.x!=0)&&(forceAttractionSpeciale.y!=0)) {           
                direction = direction.somme(forceAttractionSpeciale, 1, COEFF_ATTRACTION_FOURMILIERE_NOURRITURE);
            } else if (pheromones.size() > 0) {
                esquiveCoins(obstacles);
                direction = direction.somme(calculAttractionPheromones(pheromones, murs, false), 1, COEFF_ATTRACTION_PHEROMONES);
            }
            direction = direction.somme(errance, 1, COEFF_ERRANCE);
        }
        direction.unitaire();
    }
        
    // Renvoie une LinkedList contenant les nourritures suffisamment proches de la fourmi pour qu'elle puisse les voir
    protected LinkedList<Pheromone> pheromonesEnVue(LinkedList<Pheromone> pheromones) { 
        LinkedList<Pheromone> rep = new LinkedList<Pheromone>();
        Vecteur distance = new Vecteur();
        for (Pheromone p : pheromones) {
            distance = p.getPosition().soustrait(getPosition());
            if ((position.distance(p.getPosition()) < PORTEE_VUE)&&(direction.angle(distance) < Math.toRadians(ANGLE_VUE))) {
                // Ajoute p à rep si la phéromone se trouve dans le champ de visions de la fourmi
                rep.add(p);
            }
        }
        return rep;
    }

    // Renvoie une LinkedList contenant les murs dans le champ de vision de la fourmi
    protected LinkedList<Segment> obstaclesEnVue(LinkedList<Obstacle> obstacles) { 
        LinkedList<Segment> rep = new LinkedList<Segment>();
        
        Vecteur p2 = new Vecteur(getPosition().x+PORTEE_VUE_MUR*direction.x,getPosition().y+PORTEE_VUE_MUR*direction.y); // Extrémité de la vision de la fourmi
        Segment segmentVue = new Segment(getPosition(),p2); // Segment de la vue de la fourmi
        Segment segmentVueDroite = new Segment(getPosition(),p2); // Segment de l'extrémité droite de la vision de la fourmi
        segmentVueDroite.tourner(ANGLE_VUE);
        Segment segmentVueGauche = new Segment(getPosition(),p2); // Segment de l'extrémité gauche de la vision de la fourmi
        segmentVueDroite.tourner(-ANGLE_VUE);
        
        for (Obstacle o : obstacles) {
            for (Segment s : o.getMurs()) {
                if (segmentVue.secante(s) != null) {
                    rep.add(s);
                } else if ((segmentVueDroite.secante(s) != null)||(segmentVueGauche.secante(s) != null)) {
                    rep.add(s);
                }
            }
        }
        return rep;
    }

    // On détermine dans quels murs va la fourmi
    public LinkedList<Segment> mursSecants(LinkedList<Segment> segments) {
        Vecteur p2 = new Vecteur(getPosition().x+PORTEE_VUE_MUR*direction.x,getPosition().y+PORTEE_VUE_MUR*direction.y); // Extrémité de la vision de la fourmi
        Segment segmentVue = new Segment(getPosition(),p2); // Segment de la vue de la fourmi
        LinkedList<Segment> murs = new LinkedList<Segment>();
        Vecteur pointSecant = new Vecteur();
        contactMurs.clear();
        for (Segment s : segments) {
            pointSecant = segmentVue.secante(s);
            if (pointSecant != null) {
                murs.add(s);
                contactMurs.add(pointSecant);
            }
        }
        return murs;
    }

    // On isole le segment de mur le plus proche de la fourmi
    public Segment segmentLePlusProche(LinkedList<Segment> murs) {
        int min = 0; // Indice du point le plus proche de la fourmi
        int i = 0;
        double distanceMin = 2000; // Distance minimale à la fourmi
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
        Vecteur direction2 = direction.tourner2(0.01);
        Vecteur mur = s.pointA.soustrait(s.pointB);
        double angle1 = mur.angle(direction);
        double angle2 = mur.angle(direction2);
        if (Math.abs(angle1-Math.PI/2) < Math.abs(angle2-Math.PI/2)) {
            sensRotation = 1; // 1 : il faut tourner dans le sens indirect
        } else {
            sensRotation = -1; // -1 : il faut tourner dans le sens direct
        }
    }

    // Les fourmiA (resp. fourmiB) doivent impléter cette méthode, qui correspond à l'attraction à la nourriture (resp. la fourmilière)
    protected abstract Vecteur calculForceSpeciale(Fourmiliere fourmiliere, LinkedList<Nourriture> nourritures);

    // Calcule l'attraction d'une fourmi aux nourritures dans son champ de vision
    protected Vecteur calculAttractionPheromones(LinkedList<Pheromone> pheromones, LinkedList<Segment> murs, boolean initial) {
        Vecteur rep = new Vecteur();
        for (Pheromone p : pheromones) {
            if ((vueDirecte(p, murs))||(initial)) {
                // Augmente rep si la phéromone se trouve dans le champ de vision de la fourmi
                rep = rep.somme(p.getPosition().soustrait(getPosition()),1,p.getTaux()/100+PONDERATION_TAUX);
            }
        }
        rep.unitaire();
        return rep;
    }

    // On vérifie qu'il n'y ait pas de mur entre la fourmi et les phéromones
    public boolean vueDirecte(Pheromone p, LinkedList<Segment> murs) {
        Segment chemin = new Segment(getPosition(), p.position);
        boolean rep = true;
        for (Segment s : murs) {
            if (chemin.secante(s) != null) {
                rep = false;
                break;
            }
        }
        return rep;
    }

    // Les fourmis ne collent pas aux murs en esquivant les coins
    public void esquiveCoins(LinkedList<Obstacle> obstacles) {
        for (Obstacle o : obstacles) {
            for (Segment s : o.getMurs()) {
                if (position.distance(s.pointA) < DISTANCE_COINS) {
                    Vecteur dir = position.soustrait(s.pointA);
                    dir.unitaire();
                    position = position.somme(dir,1,1);
                }
                if (position.distance(s.pointB) < DISTANCE_COINS) {
                    Vecteur dir = position.soustrait(s.pointB);
                    dir.unitaire();
                    position = position.somme(dir,1,1);
                }
            }
        }
    }

    // Dessine une fourmi à la position de la fourmi
    public void dessine(Graphics2D g, BufferedImage imageFourmi) {
        double rayon = imageFourmi.getWidth()/2; // Le rayon de la fourmi est égal à la moitié de la hauteur de son image

        // On doit distinguer les cas où l'angle est compris dans [0;pi] ou [-pi;0]
        double angle =direction.angle(new Vecteur(0,100))+Math.PI;
        if (direction.x>=0) {
            angle = -angle+2*Math.PI;
        }

        // On fait tourner la carte de l'angle désiré, on peint la fourmi, puis on le retourne dans l'autre sens
        g.rotate(angle, (int)position.x, (int)position.y);
        g.drawImage(imageFourmi, (int)(position.x-rayon), (int)(position.y-rayon), null);
        g.rotate(-angle, (int)position.x, (int)position.y);

        // On peut choisir d'afficher le vecteur direction de la fourmi également
        if (AFFICHAGE_DIRECTION) {
            g.setColor(Color.BLUE);
            g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*direction.x),(int)(position.y+50*direction.y));
            g.setColor(Color.GREEN);
            g.drawLine((int)position.x, (int)position.y,(int)(position.x+50*errance.x),(int)(position.y+50*errance.y));
        }
    }
}
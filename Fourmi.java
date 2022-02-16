public class Fourmi {

    private int x;
    private int y;
    private int direction;
    private boolean porteNourriture;
    private double vitesse;

    public Fourmi(int xinit, int yinit) {
        x = xinit;
        y = yinit;
        direction = 8*(int)Math.random(); // Provisoire, à discuter
        porteNourriture = false;
        vitesse = 1.0; // Provisoire, voir ce que ca implique si on le change
    }

}
public class Map extends JFrame implements ActionListener, MouseListener {
    private int width;
    private int height;
    private int dt = 100;
    private Timer timer;

    public Carte() {
        width = 1000;
        height = 700;

    }
    public Carte(int width, int height) {
        this.width = width;
        this.height = height;

        setBounds(100, 50, width, height) ;
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
        setBackground(new Color(43, 37, 20));

        this.addMouseListener(this);
        timer = new Timer(dt, this);
        timer.start();

        repaint();
        
    }

    public void paint (Graphics g) {

        g.setColor(couleurMur);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(couleurSol);
        g.fillRect((int)(borneSup.x),(int)(borneSup.y),(int)(borneInf.x-borneSup.x),(int)(borneInf.y-borneSup.y));

        spawn.draw(g);
        food.draw(g);

        for (int i = 0; i<nombreFourmis; i++) {
            g.setColor(Color.ORANGE);
            fifi[i].draw(g,true);
        }

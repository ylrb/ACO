import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Son {
    private Clip clip;
    public Son(String nom) {
        try {
            File file = new File("assets/sons/" + nom + ".wav");
            if (file.exists()) {
                Clip clip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(file.toURI().toURL());
                clip.open(ais);
            }
            else {
                throw new RuntimeException("Son: fichier non trouve: " + nom);
            }
        }
        catch (Exception e) {
            System.out.println("Erreur son : " + e);
        }
    }
    public void play(){
        clip.setFramePosition(0);  // Rejouer le son du debut
        clip.loop(0);
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
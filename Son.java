import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Son {
    private Clip myClip;
    public Son(String fileName) {
        try {
            File file = new File("assets/sons/" + fileName + ".wav");
            if (file.exists()) {
                Clip myClip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(file.toURI().toURL());
                myClip.open(ais);
            }
            else {
                throw new RuntimeException("Sound: file not found: " + fileName);
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Sound: Malformed URL: " + e);
        }
        catch (UnsupportedAudioFileException e) {
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            throw new RuntimeException("Sound: Line Unavailable: " + e);
        }
    }
    public void play(){
        myClip.setFramePosition(0);  // Must always rewind!
        myClip.loop(0);
        myClip.start();
    }
    public void loop(){
        myClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        myClip.stop();
    }
}
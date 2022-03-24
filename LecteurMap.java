import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.FileReader;

public class LecteurMap {
    public LecteurMap(String chemin){
        try {
        Scanner scan = new Scanner(new FileReader(chemin));
        StringBuilder sb = new StringBuilder();
        while (scan.hasNextLine()) {
            sb.append(scan.next());
            System.out.println(scan.next());
        }
        scan.close();
        } catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
        }
    }
}

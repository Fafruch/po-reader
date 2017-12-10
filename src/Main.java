import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            Node node = new Node(0, "", null);
            ArrayList<String> storedFile = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                storedFile.add(line);
            }

            Normalizer normalizer = new Normalizer();
            storedFile = normalizer.cleanFile(storedFile);
            storedFile = normalizer.connectLines(storedFile);

            Parser parser = new Parser(storedFile);

            Node parsedNode = parser.parseToNodes(node);

        } catch(IOException ex) {
            System.out.println("Podales zla sciezke!");
            System.out.println(ex);
        }
    }
}

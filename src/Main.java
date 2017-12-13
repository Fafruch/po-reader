import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if(args.length < 2) {
            throw new Error("You passed too few arugments!");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            Node emptyDataTree = new Node(0, "", null);
            ArrayList<String> storedFile = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                storedFile.add(line);
            }

            Normalizer normalizer = new Normalizer();
            storedFile = normalizer.cleanFile(storedFile);
            storedFile = normalizer.connectLines(storedFile);

            Parser parser = new Parser(storedFile);
            Node dataTree = parser.parseToTree(emptyDataTree);

            Printer printer = new Printer(args);
            printer.print(dataTree);

        } catch(IOException | Error ex) {
            if(ex instanceof IOException) {
                System.out.println("Podales zla sciezke!");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
}

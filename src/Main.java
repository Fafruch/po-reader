import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            if(args.length < 2) {
                throw new Error("You passed too few arguments!");
            } else if (args.length < 3 && args[1].equals("-e")) {
                throw new Error("You passed too few arguments for -e option!");
            }

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
                System.out.println("Wrong path! Could not open file.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
}

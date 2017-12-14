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

            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            storedFile = uokikNormalizer.cleanFile(storedFile);
            storedFile = uokikNormalizer.moveUstepsToNewLine(storedFile);
            storedFile = uokikNormalizer.connectLines(storedFile);

            for(String lineToPrint : storedFile) {
                System.out.println(lineToPrint);
            }

            UokikParser uokikParser = new UokikParser(storedFile);
            Node dataTree = uokikParser.parseToTree(emptyDataTree);

             for(int i = 0; i < storedFile.size(); i++) {
                System.out.println(i + "-> " + storedFile.get(i));
            }
            /*KonstNormalizer konstNormalizer = new KonstNormalizer();
            storedFile = konstNormalizer.cleanFile(storedFile);
            storedFile = konstNormalizer.connectLines(storedFile);

            KonstParser konstParser = new KonstParser(storedFile);
            Node dataTree = konstParser.parseToTree(emptyDataTree);

            KonstPrinter konstPrinter = new KonstPrinter(args);
            konstPrinter.print(dataTree);*/

        } catch(IOException | Error ex) {
            if(ex instanceof IOException) {
                System.out.println("Wrong path! Could not open file.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
}

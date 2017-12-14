import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            if (args.length < 2) {
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

            if (args[0].equals("konstytucja.txt")) {
                KonstNormalizer konstNormalizer = new KonstNormalizer();
                storedFile = konstNormalizer.cleanFile(storedFile);
                storedFile = konstNormalizer.connectLines(storedFile);

                KonstParser konstParser = new KonstParser(storedFile);
                Node dataTree = konstParser.parseToTree(emptyDataTree);

                KonstPrinter konstPrinter = new KonstPrinter(args);
                konstPrinter.print(dataTree);
            } else if (args[0].equals("uokik.txt")) {
                UokikNormalizer uokikNormalizer = new UokikNormalizer();
                storedFile = uokikNormalizer.cleanFile(storedFile);
                storedFile = uokikNormalizer.moveUstepsToNewLine(storedFile);
                storedFile = uokikNormalizer.connectLines(storedFile);

                UokikParser uokikParser = new UokikParser(storedFile);
                Node dataTree = uokikParser.parseToTree(emptyDataTree);

                UokikPrinter uokikPrinter = new UokikPrinter(args);
                uokikPrinter.print(dataTree);
            } else {
                throw new Error("I can't parse this file. Sorry. : (");
            }

        } catch (IOException | Error ex) {
            if (ex instanceof IOException) {
                System.out.println("Wrong path! Could not open file.");
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }
}

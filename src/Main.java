import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

            checkArgs(args);

            ArrayList<String> file = convertFileToArrayList(br);

            runAppWith(args, file);

        } catch (FileNotFoundException e) {
            System.out.println("Wrong path: " + args[0] + ". Could not find file.");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static private void checkArgs(String args[]) {
        if (args.length < 2) {
            throw new IllegalArgumentException("You passed too few arguments!");
        } else if (args.length < 3 && args[1].equals("-e")) {
            throw new IllegalArgumentException("You passed too few arguments for -e option!");
        } else if (!(args[0].equals("konstytucja.txt") || args[0].equals("uokik.txt"))) {
            throw new IllegalArgumentException("I'm not build for this file. Sorry. : (");
        }
    }

    static private ArrayList<String> convertFileToArrayList(BufferedReader br) throws IOException {
        ArrayList<String> storedFile = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            storedFile.add(line);
        }

        return storedFile;
    }

    static private void runAppWith(String args[], ArrayList<String> storedFile) throws NotFoundException, IllegalArgumentException {
        Node emptyDataTree = new Node(0, "");
        String filename = args[0];

        if (filename.equals("konstytucja.txt")) {
            // normalize file for easier tree build
            KonstNormalizer konstNormalizer = new KonstNormalizer();
            storedFile = konstNormalizer.cleanFile(storedFile);
            storedFile = konstNormalizer.connectLines(storedFile);

            // convert list of lines to tree of lines
            KonstParser konstParser = new KonstParser(storedFile);
            Node dataTree = konstParser.parseToTree(emptyDataTree);

            // print lines based on user's input
            KonstPrinter konstPrinter = new KonstPrinter(args);
            konstPrinter.print(dataTree);
        } else if (filename.equals("uokik.txt")) {
            // normalize file for easier tree build
            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            storedFile = uokikNormalizer.cleanFile(storedFile);
            storedFile = uokikNormalizer.moveUstepsToNewLine(storedFile);
            storedFile = uokikNormalizer.connectLines(storedFile);

            // convert list of lines to tree of lines
            UokikParser uokikParser = new UokikParser(storedFile);
            Node dataTree = uokikParser.parseToTree(emptyDataTree);

            // print lines based on user's input
            UokikPrinter uokikPrinter = new UokikPrinter(args);
            uokikPrinter.print(dataTree);
        }
    }
}

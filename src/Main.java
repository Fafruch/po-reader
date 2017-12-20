import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {

            checkArgs(args);

            List<String> file = convertFileToList(br);

            runAppWith(args, file);

        } catch (FileNotFoundException e) {
            System.out.println("Wrong path: " + args[0] + ". Could not find file.");
        } catch (NotFoundException | IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static private void checkArgs(String args[]) {
        if (args.length < 2) {
            throw new IllegalArgumentException("You passed too few arguments! After file path provide '-t' for table of contents, '-a' for printing whole file or '-e' for particular element. ");

        } else if (!args[0].equals("konstytucja.txt") && !args[0].equals("uokik.txt")) {
            throw new IllegalArgumentException("I'm not build for this file. Sorry. : (");

        } else if (!args[1].equals("-t") && !args[1].equals("-a") && !args[1].equals("-e")) {
            throw new IllegalArgumentException("You've provided not a valid mode. Use '-t' for table of contents, '-a' for printing whole file or '-e' for particular element.");
        }
    }

    static private List<String> convertFileToList(BufferedReader br) throws IOException {
        List<String> storedFile = new LinkedList<>();
        String line;

        while ((line = br.readLine()) != null) {
            storedFile.add(line);
        }

        return storedFile;
    }

    static private void runAppWith(String args[], List<String> storedFile) throws NotFoundException, IllegalArgumentException {
        Node emptyDataTree = new Node(0, "");
        String filename = args[0];

        if (filename.equals("konstytucja.txt")) {
            // normalize file for easier tree build
            KonstNormalizer konstNormalizer = new KonstNormalizer();
            List<String> normalizedFile = konstNormalizer.normalize(storedFile);

            // convert list of lines to tree of lines
            KonstParser konstParser = new KonstParser(normalizedFile);
            Node dataTree = konstParser.parseToTree(emptyDataTree);

            // print lines based on user's input
            KonstPrinter konstPrinter = new KonstPrinter(args);
            konstPrinter.print(dataTree);

        } else if (filename.equals("uokik.txt")) {
            // normalize file for easier tree build
            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            List<String> normalizedFile = uokikNormalizer.normalize(storedFile);

            // convert list of lines to tree of lines
            UokikParser uokikParser = new UokikParser(normalizedFile);
            Node dataTree = uokikParser.parseToTree(emptyDataTree);

            // print lines based on user's input
            UokikPrinter uokikPrinter = new UokikPrinter(args);
            uokikPrinter.print(dataTree);
        }
    }
}

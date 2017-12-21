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
            System.out.println("Podano złą ścieżkę '" + args[0] + "'. Plik nie został odnaleziony.");
        } catch (NotFoundException | IllegalArgumentException | NotSupportedFileException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static private void checkArgs(String args[]) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Podano za mało argumentów. Jako drugi argument podaj: \n- '-t' dla spisu treści, \n- '-a' dla wypisania całego sformatowanego pliku \n- '-e' dla wybrania konkretnego elementu.");

        } else if (!args[1].equals("-t") && !args[1].equals("-a") && !args[1].equals("-e")) {
            throw new IllegalArgumentException("Podano zły tryb. Jako drugi argument podaj: \n - '-t' dla spisu streści, \n - '-a' dla wypisania całego sformatowanego pliku \n - '-e' dla wybrania konkretnego elementu.");

        } else if (args.length < 3 && args[1].equals("-e")) {
            throw new IllegalArgumentException("Nie podano elementu do wypisania jako trzeci argument. Spróbuj np. 'Art. 123, ust. 2., pkt 3)'.");
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

    static private void runAppWith(String args[], List<String> file)
            throws NotFoundException, IllegalArgumentException, NotSupportedFileException {
        Node emptyDataTree = new Node(0, "");
        String controlLine = file.get(0);

        // konstytucja.txt
        if (controlLine.matches(KonstPattern.IS_KONSTYTUCJA)) {
            // normalize file for easier tree build
            KonstNormalizer konstNormalizer = new KonstNormalizer();
            List<String> normalizedFile = konstNormalizer.normalize(file);

            // convert list of lines to tree of lines
            KonstParser konstParser = new KonstParser(normalizedFile);
            Node dataTree = konstParser.parseToTree(emptyDataTree);

            // print lines based on user's input
            KonstPrinter konstPrinter = new KonstPrinter(args);
            konstPrinter.print(dataTree);

        // uokik.txt
        } else if (controlLine.matches(UokikPattern.IS_UOKIK)) {
            // normalize file for easier tree build
            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            List<String> normalizedFile = uokikNormalizer.normalize(file);

            // convert list of lines to tree of lines
            UokikParser uokikParser = new UokikParser(normalizedFile);
            Node dataTree = uokikParser.parseToTree(emptyDataTree);

            // print lines based on user's input
            UokikPrinter uokikPrinter = new UokikPrinter(args);
            uokikPrinter.print(dataTree);
        } else {
            throw new NotSupportedFileException("Nie jestem stworzony dla tego pliku. : (");
        }
    }
}

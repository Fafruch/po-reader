public class UokikPrinter extends AbstractPrinter {
    private String normalizedConfig;

    public UokikPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        UokikNormalizer uokikNormalizer = new UokikNormalizer();
        normalizedConfig = uokikNormalizer.normalizeString(config);

        // System.out.println(normalizedConfig);

        if(normalizedConfig.matches('^' + Pattern.DZIAL + '$')) {
            // np. Dział IIIA
            printDzial();

        } else if(normalizedConfig.matches('^' + Pattern.DZIAL + ',' + Pattern.ROZDZIAL + '$')) {
            // np. Dział IIIA, rozdział 2
            printRozdzial();

        } else if(normalizedConfig.matches('^' + Pattern.ARTYKUL + '$')) {
            // np. Art. 119j
            printArtykul();

        } else if(normalizedConfig.matches('^' + Pattern.ZAKRES_ARTYKULOW + '$')) {
            // np. Art. 119j-121b
            printArtykuly();

        } else if(normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + '$')) {
            // np. Art. 30a, ust. 2a.
            printUstep();
        } else if(normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + '$')) {
            // np. Art. 30a, ust. 2a., pkt 1a)
            printPunkt();

        } else if(normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + ',' + Pattern.LITERA + '$')) {
            // np. Art. 30a, ust. 2a., pkt 1a), lit. b)
            printLitera();
        } else {
            throw new IllegalArgumentException("Bad input format for elements configuration! Config '" + config + "' is not valid.");
        }
    }

    private void printDzial() throws NotFoundException {
        String dzial = normalizedConfig;

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if(dzialNode == null) {
            throw new NotFoundException("Nie ma takiego dzialu!");
        }

        printNodeAndItsChildren(dzialNode);
    }

    private void printRozdzial() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String dzial = configSplit[0]; // np. art133
        String rozdzial = configSplit[1]; // np. art245

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if(dzialNode == null) {
            throw new NotFoundException("Nie ma takiego dzialu!");
        }

        Node rozdzialNode = findNodeAtDepth(dzialNode, rozdzial, 2);

        if(rozdzialNode == null) {
            throw new NotFoundException("Nie ma takiego rozdzialu!");
        }

        printNodeAndItsChildren(rozdzialNode);
    }

    private void printArtykul() throws NotFoundException {
        String artykul = normalizedConfig;

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if(artykulNode == null) {
            throw new NotFoundException("Nie ma takiego artykulu!");
        }

        printNodeAndItsChildren(artykulNode);
    }

    private void printArtykuly() throws NotFoundException, IllegalArgumentException {
        String[] configSplit = normalizedConfig.split("-");

        String firstArtykul = configSplit[0]; // np. art133
        String lastArtykul = "art" + configSplit[1]; // np. art245

        if(firstArtykul.compareTo(lastArtykul) > 0) {
            throw new IllegalArgumentException("Niepoprawny zakres artykulow!");
        }

        UokikNormalizer uokikNormalizer = new UokikNormalizer();
        String normalizedData;
        boolean wasPrinting = false;

        for(int i = 0; i < Node.getArtykuly().size(); i++) {
            Node currentNode = Node.getArtykuly().get(i);
            String data = currentNode.getData();

            normalizedData = uokikNormalizer.normalizeString(data);

            // extract only numerical values from data
            int firstArtykulIndex = Integer.parseInt(firstArtykul.replaceAll("[a-zA-Z]",""));
            int currentArtykulIndex = Integer.parseInt(normalizedData.replaceAll("[a-zA-Z]",""));
            int lastArtykulIndex = Integer.parseInt(lastArtykul.replaceAll("[a-zA-Z]",""));

            boolean isNumericalBetween = firstArtykulIndex <= currentArtykulIndex && currentArtykulIndex <= lastArtykulIndex;
            boolean isAlphabeticalBetween = firstArtykul.compareTo(normalizedData) <= 0 && normalizedData.compareTo(lastArtykul) <= 0;

            if(isNumericalBetween && isAlphabeticalBetween) {
                wasPrinting = true;
                printNodeAndItsChildren(currentNode);
            }
        }

        if(!wasPrinting) {
            throw new NotFoundException("Nie ma takich artykulow!");
        }
    }

    private void printUstep() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String artykul = configSplit[0]; // np. art133
        String ustep = configSplit[1].substring(3); // np. 3a

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if(artykulNode == null) {
            throw new NotFoundException("Nie znaleziono takiego artykulu!");
        }

        Node ustepNode = findNodeAtDepth(artykulNode, ustep, 4);

        if(ustepNode == null) {
            throw new NotFoundException("Nie znaleziono takiego ustepu!");
        }

        printNodeAndItsChildren(ustepNode);
    }

    private void printPunkt() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String artykul = configSplit[0]; // np. art133
        String ustep = configSplit[1].substring(3); // np. 3a
        String punkt = configSplit[2].substring(3); // np. 10b

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if(artykulNode == null) {
            throw new NotFoundException("Nie znaleziono takiego artykulu!");
        }

        Node ustepNode = findNodeAtDepth(artykulNode, ustep, 4);

        if(ustepNode == null) {
            throw new NotFoundException("Nie znaleziono takiego ustepu!");
        }

        Node punktNode = findNodeAtDepth(ustepNode, punkt, 5);

        if(punktNode == null) {
            throw new NotFoundException("Nie znaleziono takiego punktu!");
        }

        printNodeAndItsChildren(punktNode);
    }

    private void printLitera() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String artykul = configSplit[0]; // np. art133
        String ustep = configSplit[1].substring(3); // np. 3a
        String punkt = configSplit[2].substring(3); // np. 10b
        String litera = configSplit[3].substring(3); // np. a

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if (artykulNode == null) {
            throw new NotFoundException("Nie znaleziono takiego artykulu!");
        }

        Node ustepNode = findNodeAtDepth(artykulNode, ustep, 4);

        if (ustepNode == null) {
            throw new NotFoundException("Nie znaleziono takiego ustepu!");
        }

        Node punktNode = findNodeAtDepth(ustepNode, punkt, 5);

        if (punktNode == null) {
            throw new NotFoundException("Nie znaleziono takiego punktu!");
        }

        Node literaNode = findNodeAtDepth(punktNode, litera, 6);

        if (literaNode == null) {
            throw new NotFoundException("Nie znaleziono takiej litery!");
        }

        printNodeAndItsChildren(literaNode);
    }


    private Node findNodeAtDepth(Node root, String name, int depth) {
        if(root.getDepth() == depth) {
            String data = root.getData();

            /*System.out.println();

            System.out.println("NAME: " + name);
            System.out.println("BEFORE NORMALIZING: " + data);*/

            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            String normalizedData = uokikNormalizer.normalizeString(data);

            // keep only the chars that we want to check equality of
            if(normalizedData.length() > name.length()) {
                normalizedData = normalizedData.substring(0, name.length());
            }

            /*System.out.println("NORMALIZED: " + normalizedData);

            System.out.println();*/

            /*System.out.println(name.length());
            System.out.println(normalizedData.length());*/

            if(normalizedData.matches(name)) {
                return root;
            } else {
                return null;
            }
        }

        for(Node child : root.getChildren()) {
            Node result = findNodeAtDepth(child, name, depth);

            if(result != null) {
                return result;
            }
        }

        return null;
    }
}

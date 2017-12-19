public class UokikPrinter extends AbstractPrinter {
    public UokikPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        UokikNormalizer uokikNormalizer = new UokikNormalizer();
        config = uokikNormalizer.normalizeString(config);

        if(config.matches('^' + Pattern.DZIAL + '$')) {
            // np. Dział IIIA
            printDzial();

        } else if(config.matches('^' + Pattern.DZIAL + ',' + Pattern.ROZDZIAL + '$')) {
            // np. Dział IIIA, rozdział 2
            printRozdzial();

        } else if(config.matches('^' + Pattern.ARTYKUL + '$')) {
            // np. Art. 119j
            printArtykul();

        } else if(config.matches('^' + Pattern.ZAKRES_ARTYKULOW + '$')) {
            // np. Art. 119j-121b
            printArtykuly();

        } else if(config.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + '$')) {
            // np. Art. 30a, ust. 2a.
            printUstep();
        } else if(config.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + '$')) {
            // np. Art. 30a, ust. 2a., pkt 1a)
            printPunkt();

        } else if(config.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + ',' + Pattern.LITERA + '$')) {
            // np. Art. 30a, ust. 2a., pkt 1a), lit. b)
            printLitera();
        }
    }

    private void printDzial() throws NotFoundException {
        // np. Dział IIIA
        int indexOfComma = config.indexOf(',');
        String dzial = config.substring(0, indexOfComma);

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if(dzialNode == null) {
            throw new NotFoundException("Nie ma takiego dzialu!");
        }

        printNodeAndItsChildren(dzialNode);
    }

    private void printRozdzial() throws NotFoundException {
        int indexOfComma = config.indexOf(',');
        String dzial = config.substring(0, indexOfComma);
        String rozdzial = config.substring(indexOfComma + 1);

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
        String artykul = config;

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if(artykulNode == null) {
            throw new NotFoundException("Nie ma takiego artykulu!");
        }

        printNodeAndItsChildren(artykulNode);
    }

    private void printArtykuly() throws NotFoundException, IllegalArgumentException {
        int indexOfDash = config.indexOf('-');

        String firstArtykul = config.substring(0, indexOfDash);
        String lastArtykul = "art." + config.substring(indexOfDash + 1);

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
            normalizedData = uokikNormalizer.removeLastChar(normalizedData);

            if(firstArtykul.compareTo(normalizedData) <= 0
                    && normalizedData.compareTo(lastArtykul) <= 0
                    && normalizedData.length() >= firstArtykul.length()
                    && normalizedData.length() >= lastArtykul.length()) {

                wasPrinting = true;
                printNodeAndItsChildren(currentNode);
            }
        }

        if(!wasPrinting) {
            throw new NotFoundException("Nie ma takich artykulow!");
        }
    }

    private void printUstep() throws NotFoundException {
        int indexOfComma = config.indexOf(',');

        String artykul = config.substring(0, indexOfComma);
        String ustep = config.substring(indexOfComma + 5);

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
        int firstIndexOfComma = config.indexOf(',');
        int lastIndexOfComma = config.lastIndexOf(',');
        int indexOfParenthesis = config.lastIndexOf(')');

        String artykul = config.substring(0, firstIndexOfComma);
        String ustep = config.substring(firstIndexOfComma + 5, lastIndexOfComma);
        String punkt = config.substring(lastIndexOfComma + 4, indexOfParenthesis);

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
        int firstIndexOfComma = config.indexOf(',');
        int secondIndexOfComma = firstIndexOfComma + config.substring(firstIndexOfComma + 1).indexOf(',');
        int firstIndexOfParenthesis = config.indexOf(')');
        int lastIndexOfParenthesis = config.lastIndexOf(')');

        String artykul = config.substring(0, firstIndexOfComma);
        String ustep = config.substring(firstIndexOfComma + 5, secondIndexOfComma);
        String punkt = config.substring(secondIndexOfComma + 5, firstIndexOfParenthesis);
        String litera = config.substring(firstIndexOfParenthesis + 6, lastIndexOfParenthesis);

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

            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            String normalizedData = uokikNormalizer.normalizeString(data);

            // System.out.println(normalizedData);

            if(normalizedData.matches(name + "(.)*$")) {
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

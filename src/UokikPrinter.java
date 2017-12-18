public class UokikPrinter extends AbstractPrinter {
    public UokikPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        KonstNormalizer konstNormalizer = new KonstNormalizer();
        config = konstNormalizer.normalizeString(config);

        // np. Dział IIIA
        if(config.matches("^dzia[łl]\\w{1,4}$")) {
            int indexOfComma = config.indexOf(',');
            String dzial = config.substring(0, indexOfComma);

            Node dzialNode = findNodeAtDepth(root, dzial, 1);

            if(dzialNode == null) {
                throw new NotFoundException("Nie ma takiego dzialu!");
            }

            printNodeAndItsChildren(dzialNode);

            // np. Dział IIIA, rozdział 2
        } else if(config.matches("^dzia[łl]\\w{1,4},rozdzia[łl]\\d+$")) {
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

            // np. Art. 119j
        } else if(config.matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?$")) {
            String artykul = config;

            Node artykulNode = findNodeAtDepth(root, artykul, 3);

            if(artykulNode == null) {
                throw new NotFoundException("Nie ma takiego artykulu!");
            }

            printNodeAndItsChildren(artykulNode);

            // np. Art. 119j-121b
        } else if(config.matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?-\\d{1,3}[a-z]{0,3}(\\.)?$")) {
            int indexOfDash = config.indexOf('-');

            String firstArtykul = config.substring(0, indexOfDash);
            String lastArtykul = "art." + config.substring(indexOfDash + 1);

            /*System.out.println(firstArtykul);
            System.out.println(lastArtykul);*/

            if(firstArtykul.compareTo(lastArtykul) > 0) {
                throw new IllegalArgumentException("Niepoprawny zakres artykulow!");
            }

            printArtykulyBetween(firstArtykul, lastArtykul);

            // np. Art. 30a, ust. 2a.
        } else if(config.matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?,ust\\.\\d{1,3}[a-z]{0,3}\\.$")) {
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

            // np. Art. 30a, ust. 2a., pkt 1a)
        } else if(config.matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?,ust\\.\\d{1,3}[a-z]{0,3}\\.,pkt\\d{1,3}[a-z]{0,3}\\)$")) {
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

            // np. Art. 30a, ust. 2a., pkt 1a), lit. b)
        } else if(config.matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?,ust\\.\\d{1,3}[a-z]{0,3}\\.,pkt\\d{1,3}[a-z]{0,3}\\),lit.[a-z]{1,3}\\)$")) {
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
    }

    private void printArtykulyBetween(String firstArtykulName, String lastArtykulName) {
        UokikNormalizer uokikNormalizer = new UokikNormalizer();
        String normalizedData;

        for(int i = 0; i < Node.getArtykuly().size(); i++) {
            Node currentNode = Node.getArtykuly().get(i);
            String data = currentNode.getData();

            normalizedData = uokikNormalizer.normalizeString(data);
            normalizedData = uokikNormalizer.removeLastChar(normalizedData);

            /*System.out.println("");
            System.out.println("1. " + firstArtykulName + ", length: " + firstArtykulName.length());
            System.out.println("2. " + (firstArtykulName.compareTo(normalizedData) <= 0));
            System.out.println("3. " + normalizedData + ", length: " + normalizedData.length());
            System.out.println("4. " + (normalizedData.compareTo(lastArtykulName) <= 0));
            System.out.println("5. " + lastArtykulName + ", length: " + lastArtykulName.length());
            System.out.println("");*/

            if(firstArtykulName.compareTo(normalizedData) <= 0
                    && normalizedData.compareTo(lastArtykulName) <= 0
                    && normalizedData.length() >= firstArtykulName.length()
                    && normalizedData.length() >= lastArtykulName.length()) {

                printNodeAndItsChildren(currentNode);
            }
        }
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

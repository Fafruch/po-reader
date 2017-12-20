abstract public class AbstractPrinter {
    private String mode;
    private String config;
    protected String normalizedConfig;
    protected Node root;
    protected boolean wasPrinting = false;

    public AbstractPrinter(String[] args) {
        this.mode = args[1];

        if(args.length == 3) {
            this.config = args[2];
        }
    }

    public void print(Node root) throws NotFoundException, IllegalArgumentException {
        this.root = root;

        if(mode.equals("-t")) {
            printTableOfContents(root, 0);
        } else {
            printElements();

            if(!wasPrinting) {
                throw new IllegalArgumentException("Bad input format for elements configuration! Config '" + config + "' is not valid.");
            }
        }
    }

    protected void printNodeAndItsChildren(Node node) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printNodeAndItsChildren(childrenNode);
        }
    }

    protected Node findNodeAtDepth(Node root, String name, int depth) {
        if(root.getDepth() == depth) {
            String data = root.getData();

            Normalizer normalizer = new Normalizer();
            String normalizedData = normalizer.normalizeString(data);

            // keep only the chars that we want to check equality of
            if(normalizedData.length() > name.length()) {
                normalizedData = normalizedData.substring(0, name.length());
            }

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

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        Normalizer normalizer = new Normalizer();
        normalizedConfig = normalizer.normalizeString(config);

        if (normalizedConfig.matches('^' + Pattern.ARTYKUL + '$')) {
            // np. Art. 119j
            printArtykul();

        } else if (normalizedConfig.matches('^' + Pattern.ZAKRES_ARTYKULOW + '$')) {
            // np. Art. 119j-121b
            printArtykuly();

        } else if (normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + '$')) {
            // np. Art. 30a, ust. 2a.
            printUstep();
        } else if (normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + '$')) {
            // np. Art. 30a, ust. 2a., pkt 1a)
            printPunkt();

        } else if (normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + ',' + Pattern.LITERA + '$')) {
            // np. Art. 30a, ust. 2a., pkt 1a), lit. b)
            printLitera();
        }
    }

    private void printArtykul() throws NotFoundException {
        String artykul = normalizedConfig;

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if(artykulNode == null) {
            throw new NotFoundException("Nie ma takiego artykulu!");
        }

        printNodeAndItsChildren(artykulNode);
        wasPrinting = true;
    }

    private void printArtykuly() throws NotFoundException, IllegalArgumentException {
        String[] configSplit = normalizedConfig.split("-");

        String firstArtykul = configSplit[0]; // np. art133
        String lastArtykul = "art" + configSplit[1]; // np. art245

        if(firstArtykul.compareTo(lastArtykul) > 0) {
            throw new IllegalArgumentException("Niepoprawny zakres artykulow!");
        }

        Normalizer normalizer = new Normalizer();
        String normalizedData;
        boolean foundArticles = false;

        for(int i = 0; i < Node.getArtykuly().size(); i++) {
            Node currentNode = Node.getArtykuly().get(i);
            String data = currentNode.getData();
            normalizedData = normalizer.normalizeString(data);

            // extract only numerical values from data
            int firstArtykulIndex = Integer.parseInt(firstArtykul.replaceAll("[a-zA-Z]",""));
            int currentArtykulIndex = Integer.parseInt(normalizedData.replaceAll("[a-zA-Z]",""));
            int lastArtykulIndex = Integer.parseInt(lastArtykul.replaceAll("[a-zA-Z]",""));

            boolean isNumericalBetween = firstArtykulIndex <= currentArtykulIndex && currentArtykulIndex <= lastArtykulIndex;
            boolean isAlphabeticalBetween = firstArtykul.compareTo(normalizedData) <= 0 && normalizedData.compareTo(lastArtykul) <= 0;

            if(isNumericalBetween && isAlphabeticalBetween) {
                printNodeAndItsChildren(currentNode);
                foundArticles = true;
            }
        }

        if(!foundArticles) {
            throw new NotFoundException("Nie ma takich artykulow!");
        }

        wasPrinting = true;
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
        wasPrinting = true;
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
        wasPrinting = true;
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
        wasPrinting = true;
    }

    private void printTableOfContents(Node node, int index) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        if(node.getDepth() == 4) {
            System.out.println("Ustep " + (index + 1) + ".");
        } else if (node.getDepth() == 5) {
            System.out.println("Punkt " + (index + 1) + ")");
        } else if (node.getDepth() == 6) {
            char litera = (char) (index + 97);
            System.out.println("Litera " + litera + ")");
        } else {
            System.out.println(node.getData());
        }

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printTableOfContents(childrenNode, i);
        }
        wasPrinting = true;
    }
}

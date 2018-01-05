abstract public class AbstractPrinter {
    private String mode;
    private String config = "";
    protected String normalizedConfig;
    protected Node root;
    protected boolean wasPrinting = false;

    public AbstractPrinter(String[] args) {
        this.mode = args[1];

        if (args.length == 3) {
            this.config = args[2];
        }
    }

    public void print(Node root) throws NotFoundException, IllegalArgumentException {
        this.root = root;

        if (mode.equals("-t")) {
            printTableOfContents();

        } else if (mode.equals("-a")) {
            printAll();

        } else {
            printElements();

            if (!wasPrinting) {
                throw new IllegalArgumentException("Zła konfiguracja dla opcji '-e'. Konfiguracja '" + config + "' nie jest poprawna.");
            }
        }
    }

    protected void printNodeAndItsChildren(Node node) {
        for (int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for (int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printNodeAndItsChildren(childrenNode);
        }
    }

    protected Node findNodeAtDepth(Node root, String key, int depth) {
        if (root.getDepth() == depth) {
            String data = root.getData();

            // only for usteps without explicit index on start
            int keyIndex = -1;
            if(root.getDepth() == 4) {
                keyIndex = Integer.parseInt(key.replaceAll("[a-zA-Z]", ""));
            }

            Normalizer normalizer = new Normalizer();
            String normalizedData = normalizer.normalizeString(data);

            // keep only the chars that we want to check equality of
            if (normalizedData.length() > key.length()) {
                normalizedData = normalizedData.substring(0, key.length());
            }

            // if ustep has no index and we seek 'ustęp 1', the first is true, else check if element keys are the same
            if (keyIndex == 1 || normalizedData.matches(key)) {
                return root;
            } else {
                return null;
            }
        }

        for (Node child : root.getChildren()) {
            Node result = findNodeAtDepth(child, key, depth);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        Normalizer normalizer = new Normalizer();
        normalizedConfig = normalizer.normalizeString(config);

        if (normalizedConfig.matches('^' + Pattern.ARTYKUL + '$')) {
            // e.g. Art. 119j
            printArtykul();

        } else if (normalizedConfig.matches('^' + Pattern.ZAKRES_ARTYKULOW + '$')) {
            // e.g. Art. 119j-121b
            printArtykuly();

        } else if (normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + '$')) {
            // e.g. Art. 30a, ust. 2a.
            printUstep();
        } else if (normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + '$')) {
            // e.g. Art. 30a, ust. 2a., pkt 1a)
            printPunkt();

        } else if (normalizedConfig.matches('^' + Pattern.ARTYKUL + ',' + Pattern.USTEP + ',' + Pattern.PUNKT + ',' + Pattern.LITERA + '$')) {
            // e.g. Art. 30a, ust. 2a., pkt 1a), lit. b)
            printLitera();
        }
    }

    // e.g. Art. 119j
    private void printArtykul() throws NotFoundException {
        String artykul = normalizedConfig;

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if (artykulNode == null) {
            throw new NotFoundException("Nie ma takiego artykułu!");
        }

        printNodeAndItsChildren(artykulNode);
        wasPrinting = true;
    }

    // e.g. Art. 119j-121b
    private void printArtykuly() throws NotFoundException, IllegalArgumentException {
        String[] configSplit = normalizedConfig.split("-");

        String firstArtykul = configSplit[0]; // e.g. art133
        String lastArtykul = "art" + configSplit[1]; // e.g. art245

        if (firstArtykul.compareTo(lastArtykul) > 0) {
            throw new IllegalArgumentException("Niepoprawny zakres artykułów!");
        }

        Normalizer normalizer = new Normalizer();
        String normalizedData;
        boolean foundArticles = false;

        for (int i = 0; i < root.getArticleList().size(); i++) {
            Node currentNode = root.getArticleList().get(i);
            String data = currentNode.getData();
            normalizedData = normalizer.normalizeString(data);

            // extract only numerical values from data
            int firstArtykulIndex = Integer.parseInt(firstArtykul.replaceAll("[a-zA-Z]", ""));
            int currentArtykulIndex = Integer.parseInt(normalizedData.replaceAll("[a-zA-Z]", ""));
            int lastArtykulIndex = Integer.parseInt(lastArtykul.replaceAll("[a-zA-Z]", ""));

            boolean isNumericalBetween = firstArtykulIndex <= currentArtykulIndex && currentArtykulIndex <= lastArtykulIndex;
            boolean isAlphabeticalBetween = firstArtykul.compareTo(normalizedData) <= 0 && normalizedData.compareTo(lastArtykul) <= 0;

            if (isNumericalBetween && isAlphabeticalBetween) {
                printNodeAndItsChildren(currentNode);
                foundArticles = true;
            }
        }

        if (!foundArticles) {
            throw new NotFoundException("Nie ma takich artykułów!");
        }

        wasPrinting = true;
    }

    // e.g. Art. 30a, ust. 2a.
    private void printUstep() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String artykul = configSplit[0]; // e.g. art133
        String ustep = configSplit[1].substring(3); // e.g. 3a

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if (artykulNode == null) {
            throw new NotFoundException("Nie znaleziono takiego artykułu!");
        }

        Node ustepNode = findNodeAtDepth(artykulNode, ustep, 4);

        if (ustepNode == null) {
            throw new NotFoundException("Nie znaleziono takiego ustępu!");
        }

        printNodeAndItsChildren(ustepNode);
        wasPrinting = true;
    }

    // e.g. Art. 30a, ust. 2a., pkt 1a)
    private void printPunkt() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String artykul = configSplit[0]; // e.g. art133
        String ustep = configSplit[1].substring(3); // e.g. 3a
        String punkt = configSplit[2].substring(3); // e.g. 10b

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if (artykulNode == null) {
            throw new NotFoundException("Nie znaleziono takiego artykułu!");
        }

        Node ustepNode = findNodeAtDepth(artykulNode, ustep, 4);

        if (ustepNode == null) {
            throw new NotFoundException("Nie znaleziono takiego ustępu!");
        }

        Node punktNode = findNodeAtDepth(ustepNode, punkt, 5);

        if (punktNode == null) {
            throw new NotFoundException("Nie znaleziono takiego punktu!");
        }

        printNodeAndItsChildren(punktNode);
        wasPrinting = true;
    }

    // e.g. Art. 30a, ust. 2a., pkt 1a), lit. b)
    private void printLitera() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String artykul = configSplit[0]; // e.g. art133
        String ustep = configSplit[1].substring(3); // e.g. 3a
        String punkt = configSplit[2].substring(3); // e.g. 10b
        String litera = configSplit[3].substring(3); // e.g. a

        Node artykulNode = findNodeAtDepth(root, artykul, 3);

        if (artykulNode == null) {
            throw new NotFoundException("Nie znaleziono takiego artykułu!");
        }

        Node ustepNode = findNodeAtDepth(artykulNode, ustep, 4);

        if (ustepNode == null) {
            throw new NotFoundException("Nie znaleziono takiego ustępu!");
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

    private void printAll() {
        printNodeAndItsChildren(root);
    }

    private void printTableOfContents() {
        printTableOfContents(root, 0);
    }

    private void printTableOfContents(Node node, int index) {
        for (int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        if (node.getDepth() == 4) {
            System.out.println("Ustęp " + (index + 1) + ".");
        } else if (node.getDepth() == 5) {
            System.out.println("Punkt " + (index + 1) + ")");
        } else if (node.getDepth() == 6) {
            System.out.println("Litera " + ((char) (index + 97)) + ")");
        } else {
            System.out.println(node.getData());
        }

        for (int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printTableOfContents(childrenNode, i);
        }
        wasPrinting = true;
    }
}

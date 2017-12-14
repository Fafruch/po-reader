public class UokikPrinter extends AbstractPrinter {
    public UokikPrinter(String[] args) {
        super(args);
    }

    protected void printElements() {
        KonstNormalizer konstNormalizer = new KonstNormalizer();
        args[2] = konstNormalizer.normalizeString(args[2]);

        System.out.println(args[2]);

        if(args[2].matches("^dzia[łl]\\w{1,4},rozdzia[łl]\\d+$")) {
            int indexOfComma = args[2].indexOf(',');
            String dzial = args[2].substring(0, indexOfComma);
            String rozdzial = args[2].substring(indexOfComma + 1);

            Node dzialNode = findNodeAtDepth(root, dzial, 1);

            if(dzialNode == null) {
                throw new Error("Nie ma takiego dzialu!");
            }

            Node rozdzialNode = findNodeAtDepth(dzialNode, rozdzial, 2);

            if(rozdzialNode == null) {
                throw new Error("Nie ma takiego rozdzialu!");
            }

            printNodeChildren(rozdzialNode);
        } else if(args[2].matches("^dzia[łl]\\w{1,4}$")) {
            int indexOfComma = args[2].indexOf(',');
            String dzial = args[2].substring(0, indexOfComma);

            System.out.println(dzial);

            Node dzialNode = findNodeAtDepth(root, dzial, 1);

            if(dzialNode == null) {
                throw new Error("Nie ma takiego dzialu!");
            }

            printNodeChildren(dzialNode);
        } else if(args[2].matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?$")) {
            String artykul = args[2];

            Node artykulNode = findNodeAtDepth(root, artykul, 3);

            if(artykulNode == null) {
                throw new Error("Nie ma takiego artykulu!");
            }

            printNodeChildren(artykulNode);
        } else if(args[2].matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?-\\d{1,3}[a-z]{0,3}(\\.)?$")) {
            int indexOfDash = args[2].indexOf('-');

            String firstArtykul = args[2].substring(0, indexOfDash);
            String lastArtykul = "art." + args[2].substring(indexOfDash + 1);

            System.out.println(firstArtykul);
            System.out.println(lastArtykul);

            if(firstArtykul.compareTo(lastArtykul) > 0) {
                throw new Error("Niepoprawny zakres artykulow!");
            }

            printArtykulyBetween(root, firstArtykul, lastArtykul);
        } else if(args[2].matches("^art\\.\\d{1,3}[a-z]{0,3}(\\.)?,ust\\.\\d{1,3}[a-z]{0,3}(\\))$")) {
            /*int indexOfComma = args[2].indexOf(',');

            String artykul = args[2].substring(0, indexOfComma);
            String ustep = args[2].substring(indexOfComma + 1);

            int artykulIndex = Integer.parseInt(artykul);
            int ustepIndex = Integer.parseInt(ustep) - 1;

            if(artykulIndex < 1 || artykulIndex > 243) {
                throw new Error("Nie ma takiego artykulu!");
            }

            Node artykulNode = getArtykul(root, artykulIndex);

            if(artykulNode == null) {
                throw new Error("Nie znaleziono takiego artykulu!");
            }

            if(ustepIndex >= artykulNode.getChildren().size() || ustepIndex < 0) {
                throw new Error("Nie ma takiego ustepu!");
            }

            printNodeChildren(artykulNode.getChildren().get(ustepIndex));*/
        }
    }

    private void printArtykulyBetween(Node node, String firstArtykulName, String lastArtykulName) {
        if(node.getDepth() == 3) {
            String data = node.getData();
            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            String normalizedData = uokikNormalizer.normalizeString(data);

            System.out.println(firstArtykulName);
            System.out.println(firstArtykulName.compareTo(normalizedData) <= -1);
            System.out.println(normalizedData);
            System.out.println(normalizedData.compareTo(lastArtykulName) <= -1);
            System.out.println(lastArtykulName);

            if(firstArtykulName.compareTo(normalizedData) <= -1  && normalizedData.compareTo(lastArtykulName) <= -1) {
                printNodeChildren(node);
            }

            return;
        }

        for(Node child : node.getChildren()) {
            printArtykulyBetween(child, firstArtykulName, lastArtykulName);
        }
    }

    private Node findNodeAtDepth(Node root, String name, int depth) {
        if(root.getDepth() == depth) {
            String data = root.getData();

            UokikNormalizer uokikNormalizer = new UokikNormalizer();
            String normalizedData = uokikNormalizer.normalizeString(data);

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

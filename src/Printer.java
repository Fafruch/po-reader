public class Printer {
    private Node root;
    private String[] args;

    public Printer(Node root, String[] args) {
        this.root = root;
        this.args = args;
    }

    public void print() {
        if(this.args[1].equals("-t")) {
            printTableOfContents(root, 0);
        } else if (this.args[1].equals("-e")) {
            printElements();
        } else {
            throw new Error("You've provided not a valid mode. Use '-t' for table of contents or '-e' for particular element.");
        }
    }

    private void printTableOfContents(Node node, int index) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        if(node.getDepth() == 4) {
            System.out.println("Ustep " + (index + 1) + ".");
        } else if (node.getDepth() == 5) {
            System.out.println("Punkt " + (index + 1) + ")");
        } else {
            System.out.println(node.getData());
        }

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printTableOfContents(childrenNode, i);
        }
    }

    private void printElements() {
        Normalizer normalizer = new Normalizer();
        args[2] = normalizer.normalizeString(args[2]);

        if(args[2].matches("^rozdział\\d+,dział\\d+$")) {
            int indexOfComma = args[2].indexOf(',');
            String rozdzial = args[2].substring(8, indexOfComma);
            String dzial = args[2].substring(indexOfComma + 6);

            int rozdzialIndex = Integer.parseInt(rozdzial) - 1;
            int dzialIndex = Integer.parseInt(dzial) - 1;

            if(rozdzialIndex >= root.getChildren().size() || rozdzialIndex < 0) {
                throw new Error("Nie ma takiego rozdzialu!");
            }

            if(dzialIndex >= root.getChildren().get(rozdzialIndex).getChildren().size() || dzialIndex < 0) {
                throw new Error("Nie ma takiego dzialu!");
            }

            Node element = root.getChildren().get(rozdzialIndex).getChildren().get(dzialIndex);

            printNodeChildren(element);
        } else if(args[2].matches("^rozdział\\d+$")) {
            String rozdzial = args[2].substring(8);
            int rozdzialIndex = Integer.parseInt(rozdzial) - 1;

            if(rozdzialIndex >= root.getChildren().size() || rozdzialIndex < 0) {
                throw new Error("Nie ma takiego rozdzialu!");
            }

            Node element = root.getChildren().get(rozdzialIndex);

            printNodeChildren(element);
        } else if(args[2].matches("^art.\\d+$")) {
            String artykul = args[2].substring(4);
            int artykulIndex = Integer.parseInt(artykul);

            if(artykulIndex < 1 || artykulIndex > 243) {
                throw new Error("Nie ma takiego artykulu!");
            }

            printArtykulyBetween(root, artykulIndex, artykulIndex);
        } else if(args[2].matches("^art.\\d+-\\d+$")) {
            int indexOfDash = args[2].indexOf('-');

            String firstArtykul = args[2].substring(4, indexOfDash);
            String lastArtykul = args[2].substring(indexOfDash + 1);

            int firstArtykulIndex = Integer.parseInt(firstArtykul);
            int lastArtykulIndex = Integer.parseInt(lastArtykul);

            if(firstArtykulIndex > lastArtykulIndex) {
                throw new Error("Niepoprawny zakres artykulow!");
            }

            if(firstArtykulIndex < 1 || firstArtykulIndex > 243 || lastArtykulIndex < 1 || lastArtykulIndex > 243) {
                throw new Error("Nie ma takiego artykulu!");
            }

            printArtykulyBetween(root, firstArtykulIndex, lastArtykulIndex);
        } else if(args[2].matches("^art.\\d+,ust.\\d+$")) {
            int indexOfComma = args[2].indexOf(',');

            String artykul = args[2].substring(4, indexOfComma);
            String ustep = args[2].substring(indexOfComma + 5);

            int artykulIndex = Integer.parseInt(artykul);
            int ustepIndex = Integer.parseInt(ustep) - 1;

            if(artykulIndex < 1 || artykulIndex > 243) {
                throw new Error("Nie ma takiego artykulu!");
            }

            Node artykulNode = getArtykul(root, artykulIndex);

            if(artykulNode == null) {
                throw new Error("Nie znaleziono takiego artykulu!");
            }

            printNodeChildren(artykulNode.getChildren().get(ustepIndex));
        }
    }

    private void printArtykulyBetween(Node node, int firstArtykulIndex, int lastArtykulIndex) {
        if(node.getDepth() > 3) return;

        if(node.getDepth() == 3) {
            String data = node.getData();
            int lastIndexOfDot = data.lastIndexOf('.');
            String artykul = node.getData().substring(5, lastIndexOfDot);

            int artykulIndex = Integer.parseInt(artykul);

            if(firstArtykulIndex <= artykulIndex && artykulIndex <= lastArtykulIndex) {
                printNodeChildren(node);
            }
        }

        for(Node child : node.getChildren()) {
            printArtykulyBetween(child, firstArtykulIndex, lastArtykulIndex);
        }
    }

    private void printNodeChildren(Node node) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printNodeChildren(childrenNode);
        }
    }

    private Node getArtykul(Node node, int index) {
        if(node.getDepth() == 3) {
            String data = node.getData();
            int lastIndexOfDot = data.lastIndexOf('.');
            String artykul = node.getData().substring(5, lastIndexOfDot);

            int artykulIndex = Integer.parseInt(artykul);

            if(artykulIndex == index) {
                return node;
            }
        }

        if(node.getDepth() <= 2) {
            for(Node child : node.getChildren()) {
                Node result = getArtykul(child, index);

                if(result != null) {
                    return result;
                }
            }
        }

        return null;
    }
}

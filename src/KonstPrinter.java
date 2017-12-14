public class KonstPrinter extends AbstractPrinter {
    public KonstPrinter(String[] args) {
        super(args);
    }

    protected void printElements() {
        KonstNormalizer konstNormalizer = new KonstNormalizer();
        args[2] = konstNormalizer.normalizeString(args[2]);

        if(args[2].matches("^rozdzia[łl]\\d+,dzia[łl]\\d+$")) {
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
        } else if(args[2].matches("^rozdzia[łl]\\d+$")) {
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

            if(ustepIndex >= artykulNode.getChildren().size() || ustepIndex < 0) {
                throw new Error("Nie ma takiego ustepu!");
            }

            printNodeChildren(artykulNode.getChildren().get(ustepIndex));
        } else if(args[2].matches("^art.\\d+,ust.\\d+,pkt\\d+\\)$")) {
            int firstIndexOfComma = args[2].indexOf(',');
            int lastIndexOfComma = args[2].lastIndexOf(',');
            int indexOfParenthesis = args[2].lastIndexOf(')');

            String artykul = args[2].substring(4, firstIndexOfComma);
            String ustep = args[2].substring(firstIndexOfComma + 5, lastIndexOfComma);
            String punkt = args[2].substring(lastIndexOfComma + 4, indexOfParenthesis);

            int artykulIndex = Integer.parseInt(artykul);
            int ustepIndex = Integer.parseInt(ustep) - 1;
            int punktIndex = Integer.parseInt(punkt) - 1;

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

            if(punktIndex >= artykulNode.getChildren().get(ustepIndex).getChildren().size() || punktIndex < 0) {
                throw new Error("Nie ma takiego ustepu!");
            }

            printNodeChildren(artykulNode.getChildren().get(ustepIndex).getChildren().get(punktIndex));
        }
    }

    private void printArtykulyBetween(Node node, int firstArtykulIndex, int lastArtykulIndex) {
        if(node.getDepth() == 3) {
            String data = node.getData();
            int lastIndexOfDot = data.lastIndexOf('.');
            String artykul = node.getData().substring(5, lastIndexOfDot);

            int artykulIndex = Integer.parseInt(artykul);

            if(firstArtykulIndex <= artykulIndex && artykulIndex <= lastArtykulIndex) {
                printNodeChildren(node);
            }

            return;
        }

        for(Node child : node.getChildren()) {
            printArtykulyBetween(child, firstArtykulIndex, lastArtykulIndex);
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
            } else {
                return null;
            }
        }

        for(Node child : node.getChildren()) {
            Node result = getArtykul(child, index);

            if(result != null) {
                return result;
            }
        }

        return null;
    }
}

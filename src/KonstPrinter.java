public class KonstPrinter extends AbstractPrinter {
    public KonstPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException{
        KonstNormalizer konstNormalizer = new KonstNormalizer();
        args[2] = konstNormalizer.normalizeString(args[2]);

        // np. Rozdział 3
        if(args[2].matches("^rozdzia[łl]\\d+$")) {
            String rozdzial = args[2].substring(8);
            int rozdzialIndex = Integer.parseInt(rozdzial) - 1;

            if(rozdzialIndex < 0 || rozdzialIndex >= root.getChildren().size()) {
                throw new NotFoundException("Nie ma takiego rozdzialu!");
            }

            Node rozdzialNode = root.getChildren().get(rozdzialIndex);

            printNodeChildren(rozdzialNode);

            // np. Rozdział 3, dział 2
        } else if(args[2].matches("^rozdzia[łl]\\d+,dzia[łl]\\d+$")) {
            int indexOfComma = args[2].indexOf(',');
            String rozdzial = args[2].substring(8, indexOfComma);
            String dzial = args[2].substring(indexOfComma + 6);

            int rozdzialIndex = Integer.parseInt(rozdzial) - 1;
            int dzialIndex = Integer.parseInt(dzial) - 1;

            if(rozdzialIndex < 0 || rozdzialIndex >= root.getChildren().size()) {
                throw new NotFoundException("Nie ma takiego rozdzialu!");
            }

            Node rozdzialNode = root.getChildren().get(rozdzialIndex);

            if(dzialIndex < 0 || dzialIndex >= root.getChildren().get(rozdzialIndex).getChildren().size()) {
                throw new NotFoundException("Nie ma takiego dzialu!");
            }

            Node dzialNode = rozdzialNode.getChildren().get(dzialIndex);

            printNodeChildren(dzialNode);

            // np. Art. 13
        } else if(args[2].matches("^art.\\d+$")) {
            String artykul = args[2].substring(4);
            int artykulIndex = Integer.parseInt(artykul) - 1;

            if(artykulIndex < 0 || artykulIndex > Node.getArtykuly().size() - 1) {
                throw new NotFoundException("Nie ma takiego artykulu!");
            }

            printArtykulyBetween(artykulIndex, artykulIndex);

            // np. Art. 13-17
        } else if(args[2].matches("^art.\\d+-\\d+$")) {
            int indexOfDash = args[2].indexOf('-');

            String firstArtykul = args[2].substring(4, indexOfDash);
            String lastArtykul = args[2].substring(indexOfDash + 1);

            int firstArtykulIndex = Integer.parseInt(firstArtykul) - 1;
            int lastArtykulIndex = Integer.parseInt(lastArtykul) - 1;

            if(firstArtykulIndex > lastArtykulIndex) {
                throw new IllegalArgumentException("Niepoprawny zakres artykulow!");
            }

            if(firstArtykulIndex < 0 || firstArtykulIndex > Node.getArtykuly().size() - 1
                    || lastArtykulIndex < 0 || lastArtykulIndex > Node.getArtykuly().size() - 1) {
                throw new NotFoundException("Nie ma takiego artykulu!");
            }

            printArtykulyBetween(firstArtykulIndex, lastArtykulIndex);

            // np. Art. 13, ust. 4
        } else if(args[2].matches("^art.\\d+,ust.\\d+$")) {
            int indexOfComma = args[2].indexOf(',');

            String artykul = args[2].substring(4, indexOfComma);
            String ustep = args[2].substring(indexOfComma + 5);

            int artykulIndex = Integer.parseInt(artykul) - 1;
            int ustepIndex = Integer.parseInt(ustep) - 1;

            if(artykulIndex < 0 || artykulIndex > Node.getArtykuly().size() - 1) {
                throw new NotFoundException("Nie ma takiego artykulu!");
            }

            Node artykulNode = Node.getArtykuly().get(artykulIndex);

            if(ustepIndex < 0 || ustepIndex >= artykulNode.getChildren().size()) {
                throw new NotFoundException("Nie ma takiego ustepu!");
            }

            Node ustepNode = artykulNode.getChildren().get(ustepIndex);

            printNodeChildren(ustepNode);

            // np. Art. 13-17, ust. 4, pkt 2)
        } else if(args[2].matches("^art.\\d+,ust.\\d+,pkt\\d+\\)$")) {
            int firstIndexOfComma = args[2].indexOf(',');
            int lastIndexOfComma = args[2].lastIndexOf(',');
            int indexOfParenthesis = args[2].lastIndexOf(')');

            String artykul = args[2].substring(4, firstIndexOfComma);
            String ustep = args[2].substring(firstIndexOfComma + 5, lastIndexOfComma);
            String punkt = args[2].substring(lastIndexOfComma + 4, indexOfParenthesis);

            int artykulIndex = Integer.parseInt(artykul) - 1;
            int ustepIndex = Integer.parseInt(ustep) - 1;
            int punktIndex = Integer.parseInt(punkt) - 1;

            if(artykulIndex < 0 || artykulIndex > Node.getArtykuly().size() - 1) {
                throw new NotFoundException("Nie ma takiego artykulu!");
            }

            Node artykulNode = Node.getArtykuly().get(artykulIndex);

            if(ustepIndex < 0 || ustepIndex >= artykulNode.getChildren().size()) {
                throw new NotFoundException("Nie ma takiego ustepu!");
            }

            Node ustepNode = artykulNode.getChildren().get(ustepIndex);

            if(punktIndex < 0 || punktIndex >= ustepNode.getChildren().size()) {
                throw new NotFoundException("Nie ma takiego punktu!");
            }

            Node punktNode = ustepNode.getChildren().get(punktIndex);

            printNodeChildren(punktNode);
        }
    }

    private void printArtykulyBetween(int firstArtykulIndex, int lastArtykulIndex) {
        for(int i = firstArtykulIndex; i <= lastArtykulIndex; i++) {
            Node artykulNode = Node.getArtykuly().get(i);

            printNodeChildren(artykulNode);
        }
    }
}

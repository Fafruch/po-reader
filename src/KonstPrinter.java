public class KonstPrinter extends AbstractPrinter {
    public KonstPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        super.printElements();

        if (normalizedConfig.matches("^" + Pattern.ROZDZIAL + "$")) {
            // e.g. Rozdział III
            printRozdzial();

        } else if (normalizedConfig.matches("^" + Pattern.ROZDZIAL + ',' + Pattern.DZIAL + "$")) {
            // e.g. Rozdział III, dział 2
            printDzial();
        }
    }

    private void printRozdzial() throws NotFoundException {
        String rozdzial = normalizedConfig; // e.g. rodzialiii

        Node rozdzialNode = findNodeAtDepth(root, rozdzial, 1);

        if (rozdzialNode == null) {
            throw new NotFoundException("Nie ma takiego rozdziału!");
        }

        printNodeAndItsChildren(rozdzialNode);
        wasPrinting = true;
    }

    private void printDzial() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String rozdzial = configSplit[0]; // e.g. rozdzialiii
        int dzialIndex = Integer.parseInt(configSplit[1].substring(5)) - 1; // e.g. 2

        Node rozdzialNode = findNodeAtDepth(root, rozdzial, 1);

        if (rozdzialNode == null) {
            throw new NotFoundException("Nie ma takiego rozdziału!");
        }

        if (dzialIndex < 0 || dzialIndex >= rozdzialNode.getChildren().size()) {
            throw new NotFoundException("Nie ma takiego działu!");
        }

        Node dzialNode = rozdzialNode.getChildren().get(dzialIndex);

        printNodeAndItsChildren(dzialNode);
        wasPrinting = true;
    }
}
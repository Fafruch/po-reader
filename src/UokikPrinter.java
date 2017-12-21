public class UokikPrinter extends AbstractPrinter {
    public UokikPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        super.printElements();

        if (normalizedConfig.matches('^' + Pattern.DZIAL + '$')) {
            // e.g. Dział IIIA
            printDzial();

        } else if (normalizedConfig.matches('^' + Pattern.DZIAL + ',' + Pattern.ROZDZIAL + '$')) {
            // e.g. Dział IIIA, rozdział 2
            printRozdzial();
        }
    }

    private void printDzial() throws NotFoundException {
        String dzial = normalizedConfig; // e.g. dzialiiia

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if (dzialNode == null) {
            throw new NotFoundException("Nie ma takiego działu!");
        }

        printNodeAndItsChildren(dzialNode);
        wasPrinting = true;
    }

    private void printRozdzial() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String dzial = configSplit[0]; // e.g. dzialiiia
        String rozdzial = configSplit[1]; // e.g. rozdzial2

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if (dzialNode == null) {
            throw new NotFoundException("Nie ma takiego działu!");
        }

        Node rozdzialNode = findNodeAtDepth(dzialNode, rozdzial, 2);

        if (rozdzialNode == null) {
            throw new NotFoundException("Nie ma takiego rozdziału!");
        }

        printNodeAndItsChildren(rozdzialNode);
        wasPrinting = true;
    }
}

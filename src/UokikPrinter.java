public class UokikPrinter extends AbstractPrinter {
    public UokikPrinter(String[] args) {
        super(args);
    }

    protected void printElements() throws NotFoundException, IllegalArgumentException {
        super.printElements();

        if(normalizedConfig.matches('^' + Pattern.DZIAL + '$')) {
            // np. Dział IIIA
            printDzial();

        } else if(normalizedConfig.matches('^' + Pattern.DZIAL + ',' + Pattern.ROZDZIAL + '$')) {
            // np. Dział IIIA, rozdział 2
            printRozdzial();
        }
    }

    private void printDzial() throws NotFoundException {
        String dzial = normalizedConfig;

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if(dzialNode == null) {
            throw new NotFoundException("Nie ma takiego dzialu!");
        }

        printNodeAndItsChildren(dzialNode);
        wasPrinting = true;
    }

    private void printRozdzial() throws NotFoundException {
        String[] configSplit = normalizedConfig.split(",");

        String dzial = configSplit[0]; // np. art133
        String rozdzial = configSplit[1]; // np. art245

        Node dzialNode = findNodeAtDepth(root, dzial, 1);

        if(dzialNode == null) {
            throw new NotFoundException("Nie ma takiego dzialu!");
        }

        Node rozdzialNode = findNodeAtDepth(dzialNode, rozdzial, 2);

        if(rozdzialNode == null) {
            throw new NotFoundException("Nie ma takiego rozdzialu!");
        }

        printNodeAndItsChildren(rozdzialNode);
        wasPrinting = true;
    }
}

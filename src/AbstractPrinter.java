abstract public class AbstractPrinter implements IPrinter {
    protected String[] args;
    protected Node root;

    public AbstractPrinter(String[] args) {
        this.args = args;
    }

    public void print(Node root) {
        this.root = root;

        if(this.args[1].equals("-t")) {
            printTableOfContents(root, 0);
        } else if (this.args[1].equals("-e")) {
            printElements();
        } else {
            throw new Error("You've provided not a valid mode. Use '-t' for table of contents or '-e' for particular element.");
        }
    }

    abstract protected void printTableOfContents(Node root, int index);
    abstract protected void printElements();
}

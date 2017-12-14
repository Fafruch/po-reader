abstract public class AbstractPrinter {
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
    }

    protected void printNodeChildren(Node node) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printNodeChildren(childrenNode);
        }
    }

    abstract protected void printElements();
}

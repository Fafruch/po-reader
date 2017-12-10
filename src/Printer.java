public class Printer {
    private Node root;
    private String[] args;

    public Printer(Node root, String[] args) {
        this.root = root;
        this.args = args;
    }

    public void print() {
        if(this.args[1].equals("-t")) {
            printTableOfContents(root);
        } else if (this.args[1].equals("-e")) {
            printElements();
        } else {
            throw new Error("You've provided not a valid mode. Use '-t' for table of contents or '-e' for particular element.");
        }
    }

    public void printTableOfContents(Node node) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printTableOfContents(childrenNode);
        }
    }

    public void printElements() {

    }
}

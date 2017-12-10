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

    public void printTableOfContents(Node node, int index) {
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

    public void printNodeChildren(Node node, int index) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printNodeChildren(childrenNode, i);
        }
    }

    public void printElements() {
        System.out.println(args[2]);
        if(args[2].matches("^Rozdział\\s\\d+$")) {
            int index = Integer.parseInt(args[2].substring(9));
            System.out.println(index);
        }

        if(args[2].matches("")) {

        }
    }
}

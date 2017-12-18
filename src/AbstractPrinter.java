abstract public class AbstractPrinter {
    protected String mode;
    protected String config;
    protected Node root;

    public AbstractPrinter(String[] args) {
        this.mode = args[1];
        this.config = args[2];
    }

    public void print(Node root) throws NotFoundException, IllegalArgumentException{
        this.root = root;

        if(mode.equals("-t")) {
            printTableOfContents(root, 0);
        } else {
            printElements();
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

    protected void printNodeAndItsChildren(Node node) {
        for(int i = 1; i < node.getDepth(); i++) {
            System.out.print("  ");
        }

        System.out.println(node.getData());

        for(int i = 0; i < node.getChildren().size(); i++) {
            Node childrenNode = node.getChildren().get(i);

            printNodeAndItsChildren(childrenNode);
        }
    }

    abstract protected void printElements() throws NotFoundException, IllegalArgumentException;
}

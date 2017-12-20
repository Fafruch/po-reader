import java.util.ArrayList;
import java.util.List;

abstract public class AbstractParser {
    protected List<String> storedFile;
    protected List<Node> stack = new ArrayList<>();

    public AbstractParser(List<String> storedFile) {
        this.storedFile = storedFile;
    }

    public abstract Node parseToTree(Node root);

    protected void addNodeToTreeOrStack(Node newNode) {
        Node lastNodeOnStack = this.stack.get(this.stack.size() - 1);

        if (lastNodeOnStack.getDepth() < newNode.getDepth()) {
            addNodeToTree(lastNodeOnStack, newNode);

            this.stack.add(newNode);
        } else if (lastNodeOnStack.getDepth() == newNode.getDepth()) {
            for (int i = this.stack.size() - 1; i >= 0; i--) {
                Node currentNode = this.stack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    addNodeToTree(currentNode, newNode);
                    break;
                }
            }

            this.stack.add(newNode);
        } else {
            for (int i = this.stack.size() - 1; i >= 0; i--) {
                Node currentNode = this.stack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    addNodeToTree(currentNode, newNode);
                    break;
                } else {
                    this.stack.remove(i);
                }
            }

            this.stack.add(newNode);
        }
    }

    private void addNodeToTree(Node parentNode, Node childNode) {
        parentNode.addChild(childNode);

        if (childNode.getDepth() == 3) {
            Node.getArtykuly().add(childNode);
        }
    }
}
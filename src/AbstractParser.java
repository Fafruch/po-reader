import java.util.LinkedList;
import java.util.List;

abstract public class AbstractParser {
    protected List<String> storedFile;
    protected List<Node> stack = new LinkedList<>();

    public AbstractParser(List<String> storedFile) {
        this.storedFile = storedFile;
    }

    public abstract Node parseToTree(Node root);

    protected void addNodeToTreeOrStack(Node newNode, Node root) {
        Node lastNodeOnStack = stack.get(stack.size() - 1);

        if (lastNodeOnStack.getDepth() < newNode.getDepth()) {
            addNodeToTree(lastNodeOnStack, newNode, root);

            stack.add(newNode);
        } else if (lastNodeOnStack.getDepth() == newNode.getDepth()) {
            for (int i = stack.size() - 1; i >= 0; i--) {
                Node currentNode = stack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    addNodeToTree(currentNode, newNode, root);
                    break;
                }
            }

            stack.add(newNode);
        } else {
            for (int i = stack.size() - 1; i >= 0; i--) {
                Node currentNode = stack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    addNodeToTree(currentNode, newNode, root);
                    break;
                } else {
                    stack.remove(i);
                }
            }

            stack.add(newNode);
        }
    }

    private void addNodeToTree(Node parentNode, Node childNode, Node root) {
        parentNode.addChild(childNode);

        if (childNode.getDepth() == 3) {
            root.getArticleList().add(childNode);
        }
    }
}
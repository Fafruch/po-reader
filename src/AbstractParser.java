import java.util.ArrayList;

abstract public class AbstractParser {
    protected ArrayList<String> storedFile;

    public AbstractParser(ArrayList<String> storedFile) {
        this.storedFile = storedFile;
    }

    public abstract Node parseToTree(Node root);

    protected void addNodeToTreeOrStack(ArrayList<Node> arrayListStack, Node newNode) {
        Node lastNodeOnStack = arrayListStack.get(arrayListStack.size() - 1);

        if (lastNodeOnStack.getDepth() < newNode.getDepth()) {
            addNodeToTree(lastNodeOnStack, newNode);

            arrayListStack.add(newNode);
        } else if (lastNodeOnStack.getDepth() == newNode.getDepth()) {
            for (int i = arrayListStack.size() - 1; i >= 0; i--) {
                Node currentNode = arrayListStack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    addNodeToTree(currentNode, newNode);
                    break;
                }
            }

            arrayListStack.add(newNode);
        } else {
            for (int i = arrayListStack.size() - 1; i >= 0; i--) {
                Node currentNode = arrayListStack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    addNodeToTree(currentNode, newNode);
                    break;
                } else {
                    arrayListStack.remove(i);
                }
            }

            arrayListStack.add(newNode);
        }
    }

    private void addNodeToTree(Node parentNode, Node childNode) {
        parentNode.addChild(childNode);

        if(childNode.getDepth() == 3) {
            Node.getArtykuly().add(childNode);
        }
    }
}
import java.util.ArrayList;

abstract public class AbstractParser {
    public abstract Node parseToTree(Node root);

    protected void addNodeToTreeOrStack(ArrayList<Node> arrayListStack, Node newNode) {
        Node lastNodeOnStack = arrayListStack.get(arrayListStack.size() - 1);

        if (lastNodeOnStack.getDepth() < newNode.getDepth()) {
            lastNodeOnStack.addChild(newNode);
            newNode.setParent(lastNodeOnStack);

            arrayListStack.add(newNode);
        } else if (lastNodeOnStack.getDepth() == newNode.getDepth()) {
            for (int i = arrayListStack.size() - 1; i >= 0; i--) {
                Node currentNode = arrayListStack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    currentNode.addChild(newNode);
                    newNode.setParent(currentNode);
                    break;
                }
            }

            arrayListStack.add(newNode);
        } else {
            for (int i = arrayListStack.size() - 1; i >= 0; i--) {
                Node currentNode = arrayListStack.get(i);

                if (currentNode.getDepth() < newNode.getDepth()) {
                    currentNode.addChild(newNode);
                    newNode.setParent(currentNode);
                    break;
                } else {
                    arrayListStack.remove(i);
                }
            }

            arrayListStack.add(newNode);
        }
    }
}
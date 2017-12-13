import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private ArrayList<String> storedFile;
    public Parser(ArrayList<String> storedFile) {
        this.storedFile = storedFile;
    }

    public Node parseToTree(Node root) {
        ArrayList<Node> arrayListStack = new ArrayList<>();
        arrayListStack.add(root);

        for(String line : storedFile) {
            boolean lineIsRodzial = line.matches(Pattern.ROZDZIAL);
            boolean lineIsDzial = line.matches(Pattern.DZIAL);
            boolean lineIsArtykul = line.matches(Pattern.ARTYKUL);
            boolean lineIsUstep = line.matches(Pattern.USTEP) || (
                    line.matches(Pattern.KAZDA_LINIA) &&
                            !line.matches(Pattern.ROZDZIAL) &&
                            !line.matches(Pattern.DZIAL) &&
                            !line.matches(Pattern.USTEP) &&
                            !line.matches(Pattern.PUNKT));
            boolean lineIsPunkt = line.matches(Pattern.PUNKT);

            int depth = 0;

            if(lineIsRodzial) {
                depth = 1;
            } else if(lineIsDzial) {
                depth = 2;
            } else if(lineIsArtykul) {
                depth = 3;
            } else if(lineIsUstep) {
                depth = 4;
            } else if(lineIsPunkt) {
                depth = 5;
            }

            Node newNode = new Node(depth, line);
            Node lastNodeOnStack = arrayListStack.get(arrayListStack.size() - 1);

            if(lastNodeOnStack.getDepth() < newNode.getDepth()) {
                lastNodeOnStack.addChild(newNode);
                newNode.setParent(lastNodeOnStack);

                arrayListStack.add(newNode);
            } else if(lastNodeOnStack.getDepth() == newNode.getDepth()) {
                for(int i = arrayListStack.size() - 1; i >= 0; i--) {
                    Node currentNode = arrayListStack.get(i);

                    if(currentNode.getDepth() < newNode.getDepth()) {
                        currentNode.addChild(newNode);
                        newNode.setParent(currentNode);
                        break;
                    }
                }

                arrayListStack.add(newNode);
            } else {
                for(int i = arrayListStack.size() - 1; i >= 0; i--) {
                    Node currentNode = arrayListStack.get(i);

                    if(currentNode.getDepth() < newNode.getDepth()) {
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

        return root;
    }
}

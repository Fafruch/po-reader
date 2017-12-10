import java.util.ArrayList;
import java.util.Stack;

public class Parser {
    private ArrayList<String> storedFile;
    String patternRozdzial = "^Rozdział \\w*$";
    String patternDzial = "^([A-Z,ŻŹĆĄŚĘŁÓŃ](\\s)?)+$";
    String patternArtykul = "^(Art. )(\\d)+(.)$";
    String patternUstep = "^(\\d+. )(.)*$";
    String patternPunkt = "^(\\d+)\\)(.)*$";
    String patternKazdaLinia = "^(.)*$";

    public Parser(ArrayList<String> storedFile) {
        this.storedFile = storedFile;
    }

    public Node parseToNodes(Node root) {
        ArrayList<Node> arrayListStack = new ArrayList<>();
        arrayListStack.add(root);

        for(String line : storedFile) {
            System.out.println(line);

            boolean lineIsRodzial = line.matches(patternRozdzial);
            boolean lineIsDzial = line.matches(patternDzial);
            boolean lineIsArtykul = line.matches(patternArtykul);
            boolean lineIsUstep = line.matches(patternUstep) || (
                    line.matches(patternKazdaLinia) &&
                            !line.matches(patternRozdzial) &&
                            !line.matches(patternDzial) &&
                            !line.matches(patternUstep) &&
                            !line.matches(patternPunkt));;
            boolean lineIsPunkt = line.matches(patternPunkt);

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

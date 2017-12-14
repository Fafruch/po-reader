import java.util.ArrayList;

public class KonstytucjaParser extends AbstractParser{
    private ArrayList<String> storedFile;

    public KonstytucjaParser(ArrayList<String> storedFile) {
        this.storedFile = storedFile;
    }

    public Node parseToTree(Node root) {
        ArrayList<Node> arrayListStack = new ArrayList<>();
        arrayListStack.add(root);

        for (String line : storedFile) {
            boolean lineIsRodzial = line.matches(Pattern.ROZDZIAL);
            boolean lineIsDzial = line.matches(Pattern.DZIAL);
            boolean lineIsArtykul = line.matches(Pattern.ARTYKUL);
            boolean lineIsUstep = line.matches(Pattern.USTEP) || (
                    !line.matches(Pattern.ROZDZIAL) &&
                            !line.matches(Pattern.DZIAL) &&
                            !line.matches(Pattern.USTEP) &&
                            !line.matches(Pattern.PUNKT));
            boolean lineIsPunkt = line.matches(Pattern.PUNKT);

            int depth = 0;
            if (lineIsRodzial) {
                depth = 1;
            } else if (lineIsDzial) {
                depth = 2;
            } else if (lineIsArtykul) {
                depth = 3;
            } else if (lineIsUstep) {
                depth = 4;
            } else if (lineIsPunkt) {
                depth = 5;
            }

            Node newNode = new Node(depth, line);
            addNodeToTreeOrStack(arrayListStack, newNode);
        }

        return root;
    }
}

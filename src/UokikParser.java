import java.util.ArrayList;

public class UokikParser extends AbstractParser {
    public UokikParser(ArrayList<String> storedFile) {
        super(storedFile);
    }

    public Node parseToTree(Node root) {
        this.stack.add(root);

        for (String line : storedFile) {
            boolean lineIsDzial = line.matches(UokikPattern.DZIAL);
            boolean lineIsRodzial = line.matches(UokikPattern.ROZDZIAL);
            boolean lineIsArtykul = line.matches(UokikPattern.ARTYKUL);
            boolean lineIsUstep = line.matches(UokikPattern.USTEP) || (
                    !line.matches(UokikPattern.ROZDZIAL) &&
                            !line.matches(UokikPattern.DZIAL) &&
                            !line.matches(UokikPattern.USTEP) &&
                            !line.matches(UokikPattern.PUNKT) &&
                            !line.matches(UokikPattern.LITERA));
            boolean lineIsPunkt = line.matches(UokikPattern.PUNKT);
            boolean lineIsLitera = line.matches(UokikPattern.LITERA);

            int depth = 0;
            if (lineIsDzial) {
                depth = 1;
            } else if (lineIsRodzial) {
                depth = 2;
            } else if (lineIsArtykul) {
                depth = 3;
            } else if (lineIsUstep) {
                depth = 4;
            } else if (lineIsPunkt) {
                depth = 5;
            } else if (lineIsLitera) {
                depth = 6;
            }

            Node newNode = new Node(depth, line);
            addNodeToTreeOrStack(newNode);
        }

        return root;
    }
}

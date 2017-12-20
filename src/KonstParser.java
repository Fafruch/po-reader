import java.util.List;

public class KonstParser extends AbstractParser {
    public KonstParser(List<String> storedFile) {
        super(storedFile);
    }

    public Node parseToTree(Node root) {
        this.stack.add(root);

        for (String line : storedFile) {
            boolean lineIsRodzial = line.matches(KonstPattern.ROZDZIAL);
            boolean lineIsDzial = line.matches(KonstPattern.DZIAL);
            boolean lineIsArtykul = line.matches(KonstPattern.ARTYKUL);
            boolean lineIsUstep = line.matches(KonstPattern.USTEP) || (
                    !line.matches(KonstPattern.ROZDZIAL) &&
                            !line.matches(KonstPattern.DZIAL) &&
                            !line.matches(KonstPattern.USTEP) &&
                            !line.matches(KonstPattern.PUNKT));
            boolean lineIsPunkt = line.matches(KonstPattern.PUNKT);

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
            addNodeToTreeOrStack(newNode);
        }

        return root;
    }
}

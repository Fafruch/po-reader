import java.util.List;

public class KonstParser extends AbstractParser {
    public KonstParser(List<String> storedFile) {
        super(storedFile);
    }

    public Node parseToTree(Node root) {
        stack.add(root);

        for (String line : storedFile) {
            int depth = 0;

            if (line.matches(KonstPattern.ROZDZIAL)) {
                depth = 1;
            } else if (line.matches(KonstPattern.DZIAL)) {
                depth = 2;
            } else if (line.matches(KonstPattern.ARTYKUL)) {
                depth = 3;
            } else if (line.matches(KonstPattern.USTEP) || line.matches(KonstPattern.NOTHING_EXCEPT_USTEP)) {
                depth = 4;
            } else if (line.matches(KonstPattern.PUNKT)) {
                depth = 5;
            }

            Node newNode = new Node(depth, line);
            addNodeToTreeOrStack(newNode);
        }

        return root;
    }
}

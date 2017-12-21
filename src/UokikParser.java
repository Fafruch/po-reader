import java.util.List;

public class UokikParser extends AbstractParser {
    public UokikParser(List<String> storedFile) {
        super(storedFile);
    }

    public Node parseToTree(Node root) {
        stack.add(root);

        for (String line : storedFile) {
            int depth = 0;

            if (line.matches(UokikPattern.DZIAL)) {
                depth = 1;
            } else if (line.matches(UokikPattern.ROZDZIAL)) {
                depth = 2;
            } else if (line.matches(UokikPattern.ARTYKUL)) {
                depth = 3;
            } else if (line.matches(UokikPattern.USTEP) || line.matches(UokikPattern.NOTHING_EXCEPT_USTEP)) {
                depth = 4;
            } else if (line.matches(UokikPattern.PUNKT)) {
                depth = 5;
            } else if (line.matches(UokikPattern.LITERA)) {
                depth = 6;
            }

            Node newNode = new Node(depth, line);
            addNodeToTreeOrStack(newNode);
        }

        return root;
    }
}

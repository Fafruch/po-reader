import java.util.ArrayList;
import java.util.List;

public class Node {
    static private List<Node> artykuly = new ArrayList<>();
    private List<Node> children = new ArrayList<>();
    private int depth;
    private String data;

    public Node(int depth, String data) {
        this.depth = depth;
        this.data = data;
    }

    static public List<Node> getArtykuly() {
        return artykuly;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getDepth() {
        return this.depth;
    }

    public String getData() {
        return this.data;
    }
}
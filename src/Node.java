import java.util.ArrayList;
import java.util.List;

public class Node {
    static private ArrayList<Node> artykuly = new ArrayList<>();
    private List<Node> children = new ArrayList<>();
    private int depth;
    private String data;
    private Node parent = null;

    public Node(int depth, String data) {
        this.depth = depth;
        this.data = data;
    }

    public Node(int depth, String data, Node parent) {
        this.depth = depth;
        this.data = data;
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }


    public int getDepth() {
        return this.depth;
    }

    public String getData() {
        return this.data;
    }

    static public ArrayList<Node> getArtykuly() {
        return artykuly;
    }
}
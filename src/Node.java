import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node parent = null;
    private List<Node> children = new ArrayList<>();
    private int depth;
    private String data;

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

    public void removeParent() {
        this.parent = null;
    }

    public void addNewChild(int depth, String data) {
        Node child = new Node(depth, data, this);
        this.children.add(child);
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0)
            return true;
        else
            return false;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }


    public int getDepth() {
        return this.depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }


    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
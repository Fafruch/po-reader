import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> children = new ArrayList<>();
    private int depth;
    private String data;
    private List<Node> articleList;

    public Node(int depth, String data) {
        this.depth = depth;
        this.data = data;
    }

    // constructor only for tree root
    public Node(int depth, String data, List<Node> articleList) {
        this.depth = depth;
        this.data = data;
        this.articleList = articleList;
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

    public List<Node> getArticleList() {
        return articleList;
    }
}
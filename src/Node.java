import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> children = new ArrayList<>();
    private int depth;
    private String data;
    private static List<Node> articleList = new ArrayList<>();

    public Node(int depth, String data) {
        this.depth = depth;
        this.data = data;
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

    public static List<Node> getArticleList() {
        return articleList;
    }
}
public class Node {
    public int frequency;
    public char data;
    public Node left, right;

    public Node(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    public Node(char data, int frequency, Node left, Node right) {
        this.data = data;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}
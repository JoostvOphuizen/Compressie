import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;

public class Huffman {
    private final Node root;
    private final Map<Character, String> charPrefixHashMap = new HashMap<>();

    public Huffman(String text) {
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>(Comparator.comparingInt(l -> l.frequency));

        int[] freq = new int[256];
        for (char c : text.toCharArray()) {
            freq[c]++;
        }

        for (int i = 0; i < 256; i++) {
            if (freq[i] > 0) {
                nodeQueue.offer(new Node((char) i, freq[i]));
            }
        }

        while (nodeQueue.size() > 1) {
            Node left = nodeQueue.poll();
            Node right = nodeQueue.poll();
            assert right != null;
            Node parent = new Node('\0', left.frequency + right.frequency, left, right);
            nodeQueue.offer(parent);
        }

        root = nodeQueue.poll();
        setPrefixCodes(root, new StringBuilder());
    }

    private void setPrefixCodes(Node node, StringBuilder prefix) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                charPrefixHashMap.put(node.data, prefix.toString());
            } else {
                prefix.append('0');
                setPrefixCodes(node.left, prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append('1');
                setPrefixCodes(node.right, prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    public String compress(String text) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : text.toCharArray()) {
            stringBuilder.append(charPrefixHashMap.get(c));
        }

        return stringBuilder.toString();
    }

    public String decompress(String s) {
        StringBuilder stringBuilder = new StringBuilder();

        Node temp = root;
        for (int i = 0; i < s.length(); i++) {
            int j = Integer.parseInt(String.valueOf(s.charAt(i)));
            if (j == 0) {
                temp = temp.left;
                assert temp != null;
                if (temp.left == null && temp.right == null) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
            if (j == 1) {
                temp = temp.right;
                if (temp.left == null && temp.right == null) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
        }

        return stringBuilder.toString();
    }

    public void printTree() {
        printTreeHelper(root, "");
    }

    private void printTreeHelper(Node node, String indent) {
        if (node != null) {
            printTreeHelper(node.right, indent + "    ");
            System.out.println(indent + node.frequency + (node.left == null && node.right == null ? ": " + node.data : ""));
            printTreeHelper(node.left, indent + "    ");
        }
    }
}
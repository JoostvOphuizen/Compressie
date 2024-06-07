import java.util.Arrays;

/**
 * Burrows-Wheeler transform
 * <p>
 * The Burrows-Wheeler transform of a string is a block sorting transform that is
 * useful for data compression. The transform rearranges a string into runs of
 * similar characters, which is useful for compression algorithms.
 */
public class BurrowsWheeler {

    public static String[] transform(String input) {
        String[] rotations = new String[input.length()];
        for (int i = 0; i < input.length(); i++) {
            rotations[i] = input.substring(i) + input.substring(0, i);
        }

        Arrays.sort(rotations);

        StringBuilder output = new StringBuilder(input.length());
        int originalIndex = -1;
        for (int i = 0; i < rotations.length; i++) {
            if (rotations[i].equals(input)) {
                originalIndex = i;
            }
            output.append(rotations[i].charAt(rotations[i].length() - 1));
        }

        return new String[]{output.toString(), String.valueOf(originalIndex)};
    }

    public static String inverseTransform(String input, int originalIndex) {
        int length = input.length();
        char[] sorted = input.toCharArray();
        Arrays.sort(sorted);

        int[] next = new int[length];
        boolean[] used = new boolean[length];
        for (int i = 0; i < length; i++) {
            char c = sorted[i];
            for (int j = 0; j < length; j++) {
                if (!used[j] && input.charAt(j) == c) {
                    used[j] = true;
                    next[i] = j;
                    break;
                }
            }
        }

        StringBuilder output = new StringBuilder(length);
        for (int i = 0, j = originalIndex; i < length; i++, j = next[j]) {
            output.append(sorted[j]);
        }

        return output.toString();
    }
}
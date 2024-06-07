/**
 * RunLength class
 * This class contains the encode and decode methods for the Run-Length encoding algorithm.
 */
public class RunLength {

    /**
     * Encodes the input string using the Run-Length encoding algorithm.
     *
     * @param input The input string to encode. Example: "aaabbbcc".
     * @return The encoded string. Example: "a3b3c2".
     */
    public static String encode(String input) {
        StringBuilder stringBuilder = new StringBuilder();

        char c = input.charAt(0);
        int count = 1;

        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == c) {
                count++;
            } else {
                stringBuilder.append(c);
                stringBuilder.append(count);
                c = input.charAt(i);
                count = 1;
            }
        }

        stringBuilder.append(c);
        stringBuilder.append(count);

        return stringBuilder.toString();
    }

    /**
     * Decodes the input string using the Run-Length encoding algorithm.
     *
     * @param input The input string to decode. Example: "a3b3c2".
     * @return The decoded string. Example: "aaabbbcc".
     */
    public static String decode(String input) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i += 2) {
            char c = input.charAt(i);
            int count = Integer.parseInt(String.valueOf(input.charAt(i + 1)));
            stringBuilder.append(String.valueOf(c).repeat(Math.max(0, count)));
        }

        return stringBuilder.toString();
    }
}

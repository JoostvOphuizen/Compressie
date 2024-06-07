import bitpacker.Floor;
import bitpacker.GroundItem;
import com.google.gson.Gson;
import gamestate.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Item[] items = {new Item(13), new Item(4)};
//        Item[] items2 = {new Item(1), new Item(2), new Item(3)};
//        Item[] items3 = {new Item(5), new Item(6), new Item(7), new Item(8)};
//        Item[] items4 = {new Item(9), new Item(10), new Item(11), new Item(12), new Item(13)};
//        Item[] items5 = {new Item(14), new Item(15), new Item(16), new Item(17), new Item(18), new Item(19)};
//        Item[] items6 = {new Item(20), new Item(21), new Item(22), new Item(23), new Item(24), new Item(25), new Item(26)};
//
//        // add 100 players with different inventories
//        Player[] players = new Player[100];
//        for (int i = 0; i < 100; i++) {
//            Item[] itemsToAdd = switch (i % 7) {
//                case 0 -> items;
//                case 1 -> items2;
//                case 2 -> items3;
//                case 3 -> items4;
//                case 4 -> items5;
//                case 5 -> items6;
//                case 6 -> new Item[0];
//                default -> null;
//            };
//            players[i] = new Player(i, 500, 4000, 44, 10, 99, itemsToAdd);
//        }
//
//        // add 100 monsters with different health
//        Monster[] monsters = new Monster[2000];
//        for (int i = 0; i < 2000; i++) {
//            monsters[i] = new Monster(3, 500, 15, 5);
//        }
//
//        // add 100 ground items with different item ids
//        GroundItem[] groundItems = new GroundItem[2000];
//        for (int i = 0; i < 2000; i++) {
//            groundItems[i] = new GroundItem(i, i, 5);
//        }
//
//        Floor floor = new Floor(0, players, monsters, groundItems);
//        byte[] bytes = floor.toBytes();
//
//        // print the bytes 1 by 1 in 1's and 0's
//        System.out.println();
//        StringBuilder s = new StringBuilder();
//        for (byte b : bytes) {
//            s.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
//        }
//        System.out.println(s);
//        System.out.println();
//
//        Floor unpackedFloor = Floor.fromBytes(bytes);
//        System.out.println(unpackedFloor);
//
//        Gson gson = new Gson();
//        String json = gson.toJson(unpackedFloor);
//        System.out.println(json);
//
//        // amount of chars
//        System.out.println("Amount of chars: " + json.length());
//        // amount of bits
//        System.out.println("Amount of bits: " + s.length());
//        // amount of bytes
//        System.out.println("Amount of bytes: " + bytes.length);

        String encoded = RunLength.encode("ASD klas b is beter!");
        System.out.println("Runlenght Encoded: " + encoded);
    }

    private static String compress(String text) {
        // burrow wheeler transform
        String[] transformed = BurrowsWheeler.transform(text);
        System.out.println("Burrow-Wheeler transform: " + transformed[0]);

        // Run-length encoding
        String encoded = RunLength.encode(transformed[0]);
        System.out.println("Runlenght Encoded: " + encoded);

        // Huffman encoding
        Huffman huffman = new Huffman(encoded);
        String compressed = huffman.compress(encoded);
        System.out.println("Huffman Compressed: " + compressed);

        // Huffman decoding
        String decompressed = huffman.decompress(compressed);
        System.out.println("Huffman Decompressed: " + decompressed);

        // Run-length decoding
        String decoded = RunLength.decode(decompressed);
        System.out.println("Runlength Decoded: " + decoded);

        // burrow wheeler inverse transform
        String inverseTransformed = BurrowsWheeler.inverseTransform(decoded, Integer.parseInt(transformed[1]));
        System.out.println("Burrow-Wheeler Inverse transformed: " + inverseTransformed);

        System.out.println("Original equals inverse transformed: " + text.equals(inverseTransformed));
        return inverseTransformed;
    }

    private static void testCompressedGameState() {
        GameState gameState = new GameState(1234);

        // Add two players
        //gameState.addPlayerState(new PlayerState(1, 100, new Item[]{new Item(10), new Item(5), new Item(2)},new DynamicEntity(100, 100, 600)));

        // Convert to bytes and print
        byte[] bytes = gameState.toBytes();
        StringBuilder s = new StringBuilder();
        for (byte b : bytes) {
            s.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        System.out.println("Compressed (" + "Byte size: " + bytes.length + "): ");
        System.out.println(s);

        // Convert back to CompressedGameState and print
        System.out.println("Decompressed:");
        GameState gameState2 = GameState.fromBytes(bytes);
        System.out.println(gameState2);
    }
}
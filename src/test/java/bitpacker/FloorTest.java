package bitpacker;

import gamestate.Item;
import gamestate.Monster;
import gamestate.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FloorTest {

    @Test
    public void testToBytes() throws IOException {
        Item[] items = {new Item(13), new Item(4)};
        Item[] items2 = {new Item(1), new Item(2), new Item(3)};
        Item[] items3 = {new Item(5), new Item(6), new Item(7), new Item(8)};
        Item[] items4 = {new Item(9), new Item(10), new Item(11), new Item(12), new Item(13)};
        Item[] items5 = {new Item(14), new Item(15), new Item(16), new Item(17), new Item(18), new Item(19)};
        Item[] items6 = {new Item(20), new Item(21), new Item(22), new Item(23), new Item(24), new Item(25), new Item(26)};

        // add 100 players with different inventories
        Player[] players = new Player[100];
        for (int i = 0; i < 100; i++) {
            Item[] itemsToAdd = switch (i % 7) {
                case 0 -> items;
                case 1 -> items2;
                case 2 -> items3;
                case 3 -> items4;
                case 4 -> items5;
                case 5 -> items6;
                case 6 -> new Item[0];
                default -> null;
            };
            players[i] = new Player(i, 500, 4000, 44, 10, 99, itemsToAdd);
        }

        // add 100 monsters with different health
        Monster[] monsters = new Monster[300];
        for (int i = 0; i < 300; i++) {
            monsters[i] = new Monster(3, 500, 15, 5);
        }

        // add 100 ground items with different item ids
        GroundItem[] groundItems = new GroundItem[4000];
        for (int i = 0; i < 4000; i++) {
            groundItems[i] = new GroundItem(i, i, 5);
        }

        Floor floor = new Floor(0, players, monsters, groundItems);
        byte[] bytes = floor.toBytes();

        // print the bytes 1 by 1 in 1's and 0's
        System.out.println();
        for (byte b : bytes) {
            System.out.print(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
        System.out.println();

        Floor unpackedFloor = Floor.fromBytes(bytes);
        System.out.println(unpackedFloor);

        assert (floor.equals(unpackedFloor));
    }
}

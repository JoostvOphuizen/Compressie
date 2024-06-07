package bitpacker;

import com.google.gson.Gson;
import gamestate.Item;
import gamestate.Monster;
import gamestate.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class Floor {
    private final int z;
    private final Player[] players;
    private final Monster[] monsters;
    private final GroundItem[] groundItems;

    public Floor(int z, Player[] players, Monster[] monsters, GroundItem[] groundItems) {
        this.z = z;
        this.players = players;
        this.monsters = monsters;
        this.groundItems = groundItems;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(z);

        // players
        outputStream.write(players.length);
        for (Player player : players) {
            byte[] playerBytes = player.toBytes();
            outputStream.write(playerBytes.length);
            outputStream.write(playerBytes);
        }

        // monsters
        outputStream.write(ByteBuffer.allocate(4).putInt(monsters.length).array());
        for (Monster monster : monsters) {
            byte[] monsterBytes = monster.toBytes();
            outputStream.write(monsterBytes.length);
            outputStream.write(monsterBytes);
        }

        // ground items
        groundItemsToBytes(outputStream);

        return outputStream.toByteArray();
    }

    private void groundItemsToBytes(ByteArrayOutputStream outputStream) throws IOException {
        // Sort the ground items by item ID
        Arrays.sort(groundItems, Comparator.comparingInt(GroundItem::getItemId));

        int currentId = -1;
        int count = 0;
        ByteArrayOutputStream itemStream = new ByteArrayOutputStream();
        for (GroundItem groundItem : groundItems) {
            if (groundItem.getItemId() != currentId) {
                // New item ID encountered, write the previous ID and count to the byte array
                if (currentId != -1) {
                    outputStream.write(ByteBuffer.allocate(2).putShort((short) currentId).array());
                    outputStream.write(ByteBuffer.allocate(2).putShort((short) count).array());
                    outputStream.write(itemStream.toByteArray());
                }

                // Reset the current ID and count
                currentId = groundItem.getItemId();
                count = 1;
                itemStream = new ByteArrayOutputStream();
            } else {
                // Same item ID, increment the count
                count++;
            }

            // Write the x and y coordinates of the item to the byte array
            int xy = (groundItem.getX() << 12) | groundItem.getY();
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(xy);
            byte[] xyBytes = buffer.array();

            // Skip the first byte and write the last 3 bytes to the itemStream
            itemStream.write(xyBytes, 1, 3);
        }

        // Write the last item ID and count to the byte array
        if (currentId != -1) {
            outputStream.write(ByteBuffer.allocate(2).putShort((short) currentId).array());
            outputStream.write(ByteBuffer.allocate(2).putShort((short) count).array());
            outputStream.write(itemStream.toByteArray());
        }
    }

    public static Floor fromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        int z = inputStream.read();

        // players
        int numberOfPlayers = inputStream.read();
        Player[] players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            int playerSize = inputStream.read();
            byte[] playerBytes = new byte[playerSize];
            inputStream.read(playerBytes);
            players[i] = Player.fromBytes(playerBytes);
        }

        // monsters
        byte[] monsterCountBytes = new byte[4];
        inputStream.read(monsterCountBytes);
        int numberOfMonsters = ByteBuffer.wrap(monsterCountBytes).getInt();
        Monster[] monsters = new Monster[numberOfMonsters];
        for (int i = 0; i < numberOfMonsters; i++) {
            int monsterSize = inputStream.read();
            byte[] monsterBytes = new byte[monsterSize];
            inputStream.read(monsterBytes);
            monsters[i] = Monster.fromBytes(monsterBytes);
        }

        // ground items
        List<GroundItem> groundItemList = new ArrayList<>();
        while (inputStream.available() > 0) {
            byte[] itemIdBytes = new byte[2];
            inputStream.read(itemIdBytes);
            int itemId = ByteBuffer.wrap(itemIdBytes).getShort();

            byte[] itemCountBytes = new byte[2];
            inputStream.read(itemCountBytes);
            int itemCount = ByteBuffer.wrap(itemCountBytes).getShort();

            for (int i = 0; i < itemCount; i++) {
                byte[] xyBytes = new byte[3];
                inputStream.read(xyBytes);
                int xy = 0;
                xy = ((xyBytes[0] & 0xFF) << 16) | ((xyBytes[1] & 0xFF) << 8) | (xyBytes[2] & 0xFF);
                int x = (xy >> 12) & 0xFFF; // Extract the first 12 bits for x
                int y = xy & 0xFFF; // Extract the last 12 bits for y

                groundItemList.add(new GroundItem(x, y, itemId));
            }
        }
        GroundItem[] groundItems = groundItemList.toArray(new GroundItem[0]);

        return new Floor(z, players, monsters, groundItems);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Floor floor = (Floor) obj;
        return z == floor.z && Arrays.equals(players, floor.players);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Z: ").append(z).append("\n");
        s.append("Players: ");
        for (Player player : players) {
            s.append(player).append(" ");
        }
        s.append("\n");
        s.append("Monsters: ");
        for (Monster monster : monsters) {
            s.append(monster).append(" ");
        }
        s.append("\n");
        s.append("Ground items: ");
        for (GroundItem item : groundItems) {
            s.append(item).append(" ");
        }
        return s.toString();
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
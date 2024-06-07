package gamestate;

import bitpacker.PosEntity;

import java.util.Arrays;
import java.util.BitSet;

public class Player extends PosEntity {
    private final int playerId;
    private final int healthPoints;
    private final int level;
    private final int currentXp;
    private final Item[] inventory;

    public Player(int playerId, int x, int y, int healthPoints, int level, int currentXp, Item[] inventory) {
        super(x, y);
        this.playerId = playerId;
        this.healthPoints = healthPoints;
        this.level = level;
        this.currentXp = currentXp;
        this.inventory = inventory;
    }

    public byte[] toBytes() {
        byte[] posBytes = super.toBytes();
        byte[] bytes = new byte[5 + posBytes.length + inventory.length];
        System.arraycopy(posBytes, 0, bytes, 0, posBytes.length);
        bytes[posBytes.length] = (byte) playerId;
        bytes[posBytes.length + 1] = (byte) healthPoints;
        bytes[posBytes.length + 2] = (byte) level;
        bytes[posBytes.length + 3] = (byte) currentXp;
        bytes[posBytes.length + 4] = (byte) inventory.length;
        for (int i = 0; i < inventory.length; i++) {
            bytes[i + posBytes.length + 5] = inventory[i].toByte();
        }
        return bytes;
    }

    public static Player fromBytes(byte[] bytes) {
        int x = ((bytes[0] & 0xFF) << 4) | ((bytes[1] & 0xFF) >> 4);
        int y = ((bytes[1] & 0x0F) << 8) | (bytes[2] & 0xFF);
        int playerId = bytes[3];
        int healthPoints = bytes[4];
        int level = bytes[5];
        int currentXp = bytes[6];
        int inventorySize = bytes[7] & 0xFF;
        Item[] inventory = new Item[inventorySize];
        for (int i = 0; i < inventorySize; i++) {
            inventory[i] = Item.fromByte(bytes[i + 8]);
        }
        return new Player(playerId, x, y, healthPoints, level, currentXp, inventory);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Player ID: ").append(playerId).append("\n");
        s.append("Health: ").append(healthPoints).append("\n");
        s.append("Level: ").append(level).append("\n");
        s.append("Current XP: ").append(currentXp).append("\n");
        s.append("Inventory: ");
        for (Item item : inventory) {
            s.append(item).append(" ");
        }
        s.append("\n");
        s.append("X: ").append(getX()).append(" Y: ").append(getY());
        return s.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return playerId == player.playerId &&
                healthPoints == player.healthPoints &&
                level == player.level &&
                currentXp == player.currentXp &&
                Arrays.equals(inventory, player.inventory) &&
                x == player.x &&
                y == player.y;
    }
}
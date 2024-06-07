package gamestate;

import gamestate.compressible.Compressible;

public record PlayerState(
        int playerId,
        int health,
        Item[] inventory,
        DynamicEntity dynamicEntity
) implements Compressible {

    public PlayerState(int playerId, int health, Item[] inventory, DynamicEntity dynamicEntity) {
        this.playerId = playerId & 0x7F; // Force into 7 bits
        this.health = health & 0xFF; // Force into 8 bits
        this.inventory = inventory;
        this.dynamicEntity = dynamicEntity;
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[7 + (inventory.length + 1) / 2];
        bytes[0] = (byte) playerId;
        bytes[1] = (byte) health;
        byte[] entityBytes = dynamicEntity.toBytes();
        System.arraycopy(entityBytes, 0, bytes, 2, 5);
        for (int i = 0; i < inventory.length; i += 2) {
            bytes[7 + i / 2] = (byte) ((inventory[i].id() << 4) | (i + 1 < inventory.length ? inventory[i + 1].id() : 0));
        }
        return bytes;
    }

    public static PlayerState fromBytes(byte[] bytes) {
        int playerId = bytes[0];
        int health = bytes[1];
        byte[] entityBytes = new byte[5];
        System.arraycopy(bytes, 2, entityBytes, 0, 5);
        DynamicEntity dynamicEntity = DynamicEntity.fromBytes(entityBytes);
        Item[] inventory = new Item[(bytes.length - 7) * 2];
        for (int i = 0; i < inventory.length; i += 2) {
            inventory[i] = new Item((bytes[7 + i / 2] & 0xF0) >> 4);
            if (i + 1 < inventory.length) {
                inventory[i + 1] = new Item(bytes[7 + i / 2] & 0x0F);
            }
        }
        return new PlayerState(playerId, health, inventory, dynamicEntity);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Player ID: ").append(playerId).append("\n");
        s.append("Health: ").append(health).append("\n");
        s.append("Dynamic entity: ").append(dynamicEntity).append("\n");
        s.append("Inventory: ");
        for (Item item : inventory) {
            s.append(item).append(" ");
        }
        return s.toString();
    }
}
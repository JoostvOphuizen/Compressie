package gamestate;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final List<PlayerState> playerStates;
    private int seed;

    public GameState(int seed) {
        this.seed = seed;
        this.playerStates = new ArrayList<>();
    }

    public void addPlayerState(PlayerState playerState) {
        this.playerStates.add(playerState);
    }

    public byte[] toBytes() {
        int totalSize = 4; // size for seed
        for (PlayerState playerState : playerStates) {
            totalSize += playerState.toBytes().length;
        }
        byte[] bytes = new byte[totalSize];
        // Convert seed to bytes
        bytes[0] = (byte) (seed >> 24);
        bytes[1] = (byte) (seed >> 16);
        bytes[2] = (byte) (seed >> 8);
        bytes[3] = (byte) seed;
        // Convert player states to bytes
        int index = 4;
        for (PlayerState playerState : playerStates) {
            byte[] playerBytes = playerState.toBytes();
            System.arraycopy(playerBytes, 0, bytes, index, playerBytes.length);
            index += playerBytes.length;
        }
        return bytes;
    }

    public static GameState fromBytes(byte[] bytes) {
        GameState state = new GameState(((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF));
        // Convert bytes to player states
        int index = 4;
        while (index < bytes.length) {
            int playerId = bytes[index];
            int health = bytes[index + 1];
            byte[] entityBytes = new byte[5];
            System.arraycopy(bytes, index + 2, entityBytes, 0, 5);
            DynamicEntity dynamicEntity = DynamicEntity.fromBytes(entityBytes);
            int inventorySize = (bytes.length - index - 7) * 2;
            Item[] inventory = new Item[inventorySize];
            for (int i = 0; i < inventorySize; i += 2) {
                inventory[i] = new Item((bytes[index + 7 + i / 2] & 0xF0) >> 4);
                if (i + 1 < inventorySize) {
                    inventory[i + 1] = new Item(bytes[index + 7 + i / 2] & 0x0F);
                }
            }
            PlayerState playerState = new PlayerState(playerId, health, inventory, dynamicEntity);
            state.addPlayerState(playerState);
            index += 7 + (inventorySize + 1) / 2;
        }
        return state;
    }

    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public int getSeed() {
        return seed;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Seed: ").append(seed).append("\n");
        for (PlayerState playerState : playerStates) {
            s.append(playerState).append("\n");
        }
        return s.toString();
    }
}
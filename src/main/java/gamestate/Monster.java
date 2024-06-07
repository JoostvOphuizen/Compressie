package gamestate;

import bitpacker.PosEntity;

import java.util.BitSet;

public class Monster extends PosEntity {
    private final int monsterId;
    private final int healthPoints;

    public Monster(int x, int y, int monsterId, int healthPoints) {
        super(x, y);
        this.monsterId = monsterId;
        this.healthPoints = healthPoints;
    }

    public byte[] toBytes() {
        byte[] posBytes = super.toBytes();
        byte[] bytes = new byte[5 + posBytes.length];
        System.arraycopy(posBytes, 0, bytes, 0, posBytes.length);
        bytes[posBytes.length] = (byte) monsterId;
        bytes[posBytes.length + 1] = (byte) healthPoints;
        return bytes;
    }

    public static Monster fromBytes(byte[] bytes) {
        int x = ((bytes[0] & 0xFF) << 4) | ((bytes[1] & 0xFF) >> 4);
        int y = ((bytes[1] & 0x0F) << 8) | (bytes[2] & 0xFF);
        int monsterId = bytes[3];
        int healthPoints = bytes[4];
        return new Monster(x, y, monsterId, healthPoints);
    }

    public String toString() {
        return "Monster ID: " + monsterId + "\n" +
                "Health: " + healthPoints + "\n" +
                super.toString() + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Monster) obj;
        return this.x == that.x &&
               this.y == that.y &&
               this.monsterId == that.monsterId &&
               this.healthPoints == that.healthPoints;
    }

}

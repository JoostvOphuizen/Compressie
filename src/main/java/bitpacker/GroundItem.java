package bitpacker;

public class GroundItem extends PosEntity{
    private final int itemId;

    public GroundItem(int x, int y, int itemId) {
        super(x, y);
        this.itemId = itemId;
    }

    public byte[] toBytes() {
        byte[] posBytes = super.toBytes();
        byte[] bytes = new byte[4 + posBytes.length];
        System.arraycopy(posBytes, 0, bytes, 0, posBytes.length);
        bytes[posBytes.length] = (byte) itemId;
        return bytes;
    }

    public static GroundItem fromBytes(byte[] bytes) {
        int x = ((bytes[0] & 0xFF) << 4) | ((bytes[1] & 0xFF) >> 4);
        int y = ((bytes[1] & 0x0F) << 8) | (bytes[2] & 0xFF);
        int itemId = bytes[3];
        return new GroundItem(x, y, itemId);
    }

    public String toString() {
        return "Item ID: " + itemId + "\n" +
                super.toString() + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GroundItem) obj;
        return this.x == that.x &&
               this.y == that.y &&
               this.itemId == that.itemId;
    }

    public int getItemId() {
        return itemId;
    }
}

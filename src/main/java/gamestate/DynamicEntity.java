package gamestate;

public record DynamicEntity(
        int posX,
        int posY,
        int posZ
) {

    public DynamicEntity(int posX, int posY, int posZ) {
        this.posX = posX & 0xFFF; // Force into 12 bits
        this.posY = posY & 0xFFF; // Force into 12 bits
        this.posZ = posZ & 0xFFF; // Force into 12 bits
    }

    public byte[] toBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) (posX >> 4);
        bytes[1] = (byte) ((posX & 0xF) << 4 | (posY >> 8));
        bytes[2] = (byte) posY;
        bytes[3] = (byte) (posZ >> 4);
        bytes[4] = (byte) ((posZ & 0xF) << 4);
        return bytes;
    }

    public static DynamicEntity fromBytes(byte[] bytes) {
        int posX = ((bytes[0] & 0xFF) << 4) | ((bytes[1] & 0xF0) >> 4);
        int posY = ((bytes[1] & 0x0F) << 8) | (bytes[2] & 0xFF);
        int posZ = ((bytes[3] & 0xFF) << 4) | ((bytes[4] & 0xF0) >> 4);
        return new DynamicEntity(posX, posY, posZ);
    }

    public String toString() {
        return "CompressedDynamicEntity{posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + "}";
    }
}

package bitpacker;

public abstract class PosEntity {
    protected int x;
    protected int y;

    public PosEntity(int x, int y){
        if(x < 0 || x > 4095 || y < 0 || y > 4095){
            throw new IllegalArgumentException("x and y must be between 0 and 4095");
        }

        this.x = x;
        this.y = y;
    }

    /**
     * Converts the entity to a byte array.
     *
     * @return The byte array.
     */
    public byte[] toBytes(){
        byte[] bytes = new byte[3];
        bytes[0] = (byte) (x >> 4);
        bytes[1] = (byte) (((x & 0xF) << 4) | (y >> 8));
        bytes[2] = (byte) (y & 0xFF);
        return bytes;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString(){
        return " x: " + x + " y: " + y;
    }

}
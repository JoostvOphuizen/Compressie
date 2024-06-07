package gamestate;

import bitpacker.PosEntity;
import gamestate.compressible.Compressible;

import java.util.BitSet;
import java.util.Objects;

public final class Item extends PosEntity implements Compressible {
    private final int id;


    public Item(int id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    public Item(int id) {
        super(0,0);
        this.id = id;
    }

    public static Item fromByte(byte b) {
        int id = b & 0xFF;
        return new Item(id);
    }

    public byte toByte() {
        return (byte) id;
    }


    public String toString() {
        return "Item{id=" + id + "}";
    }

    public int id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Item) obj;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public byte[] toBytes() {
        return new byte[]{(byte) id};
    }
}
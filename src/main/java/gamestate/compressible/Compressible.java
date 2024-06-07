package gamestate.compressible;

public interface Compressible {
    byte[] toBytes();
    static Compressible fromBytes(byte[] bytes) {
        return null;
    }
}
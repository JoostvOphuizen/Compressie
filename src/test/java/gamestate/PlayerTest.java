package gamestate;

import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    public void testUnpackPlayer(){
        // Create a player
        Item[] items = {new Item(13), new Item(4)};
        Player player = new Player(0, 500, 4000, 44, 10, 99, items);

        // Pack the player
        byte[] bytes = player.toBytes();

        // Unpack the player
        Player unpackedPlayer = Player.fromBytes(bytes);

        // Check if the unpacked player is the same as the original player
        assert(player.equals(unpackedPlayer));
    }
}

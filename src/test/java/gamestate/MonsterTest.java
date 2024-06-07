package gamestate;

import org.junit.jupiter.api.Test;

public class MonsterTest {

    @Test
    public void testUnpackMonster(){
        // Create a monster
        Monster monster = new Monster(0, 500, 15, 44);

        // Pack the monster
        byte[] bytes = monster.toBytes();

        // Unpack the monster
        Monster unpackedMonster = Monster.fromBytes(bytes);

        // Check if the unpacked monster is the same as the original monster
        assert(monster.equals(unpackedMonster));
    }
}

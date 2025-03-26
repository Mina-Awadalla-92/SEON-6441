package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdvanceMoveTest {

    @Test
    void testExecute_MoveToOwnedTerritory() {
        Player player = new Player("Player1");
        Territory source = new Territory("Source", "Continent1", 5);
        Territory target = new Territory("Target", "Continent1", 5);

        source.setOwner(player);
        target.setOwner(player);

        source.setNumOfArmies(10);
        target.setNumOfArmies(5);

        player.addTerritory(source);
        player.addTerritory(target);

        AdvanceMove advanceMove = new AdvanceMove(player, source, target, 5);
        advanceMove.execute();

        assertEquals(5, source.getNumOfArmies());
        assertEquals(10, target.getNumOfArmies());
        assertEquals(player, target.getOwner());
    }

    @Test
    void testExecute_MoveToNeutralTerritory() {
        Player player = new Player("Player1");
        Territory source = new Territory("Source", "Continent1", 5);
        Territory target = new Territory("Neutral", "Continent1", 5);

        source.setOwner(player);
        target.setOwner(null);

        source.setNumOfArmies(10);
        target.setNumOfArmies(0);

        player.addTerritory(source);

        AdvanceMove advanceMove = new AdvanceMove(player, source, target, 5);
        advanceMove.execute();

        assertEquals(5, source.getNumOfArmies());
        assertEquals(5, target.getNumOfArmies());
        assertEquals(player, target.getOwner());
        assertTrue(player.getOwnedTerritories().contains(target));
    }

    @Test
    void testConstructor() {
        Player player = new Player("Player1");
        Territory source = new Territory("Source", "Continent1", 5);
        Territory target = new Territory("Target", "Continent1", 5);

        AdvanceMove advanceMove = new AdvanceMove(player, source, target, 10);

        assertEquals(player, advanceMove.getIssuer());
        assertEquals(source, advanceMove.getD_territoryFrom());
        assertEquals(target, advanceMove.getD_territoryTo());
        assertEquals(10, advanceMove.getD_numberOfArmies());
    }

    @Test
    void testCopyConstructor() {
        Player player = new Player("Player1");
        Territory source = new Territory("Source", "Continent1", 5);
        Territory target = new Territory("Target", "Continent1", 5);

        AdvanceMove original = new AdvanceMove(player, source, target, 10);
        AdvanceMove copy = new AdvanceMove(original);

        assertEquals(original.getIssuer(), copy.getIssuer());
        assertEquals(original.getD_territoryFrom(), copy.getD_territoryFrom());
        assertEquals(original.getD_territoryTo(), copy.getD_territoryTo());
        assertEquals(original.getD_numberOfArmies(), copy.getD_numberOfArmies());
    }
}
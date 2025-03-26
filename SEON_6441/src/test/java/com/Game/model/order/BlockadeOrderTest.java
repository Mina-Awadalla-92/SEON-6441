package com.Game.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.Game.model.Player;
import com.Game.model.Territory;

public class BlockadeOrderTest {

    @Test
    void testExecute_BlockadeSuccess() {
        Player player = new Player("Player1");
        Territory territory = new Territory("Territory1", "Continent1", 5);

        territory.setOwner(player);
        territory.setNumOfArmies(10);
        player.addTerritory(territory);

        BlockadeOrder blockadeOrder = new BlockadeOrder(player, territory);
        blockadeOrder.execute();

        assertNull(territory.getOwner(), "Territory owner should be null after blockade.");
        assertEquals(30, territory.getNumOfArmies(), "Number of armies should be tripled after blockade.");
        assertFalse(player.getOwnedTerritories().contains(territory), "Territory should no longer belong to the player.");
    }

    @Test
    void testConstructorWithParameters() {
        Player player = new Player("Player1");
        Territory territory = new Territory("Territory1", "Continent1", 5);

        BlockadeOrder blockadeOrder = new BlockadeOrder(player, territory);

        assertEquals(player, blockadeOrder.getIssuer(), "Issuer should match the player.");
        assertEquals(territory, blockadeOrder.getTerritoryTo(), "Target territory should match the provided territory.");
    }

    @Test
    void testCopyConstructor() {
        Player player = new Player("Player1");
        Territory territory = new Territory("Territory1", "Continent1", 5);

        BlockadeOrder original = new BlockadeOrder(player, territory);
        BlockadeOrder copy = new BlockadeOrder(original);

        assertEquals(original.getIssuer(), copy.getIssuer(), "Issuer should match the original order.");
        assertEquals(original.getTerritoryTo(), copy.getTerritoryTo(), "Target territory should match the original order.");
    }

    @Test
    void testSetAndGetTerritoryTo() {
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent2", 10);

        BlockadeOrder blockadeOrder = new BlockadeOrder();
        blockadeOrder.setTerritoryTo(territory1);

        assertEquals(territory1, blockadeOrder.getTerritoryTo(), "Territory should match the set value.");

        blockadeOrder.setTerritoryTo(territory2);
        assertEquals(territory2, blockadeOrder.getTerritoryTo(), "Territory should match the updated value.");
    }
}
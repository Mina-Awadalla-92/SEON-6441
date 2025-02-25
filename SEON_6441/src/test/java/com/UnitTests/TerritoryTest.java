package com.UnitTests;

import com.Game.Player;
import com.Game.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryTest {
    private Territory territory;
    private Territory neighbor1;
    private Territory neighbor2;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");

        territory = new Territory("TerritoryA", "ContinentX", 5);
        neighbor1 = new Territory("TerritoryB", "ContinentX", 3);
        neighbor2 = new Territory("TerritoryC", "ContinentY", 4);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("TerritoryA", territory.getName());
        assertEquals("ContinentX", territory.getContinent());
        assertEquals(5, territory.getBonus());
        assertEquals(0, territory.getNumOfArmies());
        assertNull(territory.getOwner());
    }

    @Test
    void testSetAndGetNumOfArmies() {
        territory.setNumOfArmies(10);
        assertEquals(10, territory.getNumOfArmies());
    }

    @Test
    void testSetAndGetOwner() {
        territory.setOwner(player1);
        assertEquals(player1, territory.getOwner());
    }

    @Test
    void testAddNeighborAndHasNeighbor() {
        territory.addNeighbor(neighbor1);
        territory.addNeighbor(neighbor2);

        assertTrue(territory.hasNeighbor(neighbor1));
        assertTrue(territory.hasNeighbor(neighbor2));
        assertFalse(territory.hasNeighbor(new Territory("TerritoryD", "ContinentZ", 2)));
    }

    @Test
    void testGetEnemyNeighbors() {
        territory.setOwner(player1);
        neighbor1.setOwner(player1);
        neighbor2.setOwner(player2);

        territory.addNeighbor(neighbor1);
        territory.addNeighbor(neighbor2);

        List<Territory> enemyNeighbors = territory.getEnemyNeighbors();
        assertEquals(1, enemyNeighbors.size());
        assertTrue(enemyNeighbors.contains(neighbor2));
    }

    @Test
    void testEquals() {
        Territory sameTerritory = new Territory("TerritoryA", "ContinentX", 5);
        Territory differentTerritory = new Territory("TerritoryD", "ContinentZ", 2);

        assertEquals(territory, sameTerritory);
        assertNotEquals(territory, differentTerritory);
    }
}
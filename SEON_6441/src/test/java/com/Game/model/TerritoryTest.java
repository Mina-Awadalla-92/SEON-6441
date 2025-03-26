package com.Game.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  This test class covers the main methods of the Territory class, including its getters, setters, and utility 
 *  methods like addNeighbor, hasNeighbor, getEnemyNeighbors, and equals.
 */

class TerritoryTest {

    private Territory territory;
    private Territory neighbor1;
    private Territory neighbor2;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        // Create real Player objects
        player1 = new Player("Player1");
        player2 = new Player("Player2");

        // Create Territory objects
        territory = new Territory("Territory1", "Continent1", 5);
        neighbor1 = new Territory("Neighbor1", "Continent1", 3);
        neighbor2 = new Territory("Neighbor2", "Continent2", 2);

        // Set ownership
        territory.setOwner(player1);
        neighbor1.setOwner(player1);
        neighbor2.setOwner(player2);
    }

    @Test
    void testGetEnemyNeighbors() {
        territory.addNeighbor(neighbor1);
        territory.addNeighbor(neighbor2);

        List<Territory> enemyNeighbors = territory.getEnemyNeighbors();
        assertEquals(1, enemyNeighbors.size(), "There should be 1 enemy neighbor.");
        assertTrue(enemyNeighbors.contains(neighbor2), "Neighbor2 should be an enemy neighbor.");
    }

    @Test
    void testGetEnemyNeighbors_NoNeighbors() {
        List<Territory> enemyNeighbors = territory.getEnemyNeighbors();
        assertTrue(enemyNeighbors.isEmpty(), "A territory with no neighbors should have no enemy neighbors.");
    }

    @Test
    void testConstructor() {
        Territory territory = new Territory("Territory1", "Continent1", 5);
        assertEquals("Territory1", territory.getName());
        assertEquals("Continent1", territory.getContinent());
        assertEquals(5, territory.getBonus());
        assertEquals(0, territory.getNumOfArmies());
        assertNull(territory.getOwner());
        assertTrue(territory.getNeighborList().isEmpty());
    }

    @Test
    void testSetAndGetNumOfArmies() {
        territory.setNumOfArmies(10);
        assertEquals(10, territory.getNumOfArmies());
    }

    @Test
    void testSetNegativeNumOfArmies() {
        assertThrows(IllegalArgumentException.class, () -> territory.setNumOfArmies(-5), "Setting a negative number of armies should throw an exception.");
    }

    @Test
    void testSetAndGetOwner() {
        territory.setOwner(player1);
        assertEquals(player1, territory.getOwner());
    }

    @Test
    void testSetNullOwner() {
        territory.setOwner(null);
        assertNull(territory.getOwner(), "Setting a null owner should be allowed.");
    }

    @Test
    void testAddAndHasNeighbor() {
        territory.addNeighbor(neighbor1);
        assertTrue(territory.hasNeighbor(neighbor1));
        assertFalse(territory.hasNeighbor(neighbor2));
    }

    @Test
    void testAddSameNeighborMultipleTimes() {
        territory.addNeighbor(neighbor1);
        territory.addNeighbor(neighbor1);
        List<Territory> neighbors = territory.getNeighborList();
        assertEquals(1, neighbors.size(), "Adding the same neighbor multiple times should not create duplicates.");
    }

    @Test
    void testAddNullNeighbor() {
        assertThrows(NullPointerException.class, () -> territory.addNeighbor(null), "Adding a null neighbor should throw an exception.");
    }

    @Test
    void testGetNeighborList() {
        territory.addNeighbor(neighbor1);
        territory.addNeighbor(neighbor2);
        List<Territory> neighbors = territory.getNeighborList();
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(neighbor1));
        assertTrue(neighbors.contains(neighbor2));
    }

    @Test
    void testEquals() {
        Territory sameTerritory = new Territory("Territory1", "Continent1", 5);
        Territory differentTerritory = new Territory("Territory2", "Continent1", 5);
        assertTrue(territory.equals(sameTerritory));
        assertFalse(territory.equals(differentTerritory));
    }

    @Test
    void testToString() {
        territory.addNeighbor(neighbor1);
        String result = territory.toString();
        assertTrue(result.contains("Territory Name: Territory1"));
        assertTrue(result.contains("Continent: Continent1"));
        assertTrue(result.contains("Neighbors: Neighbor1"));
    }
}
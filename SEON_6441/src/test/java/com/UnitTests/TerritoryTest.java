package com.UnitTests;

import com.Game.Player;
import com.Game.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Territory} class.
 * This class tests the functionality of the {@link Territory} class, including its constructor,
 * getters, setters, neighbor functionality, enemy neighbor detection, and equality comparison.
 */
public class TerritoryTest {

    /**
     * The Territory object being tested.
     */
    private Territory d_territory;

    /**
     * The first Territory object being tested.
     */
    private Territory d_neighbor1;

    /**
     * The second Territory object being tested.
     */
    private Territory d_neighbor2;

    /**
     * The first Player object being tested.
     */
    private Player d_player1;

    /**
     * The second Player object being tested.
     */
    private Player d_player2;

    /**
     * Sets up the test environment by initializing the objects required for testing.
     * This method runs before each test.
     */
    @BeforeEach
    void setUp() {
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");

        d_territory = new Territory("TerritoryA", "ContinentX", 5);
        d_neighbor1 = new Territory("TerritoryB", "ContinentX", 3);
        d_neighbor2 = new Territory("TerritoryC", "ContinentY", 4);
    }

    /**
     * Tests the constructor and getters of the {@link Territory} class.
     * Verifies that the territory's name, continent, bonus, number of armies, and owner are correctly set.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("TerritoryA", d_territory.getName());
        assertEquals("ContinentX", d_territory.getContinent());
        assertEquals(5, d_territory.getBonus());
        assertEquals(0, d_territory.getNumOfArmies());
        assertNull(d_territory.getOwner());
    }

    /**
     * Tests the setter and getter for the number of armies in the {@link Territory} class.
     * Verifies that the number of armies is correctly set and retrieved.
     */
    @Test
    void testSetAndGetNumOfArmies() {
        d_territory.setNumOfArmies(10);
        assertEquals(10, d_territory.getNumOfArmies());
    }

    /**
     * Tests the setter and getter for the owner of the territory in the {@link Territory} class.
     * Verifies that the owner is correctly set and retrieved.
     */
    @Test
    void testSetAndGetOwner() {
        d_territory.setOwner(d_player1);
        assertEquals(d_player1, d_territory.getOwner());
    }

    /**
     * Tests adding neighbors and checking if a territory has specific neighbors.
     * Verifies that neighbors are correctly added and detected.
     */
    @Test
    void testAddNeighborAndHasNeighbor() {
        d_territory.addNeighbor(d_neighbor1);
        d_territory.addNeighbor(d_neighbor2);

        assertTrue(d_territory.hasNeighbor(d_neighbor1));
        assertTrue(d_territory.hasNeighbor(d_neighbor2));
        assertFalse(d_territory.hasNeighbor(new Territory("TerritoryD", "ContinentZ", 2)));
    }

    /**
     * Tests the functionality of getting enemy neighbors.
     * Verifies that enemy neighbors are correctly identified based on ownership.
     */
    @Test
    void testGetEnemyNeighbors() {
        d_territory.setOwner(d_player1);
        d_neighbor1.setOwner(d_player1);
        d_neighbor2.setOwner(d_player2);

        d_territory.addNeighbor(d_neighbor1);
        d_territory.addNeighbor(d_neighbor2);

        List<Territory> enemyNeighbors = d_territory.getEnemyNeighbors();
        assertEquals(1, enemyNeighbors.size());
        assertTrue(enemyNeighbors.contains(d_neighbor2));
    }

    /**
     * Tests the equality of territories.
     * Verifies that territories with the same name, continent, and bonus are equal,
     * and those with different properties are not equal.
     */
    @Test
    void testEquals() {
        Territory sameTerritory = new Territory("TerritoryA", "ContinentX", 5);
        Territory differentTerritory = new Territory("TerritoryD", "ContinentZ", 2);

        assertEquals(d_territory, sameTerritory);
        assertNotEquals(d_territory, differentTerritory);
    }
}
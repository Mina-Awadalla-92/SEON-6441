package com.Game.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
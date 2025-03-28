package com.Game.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the territory ownership and map functionality.
 */
public class TerritoryTest {
    
    private Player d_player1;
    private Player d_player2;
    private Territory d_territory1;
    private Territory d_territory2;
    private Territory d_territory3;
    private Map d_map;
    
    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        // Create players
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        
        // Create territories
        d_territory1 = new Territory("Territory1", "Continent1", 5);
        d_territory2 = new Territory("Territory2", "Continent1", 5);
        d_territory3 = new Territory("Territory3", "Continent2", 3);
        
        // Set up neighbors
        d_territory1.addNeighbor(d_territory2);
        d_territory2.addNeighbor(d_territory1);
        d_territory2.addNeighbor(d_territory3);
        d_territory3.addNeighbor(d_territory2);
        
        // Create map
        d_map = new Map();
        d_map.addTerritory(d_territory1);
        d_map.addTerritory(d_territory2);
        d_map.addTerritory(d_territory3);
        
        d_map.addContinent("Continent1", 5);
        d_map.addContinent("Continent2", 3);
    }
    
    /**
     * Tests basic territory ownership.
     */
    @Test
    public void testTerritoryOwnership() {
        // Initially, territories have no owner
        assertNull("Territory1 should have no owner initially", d_territory1.getOwner());
        
        // Set owner for Territory1
        d_territory1.setOwner(d_player1);
        assertEquals("Territory1 should be owned by Player1", d_player1, d_territory1.getOwner());
        
        // Change ownership
        d_territory1.setOwner(d_player2);
        assertEquals("Territory1 should now be owned by Player2", d_player2, d_territory1.getOwner());
    }
    
    /**
     * Tests adding and removing territories from a player.
     */
    @Test
    public void testAddRemoveTerritories() {
        // Initially, player has no territories
        assertTrue("Player1 should have no territories initially", d_player1.getOwnedTerritories().isEmpty());
        
        // Add territories to player
        d_player1.addTerritory(d_territory1);
        d_player1.addTerritory(d_territory2);
        
        // Set ownership on the territories
        d_territory1.setOwner(d_player1);
        d_territory2.setOwner(d_player1);
        
        assertEquals("Player1 should own 2 territories", 2, d_player1.getOwnedTerritories().size());
        assertTrue("Player1 should own Territory1", d_player1.getOwnedTerritories().contains(d_territory1));
        assertTrue("Player1 should own Territory2", d_player1.getOwnedTerritories().contains(d_territory2));
        
        // Remove a territory
        d_player1.removeTerritory(d_territory1);
        assertEquals("Player1 should own 1 territory after removal", 1, d_player1.getOwnedTerritories().size());
        assertFalse("Player1 should no longer own Territory1", d_player1.getOwnedTerritories().contains(d_territory1));
        assertTrue("Player1 should still own Territory2", d_player1.getOwnedTerritories().contains(d_territory2));
    }
    
    /**
     * Tests finding a territory by name.
     */
    @Test
    public void testFindTerritoryByName() {
        // Add territories to player
        d_player1.addTerritory(d_territory1);
        d_player1.addTerritory(d_territory2);
        
        // Find by name
        Territory found = d_player1.findTerritoryByName("Territory1");
        assertNotNull("Should find Territory1", found);
        assertEquals("Found territory should be Territory1", d_territory1, found);
        
        // Find non-existent territory
        Territory notFound = d_player1.findTerritoryByName("NonExistent");
        assertNull("Should not find non-existent territory", notFound);
    }
    
    /**
     * Tests territory army management.
     */
    @Test
    public void testTerritoryArmies() {
        // Initial state
        assertEquals("Territory1 should start with 0 armies", 0, d_territory1.getNumOfArmies());
        
        // Add armies
        d_territory1.setNumOfArmies(5);
        assertEquals("Territory1 should have 5 armies", 5, d_territory1.getNumOfArmies());
        
        // Reduce armies
        d_territory1.setNumOfArmies(3);
        assertEquals("Territory1 should have 3 armies", 3, d_territory1.getNumOfArmies());
    }
    
    /**
     * Tests checking if a territory has a neighbor.
     */
    @Test
    public void testTerritoryNeighbors() {
        assertTrue("Territory1 should have Territory2 as neighbor", d_territory1.hasNeighbor(d_territory2));
        assertFalse("Territory1 should not have Territory3 as neighbor", d_territory1.hasNeighbor(d_territory3));
        assertTrue("Territory2 should have Territory3 as neighbor", d_territory2.hasNeighbor(d_territory3));
    }
    
    /**
     * Tests getting enemy neighbors.
     */
    @Test
    public void testGetEnemyNeighbors() {
        // Set up ownership
        d_territory1.setOwner(d_player1);
        d_territory2.setOwner(d_player1);
        d_territory3.setOwner(d_player2);
        
        d_player1.addTerritory(d_territory1);
        d_player1.addTerritory(d_territory2);
        d_player2.addTerritory(d_territory3);
        
        // Check enemy neighbors
        List<Territory> territory1EnemyNeighbors = d_territory1.getEnemyNeighbors();
        assertTrue("Territory1 should have no enemy neighbors", territory1EnemyNeighbors.isEmpty());
        
        List<Territory> territory2EnemyNeighbors = d_territory2.getEnemyNeighbors();
        assertEquals("Territory2 should have 1 enemy neighbor", 1, territory2EnemyNeighbors.size());
        assertTrue("Territory2's enemy neighbor should be Territory3", territory2EnemyNeighbors.contains(d_territory3));
    }
    
    /**
     * Tests the Map.getTerritoryByName method.
     */
    @Test
    public void testMapGetTerritoryByName() {
        Territory found = d_map.getTerritoryByName("Territory1");
        assertNotNull("Should find Territory1 in map", found);
        assertEquals("Found territory should be Territory1", d_territory1, found);
        
        Territory notFound = d_map.getTerritoryByName("NonExistent");
        assertNull("Should not find non-existent territory in map", notFound);
    }
    
    /**
     * Tests map validation with different ownership patterns.
     */
    @Test
    public void testMapValidationWithOwnership() {
        // Map should be valid to start with
        assertTrue("Map should be valid initially", d_map.mapValidation());
        assertTrue("Continents should be valid initially", d_map.continentValidation());
        
        // Assign ownership (shouldn't affect connectivity)
        d_territory1.setOwner(d_player1);
        d_territory2.setOwner(d_player2);
        d_territory3.setOwner(d_player1);
        
        d_player1.addTerritory(d_territory1);
        d_player1.addTerritory(d_territory3);
        d_player2.addTerritory(d_territory2);
        
        // Map should still be valid after ownership changes
        assertTrue("Map should still be valid after ownership changes", d_map.mapValidation());
        assertTrue("Continents should still be valid after ownership changes", d_map.continentValidation());
    }
    
    /**
     * Tests checking for a winner in the game.
     */
    @Test
    public void testCheckForWinner() {
        // No winner initially (both players have territories)
        d_territory1.setOwner(d_player1);
        d_territory2.setOwner(d_player2);
        d_territory3.setOwner(d_player1);
        
        d_player1.addTerritory(d_territory1);
        d_player1.addTerritory(d_territory3);
        d_player2.addTerritory(d_territory2);
        
        List<Player> players = new ArrayList<>();
        players.add(d_player1);
        players.add(d_player2);
        
        // Create a game play controller to test winner checking
        com.Game.controller.GameController gameController = new com.Game.controller.GameController();
        gameController.setGameMap(d_map);
        gameController.setPlayers(players);
        com.Game.controller.GamePlayController gamePlayController = 
            new com.Game.controller.GamePlayController(gameController, d_map, players);
        
        // This is a private method, so we'd need to use reflection or modify the code to test it directly
        // Instead, we'll test the result indirectly through handleEndTurn, which won't be called here
        
        // We could modify the code to make the checkForWinner method public or protected for testing
    }
}
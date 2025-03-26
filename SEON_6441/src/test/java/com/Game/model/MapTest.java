package com.Game.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapTest {

    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
    }

    @Test
    void testAddAndGetTerritory() {
        Territory territory = new Territory("Territory1", "Continent1", 5);
        map.addTerritory(territory);
        Territory retrievedTerritory = map.getTerritoryByName("Territory1");
        assertNotNull(retrievedTerritory, "Territory should be added to the map.");
        assertEquals("Territory1", retrievedTerritory.getName(), "Territory name should match.");
    }

    @Test
    void testAddAndRemoveContinent() {
        map.addContinent("Continent1", 10);
        assertTrue(map.getContinents().containsKey("Continent1"), "Continent should be added to the map.");
        map.removeContinent("Continent1");
        assertFalse(map.getContinents().containsKey("Continent1"), "Continent should be removed from the map.");
    }

    @Test
    void testAddAndRemoveCountry() {
        map.addContinent("Continent1", 10);
        map.addCountry("Country1", "Continent1");
        Territory country = map.getTerritoryByName("Country1");
        assertNotNull(country, "Country should be added to the map.");
        assertEquals("Country1", country.getName(), "Country name should match.");
        assertEquals("Continent1", country.getContinent(), "Country should belong to the correct continent.");
        map.removeCountry("Country1");
        assertNull(map.getTerritoryByName("Country1"), "Country should be removed from the map.");
    }

    @Test
    void testAddAndRemoveNeighbor() {
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent1", 5);
        map.addTerritory(territory1);
        map.addTerritory(territory2);
        map.addNeighbor("Territory1", "Territory2");
        assertTrue(territory1.getNeighborList().contains(territory2), "Territory1 should have Territory2 as a neighbor.");
        assertTrue(territory2.getNeighborList().contains(territory1), "Territory2 should have Territory1 as a neighbor.");
        map.removeNeighbor("Territory1", "Territory2");
        assertFalse(territory1.getNeighborList().contains(territory2), "Territory1 should no longer have Territory2 as a neighbor.");
        assertFalse(territory2.getNeighborList().contains(territory1), "Territory2 should no longer have Territory1 as a neighbor.");
    }

    @Test
    void testMapValidation_ConnectedGraph() {
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent1", 5);
        Territory territory3 = new Territory("Territory3", "Continent1", 5);
        map.addTerritory(territory1);
        map.addTerritory(territory2);
        map.addTerritory(territory3);
        map.addNeighbor("Territory1", "Territory2");
        map.addNeighbor("Territory2", "Territory3");
        boolean isValid = map.mapValidation();
        assertTrue(isValid, "The map should be a connected graph.");
    }

    @Test
    void testMapValidation_DisconnectedGraph() {
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent1", 5);
        map.addTerritory(territory1);
        map.addTerritory(territory2);
        boolean isValid = map.mapValidation();
        assertFalse(isValid, "The map should not be a connected graph.");
    }

    @Test
    void testContinentValidation_ConnectedSubgraph() {
        map.addContinent("Continent1", 10);
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent1", 5);
        map.addTerritory(territory1);
        map.addTerritory(territory2);
        map.addNeighbor("Territory1", "Territory2");
        boolean isValid = map.continentValidation();
        assertTrue(isValid, "The continent should be a connected subgraph.");
    }

    @Test
    void testContinentValidation_DisconnectedSubgraph() {
        map.addContinent("Continent1", 10);
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent1", 5);
        map.addTerritory(territory1);
        map.addTerritory(territory2);
        boolean isValid = map.continentValidation();
        assertFalse(isValid, "The continent should not be a connected subgraph.");
    }

    @Test
    void testMapValidation_EmptyMap() {
        boolean isValid = map.mapValidation();
        assertFalse(isValid, "An empty map should not be a connected graph.");
    }

    @Test
    void testMapValidation_SingleTerritory() {
        Territory territory = new Territory("Territory1", "Continent1", 5);
        map.addTerritory(territory);
        boolean isValid = map.mapValidation();
        assertTrue(isValid, "A map with a single territory should be a connected graph.");
    }

    @Test
    void testAddDuplicateTerritory() {
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory1", "Continent2", 10);
        map.addTerritory(territory1);
        map.addTerritory(territory2);
        Territory retrievedTerritory = map.getTerritoryByName("Territory1");
        assertEquals("Continent1", retrievedTerritory.getContinent(), "The original territory should not be overwritten.");
    }

    @Test
    void testAddNeighborToNonExistentTerritory() {
        assertThrows(IllegalArgumentException.class, () -> map.addNeighbor("NonExistent", "Territory1"),
            "Adding a neighbor to a non-existent territory should throw an exception.");
    }

    @Test
    void testRemoveNonExistentCountry() {
        map.removeCountry("NonExistent");
        assertNull(map.getTerritoryByName("NonExistent"), "Removing a non-existent country should not cause errors.");
    }
}
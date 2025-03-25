package com.Game.model;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class covers the core functionality of Map (adding/removing territories and continents, * *  
 * neighbors, saving to file, and validation). Adjust or expand tests as needed for your project.
 */

public class MapTest {

    private Map map;

    @BeforeEach
    void setUp() {
        map = new Map();
    }

    @Test
    void testConstructor() {
        assertNotNull(map.getTerritoryList(), "Territory list should be non-null");
        assertTrue(map.getTerritoryList().isEmpty(), "New map should have no territories");
        assertNotNull(map.getContinents(), "Continents map should be non-null");
        assertTrue(map.getContinents().isEmpty(), "New map should have no continents");
    }

    @Test
    void testCopyConstructor() {
        // Prepare original map with one territory and one continent.
        map.addContinent("Europe", 5);
        Territory t = new Territory("France", "Europe", 5);
        map.addTerritory(t);

        Map copy = new Map(map);
        // Check that territory list is copied (but not same instance)
        assertNotSame(map.getTerritoryList(), copy.getTerritoryList());
        assertEquals(1, copy.getTerritoryList().size());
        // Check that the continent mapping is copied
        assertEquals(5, copy.getContinents().get("Europe"));
    }

    @Test
    void testAddAndGetTerritory() {
        Territory t1 = new Territory("Germany", "Europe", 5);
        map.addTerritory(t1);
        Territory found = map.getTerritoryByName("Germany");
        assertNotNull(found, "Should find territory 'Germany'");
        assertEquals("Germany", found.getName());
    }

    @Test
    void testAddTerritory_Duplicate() {
        Territory t1 = new Territory("Spain", "Europe", 5);
        map.addTerritory(t1);
        // Add a duplicate territory (same name)
        Territory t2 = new Territory("Spain", "Europe", 5);
        map.addTerritory(t2);
        // Even though duplicate is added, the flag d_hasUniqueTerritories is set to false.
        // We can't access that flag directly, but we can check that there are two entries.
        assertEquals(2, map.getTerritoryList().size());
    }

    @Test
    void testAddAndRemoveContinent() {
        map.addContinent("Asia", 7);
        assertTrue(map.getContinents().containsKey("Asia"));
        assertEquals(7, map.getContinents().get("Asia"));
        // Add a territory in Asia
        Territory t = new Territory("Japan", "Asia", 7);
        map.addTerritory(t);
        // Remove continent "Asia" should remove the continent and associated territory
        map.removeContinent("Asia");
        assertFalse(map.getContinents().containsKey("Asia"));
        assertNull(map.getTerritoryByName("Japan"), "Territory 'Japan' should be removed when its continent is removed.");
    }

    @Test
    void testAddAndRemoveCountry() {
        // First, add a continent to the map
        map.addContinent("Africa", 4);
        // Add country (territory) using addCountry
        map.addCountry("Egypt", "Africa");
        Territory found = map.getTerritoryByName("Egypt");
        assertNotNull(found, "Country 'Egypt' should be added to the map.");

        // Remove the country
        map.removeCountry("Egypt");
        assertNull(map.getTerritoryByName("Egypt"), "Country 'Egypt' should be removed from the map.");
    }

    @Test
    void testAddAndRemoveNeighbor() {
        // Create two territories manually and add them to the map
        Territory t1 = new Territory("Country1", "ContinentX", 3);
        Territory t2 = new Territory("Country2", "ContinentX", 3);
        map.addTerritory(t1);
        map.addTerritory(t2);
        // Add neighbor relationship
        map.addNeighbor("Country1", "Country2");
        assertTrue(t1.getNeighborList().contains(t2), "Country1 should have Country2 as neighbor");
        assertTrue(t2.getNeighborList().contains(t1), "Country2 should have Country1 as neighbor");
        // Remove neighbor relationship
        map.removeNeighbor("Country1", "Country2");
        assertFalse(t1.getNeighborList().contains(t2), "Country1 should not have Country2 as neighbor after removal");
        assertFalse(t2.getNeighborList().contains(t1), "Country2 should not have Country1 as neighbor after removal");
    }

    @Test
    void testSetContinents() {
        Map<String, Integer> continents = new HashMap<>();
        continents.put("Oceania", 2);
        continents.put("SouthAmerica", 3);
        map.setContinents(continents);
        assertEquals(2, map.getContinents().get("Oceania"));
        assertEquals(3, map.getContinents().get("SouthAmerica"));
    }

    @Test
    void testToString() {
        // Add a couple of territories
        Territory t1 = new Territory("Italy", "Europe", 4);
        Territory t2 = new Territory("Sweden", "Europe", 4);
        map.addTerritory(t1);
        map.addTerritory(t2);
        String mapStr = map.toString();
        assertTrue(mapStr.contains("Italy"), "toString() should include 'Italy'");
        assertTrue(mapStr.contains("Sweden"), "toString() should include 'Sweden'");
    }

    @Test
    void testSaveToFile() throws IOException {
        // Setup: add continent, territories, neighbors.
        map.addContinent("Europe", 5);
        Territory t1 = new Territory("France", "Europe", 5);
        Territory t2 = new Territory("Germany", "Europe", 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addNeighbor("France", "Germany");

        // Create temporary file for testing
        File tempFile = File.createTempFile("mapTest", ".map");
        tempFile.deleteOnExit();

        // Call saveToFile
        map.saveToFile(tempFile.getAbsolutePath());

        // Read file and verify it contains key sections
        String content = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
        assertTrue(content.contains("[continents]"), "Output should contain [continents] section");
        assertTrue(content.contains("[countries]"), "Output should contain [countries] section");
        assertTrue(content.contains("[borders]"), "Output should contain [borders] section");
        assertTrue(content.contains("France"), "Output should contain territory 'France'");
        assertTrue(content.contains("Germany"), "Output should contain territory 'Germany'");
    }

    @Test
    void testMapValidation_ConnectedMap() {
        // Create a connected map:
        // Two territories connected as neighbors.
        Territory t1 = new Territory("A", "X", 1);
        Territory t2 = new Territory("B", "X", 1);
        t1.addNeighbor(t2);
        t2.addNeighbor(t1);
        map.addTerritory(t1);
        map.addTerritory(t2);
        // For a connected map, mapValidation() should return true.
        assertTrue(map.mapValidation(), "Map should be valid if all territories are connected.");
    }

    @Test
    void testMapValidation_DisconnectedMap() {
        // Create a disconnected map:
        Territory t1 = new Territory("A", "X", 1);
        Territory t2 = new Territory("B", "X", 1);
        // Do not connect t1 and t2.
        map.addTerritory(t1);
        map.addTerritory(t2);
        assertFalse(map.mapValidation(), "Map should be invalid if territories are not connected.");
    }

    @Test
    void testContinentValidation_ConnectedContinent() {
        // Create a connected continent:
        map.addContinent("Y", 2);
        Territory t1 = new Territory("C1", "Y", 2);
        Territory t2 = new Territory("C2", "Y", 2);
        t1.addNeighbor(t2);
        t2.addNeighbor(t1);
        map.addTerritory(t1);
        map.addTerritory(t2);
        assertTrue(map.continentValidation(), "Continent should be valid if its territories are connected.");
    }

    @Test
    void testContinentValidation_DisconnectedContinent() {
        // Create a disconnected continent:
        map.addContinent("Z", 3);
        Territory t1 = new Territory("D1", "Z", 3);
        Territory t2 = new Territory("D2", "Z", 3);
        // Do not connect t1 and t2.
        map.addTerritory(t1);
        map.addTerritory(t2);
        assertFalse(map.continentValidation(), "Continent should be invalid if its territories are not connected.");
    }
}

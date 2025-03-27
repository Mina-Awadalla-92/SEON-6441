package com.Game.utils;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.Game.model.Map;
import com.Game.model.Territory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for the MapLoader and map validation functionality.
 */
public class MapValidationTest {
    
    private MapLoader d_mapLoader;
    private String d_validMapFilePath = "test_valid_map.map";
    private String d_invalidMapFilePath = "test_invalid_map.map";
    private String d_disconnectedMapFilePath = "test_disconnected_map.map";
    private String d_disconnectedContinentMapFilePath = "test_disconnected_continent_map.map";
    
    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() throws IOException {
        d_mapLoader = new MapLoader();
        
        // Create test map files
        createValidMapFile();
        createInvalidMapFile();
        createDisconnectedMapFile();
        createDisconnectedContinentMapFile();
    }
    
    /**
     * Tests loading and validating a valid map.
     */
    @Test
    public void testValidMap() {
        // Test map format validation
        assertTrue("Valid map format should be recognized", d_mapLoader.isValid(d_validMapFilePath));
        
        // Test loading the map
        d_mapLoader.read(d_validMapFilePath);
        Map loadedMap = d_mapLoader.getLoadedMap();
        
        // Verify basic map properties
        assertNotNull("Map should be loaded", loadedMap);
        assertEquals("Map should have correct number of territories", 3, loadedMap.getTerritoryList().size());
        assertEquals("Map should have correct number of continents", 2, loadedMap.getContinents().size());
        
        // Test map connectivity validation
        assertTrue("Valid map should pass connectivity validation", loadedMap.mapValidation());
        
        // Test continent connectivity validation
        assertTrue("Valid map should pass continent validation", loadedMap.continentValidation());
        
        // Test overall validation
        assertTrue("Valid map should pass all validations", d_mapLoader.validateMap());
    }
    
    /**
     * Tests that an invalid map format is correctly rejected.
     */
    @Test
    public void testInvalidMapFormat() {
        assertFalse("Invalid map format should be recognized", d_mapLoader.isValid(d_invalidMapFilePath));
    }
    
    /**
     * Tests that a disconnected map is correctly identified.
     */
    @Test
    public void testDisconnectedMap() {
        assertTrue("Map should be recognized as valid format", d_mapLoader.isValid(d_disconnectedMapFilePath));
        
        d_mapLoader.read(d_disconnectedMapFilePath);
        Map loadedMap = d_mapLoader.getLoadedMap();
        
        assertFalse("Disconnected map should fail connectivity validation", loadedMap.mapValidation());
        assertFalse("Disconnected map should fail overall validation", d_mapLoader.validateMap());
    }
    
    /**
     * Tests that a map with disconnected continents is correctly identified.
     */
    @Test
    public void testDisconnectedContinent() {
        assertTrue("Map should be recognized as valid format", d_mapLoader.isValid(d_disconnectedContinentMapFilePath));
        
        d_mapLoader.read(d_disconnectedContinentMapFilePath);
        Map loadedMap = d_mapLoader.getLoadedMap();
        
        assertTrue("Map with disconnected continent should pass overall connectivity", loadedMap.mapValidation());
        assertFalse("Map with disconnected continent should fail continent validation", loadedMap.continentValidation());
        assertFalse("Map with disconnected continent should fail overall validation", d_mapLoader.validateMap());
    }
    
    /**
     * Tests the creation of a map programmatically and then validation.
     */
    @Test
    public void testCreateAndValidateMap() {
        Map map = new Map();
        
        // Add continents
        map.addContinent("Continent1", 5);
        map.addContinent("Continent2", 3);
        
        // Add territories
        map.addCountry("Territory1", "Continent1");
        map.addCountry("Territory2", "Continent1");
        map.addCountry("Territory3", "Continent2");
        
        // Add neighbors to make it connected
        map.addNeighbor("Territory1", "Territory2");
        map.addNeighbor("Territory2", "Territory3");
        
        // Validate the map
        assertTrue("Programmatically created map should be valid", map.mapValidation());
        assertTrue("Programmatically created map should have valid continents", map.continentValidation());
    }
    
    // Helper methods to create test map files
    
    private void createValidMapFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(d_validMapFilePath)) {
            writer.println("[continents]");
            writer.println("Continent1 5");
            writer.println("Continent2 3");
            writer.println("");
            writer.println("[countries]");
            writer.println("1 Territory1 1");
            writer.println("2 Territory2 1");
            writer.println("3 Territory3 2");
            writer.println("");
            writer.println("[borders]");
            writer.println("1 2 3");
            writer.println("2 1 3");
            writer.println("3 1 2");
        }
    }
    
    private void createInvalidMapFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(d_invalidMapFilePath)) {
            writer.println("[continents]");
            writer.println("Continent1 5");
            writer.println("");
            writer.println("[countries]");
            writer.println("1 Territory1 1");
            writer.println("");
            // Missing [borders] section
        }
    }
    
    private void createDisconnectedMapFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(d_disconnectedMapFilePath)) {
            writer.println("[continents]");
            writer.println("Continent1 5");
            writer.println("Continent2 3");
            writer.println("");
            writer.println("[countries]");
            writer.println("1 Territory1 1");
            writer.println("2 Territory2 1");
            writer.println("3 Territory3 2");
            writer.println("4 Territory4 2");  // Disconnected territory
            writer.println("");
            writer.println("[borders]");
            writer.println("1 2 3");
            writer.println("2 1 3");
            writer.println("3 1 2");
            writer.println("4");  // No connections
        }
    }
    
    private void createDisconnectedContinentMapFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(d_disconnectedContinentMapFilePath)) {
            writer.println("[continents]");
            writer.println("Continent1 5");
            writer.println("Continent2 3");
            writer.println("");
            writer.println("[countries]");
            writer.println("1 Territory1 1");
            writer.println("2 Territory2 1");
            writer.println("3 Territory3 2");  // Disconnected within continent
            writer.println("4 Territory4 2");  // Disconnected within continent
            writer.println("");
            writer.println("[borders]");
            writer.println("1 2 3");  // Connected to Territory3, but not within Continent1
            writer.println("2 1 4");  // Connected to Territory4, but not within Continent1
            writer.println("3 1");    // Connected to Territory1, but not within Continent2
            writer.println("4 2");    // Connected to Territory2, but not within Continent2
        }
    }
    
    /**
     * Cleans up test files after tests.
     */
    @org.junit.After
    public void tearDown() {
        // Delete test files
        new File(d_validMapFilePath).delete();
        new File(d_invalidMapFilePath).delete();
        new File(d_disconnectedMapFilePath).delete();
        new File(d_disconnectedContinentMapFilePath).delete();
    }
}
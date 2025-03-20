package com.UnitTests;

//import com.Game.Map;
//import com.Game.Territory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Map} class.
 * This class tests the functionality of adding, removing, and interacting with territories,
 * continents, and neighbors, as well as saving the map to a file.
 */
public class MapTest {

//    /**
//     * The Map object being tested.
//     */
//    private Map d_map;
//
//    /**
//     * Path for the test file used in the save operation tests.
//     */
//    private final String TESTFILEPATH = "test_map.txt";
//
//    /**
//     * Sets up the test environment before each test case.
//     * Initializes the {@link Map} object.
//     */
//    @BeforeEach
//    void setUp() {
//        d_map = new Map();
//    }
//
//    /**
//     * Cleans up after each test case by deleting the test file if it exists.
//     */
//    @AfterEach
//    void tearDown() {
//        File file = new File(TESTFILEPATH);
//        if (file.exists()) {
//            file.delete();
//        }
//    }
//
//    /**
//     * Tests the {@link Map#addTerritory(Territory)} method by adding a territory
//     * and verifying that it is added correctly to the map.
//     */
//    @Test
//    void testAddTerritory() {
//        Territory territory = new Territory("Territory1", "Continent1", 5);
//        d_map.addTerritory(territory);
//        assertEquals(1, d_map.getTerritoryList().size());
//        assertEquals("Territory1", d_map.getTerritoryList().get(0).getName());
//    }
//
//    /**
//     * Tests the {@link Map#getTerritoryByName(String)} method by checking if a territory
//     * can be retrieved by name and ensuring that non-existing territories return null.
//     */
//    @Test
//    void testGetTerritoryByName() {
//        Territory territory = new Territory("Territory1", "Continent1", 5);
//        d_map.addTerritory(territory);
//        assertNotNull(d_map.getTerritoryByName("Territory1"));
//        assertNull(d_map.getTerritoryByName("NonExistent"));
//    }
//
//    /**
//     * Tests the {@link Map#addContinent(String, int)} method by adding a continent and
//     * verifying that the corresponding country is added correctly.
//     */
//    @Test
//    void testAddContinent() {
//        d_map.addContinent("Continent1", 5);
//        d_map.addCountry("Country1", "Continent1");
//        assertEquals(1, d_map.getTerritoryList().size());
//    }
//
//    /**
//     * Tests the {@link Map#removeContinent(String)} method by removing a continent
//     * and verifying that the territories in that continent are also removed.
//     */
//    @Test
//    void testRemoveContinent() {
//        d_map.addContinent("Continent1", 5);
//        d_map.addCountry("Country1", "Continent1");
//        d_map.removeContinent("Continent1");
//        assertEquals(0, d_map.getTerritoryList().size());
//    }
//
//    /**
//     * Tests the {@link Map#addNeighbor(String, String)} method by adding a neighbor relationship
//     * between two territories and verifying that the neighbors are correctly added.
//     */
//    @Test
//    void testAddNeighbor() {
//        Territory t1 = new Territory("Territory1", "Continent1", 5);
//        Territory t2 = new Territory("Territory2", "Continent1", 5);
//        d_map.addTerritory(t1);
//        d_map.addTerritory(t2);
//        d_map.addNeighbor("Territory1", "Territory2");
//        assertTrue(t1.getNeighborList().contains(t2));
//        assertTrue(t2.getNeighborList().contains(t1));
//    }
//
//    /**
//     * Tests the {@link Map#removeNeighbor(String, String)} method by removing a neighbor relationship
//     * between two territories and verifying that the relationship is correctly removed.
//     */
//    @Test
//    void testRemoveNeighbor() {
//        Territory t1 = new Territory("Territory1", "Continent1", 5);
//        Territory t2 = new Territory("Territory2", "Continent1", 5);
//        d_map.addTerritory(t1);
//        d_map.addTerritory(t2);
//        d_map.addNeighbor("Territory1", "Territory2");
//        d_map.removeNeighbor("Territory1", "Territory2");
//        assertFalse(t1.getNeighborList().contains(t2));
//        assertFalse(t2.getNeighborList().contains(t1));
//    }
//
//    /**
//     * Tests the {@link Map#addCountry(String, String)} method by adding a country to a continent
//     * and verifying that the country is added to the map under the correct continent.
//     */
//    @Test
//    void testAddCountry() {
//        d_map.addContinent("NorthAmerica", 5);
//        d_map.addCountry("Canada", "NorthAmerica");
//
//        List<Territory> territories = d_map.getTerritoryList();
//        assertEquals(1, territories.size());
//        assertEquals("Canada", territories.get(0).getName());
//        assertEquals("NorthAmerica", territories.get(0).getContinent());
//    }
//
//    /**
//     * Tests the behavior of adding a country to a non-existent continent.
//     * Ensures that the country is not added to the map.
//     */
//    @Test
//    void testAddCountryToNonExistentContinent() {
//        d_map.addCountry("Canada", "NonExistent");
//        assertEquals(0, d_map.getTerritoryList().size());
//    }
//
//    /**
//     * Tests the {@link Map#removeCountry(String)} method by removing a country from the map
//     * and verifying that it is removed correctly.
//     */
//    @Test
//    void testRemoveCountry() {
//        d_map.addContinent("NorthAmerica", 5);
//        d_map.addCountry("Canada", "NorthAmerica");
//        d_map.addCountry("USA", "NorthAmerica");
//
//        d_map.removeCountry("Canada");
//
//        List<Territory> territories = d_map.getTerritoryList();
//        assertEquals(1, territories.size());
//        assertEquals("USA", territories.get(0).getName());
//    }
//
//    /**
//     * Tests the {@link Map#setContinents(java.util.Map)} method by setting multiple continents
//     * and verifying that countries are added correctly under those continents.
//     */
//    @Test
//    void testSetContinents() {
//        // Add some continents
//        java.util.Map<String, Integer> newContinents = new HashMap<>();
//        newContinents.put("Europe", 3);
//        newContinents.put("Asia", 7);
//
//        d_map.setContinents(newContinents);
//
//        // Add territories after setting continents
//        d_map.addCountry("France", "Europe");
//        d_map.addCountry("China", "Asia");
//
//        List<Territory> territories = d_map.getTerritoryList();
//
//        // Verify that the countries were added under the correct continent
//        assertEquals(2, territories.size());
//        assertEquals("France", territories.get(0).getName());
//        assertEquals("Europe", territories.get(0).getContinent());
//
//        assertEquals("China", territories.get(1).getName());
//        assertEquals("Asia", territories.get(1).getContinent());
//    }
//
//    /**
//     * Tests the {@link Map#saveToFile(String)} method by saving the map to a file
//     * and verifying that the file is created and contains the expected data.
//     *
//     * @throws IOException if an I/O error occurs during file writing or reading.
//     */
//    @Test
//    void testSaveToFile() throws IOException {
//        d_map.addContinent("Asia", 5);
//        d_map.addCountry("India", "Asia");
//        d_map.addCountry("China", "Asia");
//        d_map.addNeighbor("India", "China");
//
//        d_map.saveToFile(TESTFILEPATH);
//
//        File file = new File(TESTFILEPATH);
//        assertTrue(file.exists());
//
//        List<String> lines = Files.readAllLines(Paths.get(TESTFILEPATH));
//        assertFalse(lines.isEmpty());
//        assertTrue(lines.contains("[continents]"));
//        assertTrue(lines.contains("Asia 5"));
//        assertTrue(lines.contains("[countries]"));
//        assertTrue(lines.contains("[borders]"));
//    }
//
//    /**
//     * Tests the {@link Map#mapValidation()} method to ensure the entire map is connected.
//     */
//    @Test
//    void testMapValidationConnected() {
//        // Ensure the entire map is connected
//        d_map.addContinent("Asia", 5);
//        d_map.addCountry("India", "Asia");
//        d_map.addCountry("China", "Asia");
//        d_map.addNeighbor("India", "China");
//
//        assertTrue(d_map.mapValidation(), "The map should be a connected graph.");
//    }
//
//    /**
//     * Tests the {@link Map#mapValidation()} method to ensure the map is disconnected.
//     */
//    @Test
//    void testMapValidationDisconnected() {
//        // Add territories but no neighbors (disconnected map)
//        d_map.addContinent("Asia", 5);
//        d_map.addCountry("India", "Asia");
//        d_map.addCountry("China", "Asia");
//
//        assertFalse(d_map.mapValidation(), "The map should be disconnected.");
//    }
//
//    /**
//     * Tests the {@link Map#continentValidation()} method to ensure each continent is a connected subgraph.
//     */
//    @Test
//    void testContinentValidationConnected() {
//        d_map.addContinent("Asia", 5);
//        d_map.addCountry("India", "Asia");
//        d_map.addCountry("China", "Asia");
//        d_map.addNeighbor("India", "China");
//
//        assertTrue(d_map.continentValidation(), "Each continent should be a connected subgraph.");
//    }
//
//    /**
//     * Tests the {@link Map#continentValidation()} method to ensure the continent is disconnected.
//     */
//    @Test
//    void testContinentValidationDisconnected() {
//        d_map.addContinent("Asia", 5);
//        d_map.addCountry("India", "Asia");
//        d_map.addCountry("China", "Asia");
//        d_map.addCountry("Pakistan", "Asia");
//
//        d_map.addNeighbor("India", "China");
//
//        // Remove neighbor between 'China' and 'Pakistan', making 'Asia' disconnected
//        d_map.getTerritoryByName("China").getNeighborList().remove(d_map.getTerritoryByName("Pakistan"));
//
//        assertFalse(d_map.continentValidation(), "The continent Asia should be disconnected.");
//    }
	
}
package com.UnitTests;

import com.Game.Map;
import com.Game.Territory;
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

class MapTest {
    private Map map;
    private final String testFilePath = "test_map.txt";

    @BeforeEach
    void setUp() {
        map = new Map();
    }

    @AfterEach
    void tearDown() {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddTerritory() {
        Territory territory = new Territory("Territory1", "Continent1", 5);
        map.addTerritory(territory);
        assertEquals(1, map.getTerritoryList().size());
        assertEquals("Territory1", map.getTerritoryList().get(0).getName());
    }

    @Test
    void testGetTerritoryByName() {
        Territory territory = new Territory("Territory1", "Continent1", 5);
        map.addTerritory(territory);
        assertNotNull(map.getTerritoryByName("Territory1"));
        assertNull(map.getTerritoryByName("NonExistent"));
    }

    @Test
    void testAddContinent() {
        map.addContinent("Continent1", 5);
        map.addCountry("Country1", "Continent1");
        assertEquals(1, map.getTerritoryList().size());
    }

    @Test
    void testRemoveContinent() {
        map.addContinent("Continent1", 5);
        map.addCountry("Country1", "Continent1");
        map.removeContinent("Continent1");
        assertEquals(0, map.getTerritoryList().size());
    }

    @Test
    void testAddNeighbor() {
        Territory t1 = new Territory("Territory1", "Continent1", 5);
        Territory t2 = new Territory("Territory2", "Continent1", 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addNeighbor("Territory1", "Territory2");
        assertTrue(t1.getNeighborList().contains(t2));
        assertTrue(t2.getNeighborList().contains(t1));
    }

    @Test
    void testRemoveNeighbor() {
        Territory t1 = new Territory("Territory1", "Continent1", 5);
        Territory t2 = new Territory("Territory2", "Continent1", 5);
        map.addTerritory(t1);
        map.addTerritory(t2);
        map.addNeighbor("Territory1", "Territory2");
        map.removeNeighbor("Territory1", "Territory2");
        assertFalse(t1.getNeighborList().contains(t2));
        assertFalse(t2.getNeighborList().contains(t1));
    }

    @Test
    void testAddCountry() {
        map.addContinent("NorthAmerica", 5);
        map.addCountry("Canada", "NorthAmerica");

        List<Territory> territories = map.getTerritoryList();
        assertEquals(1, territories.size());
        assertEquals("Canada", territories.get(0).getName());
        assertEquals("NorthAmerica", territories.get(0).getContinent());
    }

    @Test
    void testAddCountryToNonExistentContinent() {
        map.addCountry("Canada", "NonExistent");
        assertEquals(0, map.getTerritoryList().size());
    }

    @Test
    void testRemoveCountry() {
        map.addContinent("NorthAmerica", 5);
        map.addCountry("Canada", "NorthAmerica");
        map.addCountry("USA", "NorthAmerica");

        map.removeCountry("Canada");

        List<Territory> territories = map.getTerritoryList();
        assertEquals(1, territories.size());
        assertEquals("USA", territories.get(0).getName());
    }

    @Test
    void testSetContinents() {
        // Add some continents
        java.util.Map<String, Integer> newContinents = new HashMap<>();
        newContinents.put("Europe", 3);
        newContinents.put("Asia", 7);

        map.setContinents(newContinents);

        // Add territories after setting continents
        map.addCountry("France", "Europe");
        map.addCountry("China", "Asia");

        List<Territory> territories = map.getTerritoryList();

        // Verify that the countries were added under the correct continent
        assertEquals(2, territories.size());
        assertEquals("France", territories.get(0).getName());
        assertEquals("Europe", territories.get(0).getContinent());

        assertEquals("China", territories.get(1).getName());
        assertEquals("Asia", territories.get(1).getContinent());
    }

    @Test
    void testSaveToFile() throws IOException {
        map.addContinent("Asia", 5);
        map.addCountry("India", "Asia");
        map.addCountry("China", "Asia");
        map.addNeighbor("India", "China");

        map.saveToFile(testFilePath);

        File file = new File(testFilePath);
        assertTrue(file.exists());

        List<String> lines = Files.readAllLines(Paths.get(testFilePath));
        assertFalse(lines.isEmpty());
        assertTrue(lines.contains("[continents]"));
        assertTrue(lines.contains("Asia 5"));
        assertTrue(lines.contains("[countries]"));
        assertTrue(lines.contains("[borders]"));
    }
}
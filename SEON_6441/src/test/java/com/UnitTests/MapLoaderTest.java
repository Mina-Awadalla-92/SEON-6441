package com.UnitTests;

import com.Game.MapLoader;
import com.Game.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MapLoaderTest {
    private MapLoader mapLoader;

    @BeforeEach
    void setUp() {
        mapLoader = new MapLoader();
    }

    @Test
    void testConstructor() {
        assertNull(mapLoader.getLoadedMap(), "Default constructor should initialize loadedMap as null");
    }

    @Test
    void testCopyConstructor() {
        MapLoader original = new MapLoader();
        original.read("LoadingMaps/canada.map"); // Simulate reading a valid map

        MapLoader copy = new MapLoader(original);
        assertNotSame(original, copy, "Copy should be a different instance");
        assertNotNull(copy.getLoadedMap(), "Copied MapLoader should have a loaded map");
    }

    @Test
    void testReadValidFile() throws IOException {

        String filePath = "LoadingMaps/canada.map";
        mapLoader.read(filePath);

        // Ensure the map is loaded properly
        assertNotNull(mapLoader.getLoadedMap(), "Map should be loaded for a valid file");

        // Validate territories (countries)
        List<Territory> territories = mapLoader.getLoadedMap().getTerritoryList();
        assertNotNull(territories, "Territories should not be null");

        // Example check for a specific territory
        Territory newBrunswick = territories.stream().filter(t -> t.getName().equals("New_Brunswick")).findFirst().orElse(null);
        assertNotNull(newBrunswick, "New_Brunswick should be found in the territories");
        assertEquals("Atlantic_Provinces", newBrunswick.getContinent(), "New_Brunswick should belong to Atlantic_Provinces");

        // Check for borders
        assertTrue(newBrunswick.getNeighborList().size() > 0, "New_Brunswick should have neighbors");

        // Check the existence of another territory and its border
        Territory princeEdwardIsland = territories.stream().filter(t -> t.getName().equals("Prince_Edward_Island")).findFirst().orElse(null);
        assertNotNull(princeEdwardIsland, "Prince_Edward_Island should be found in the territories");
        assertTrue(princeEdwardIsland.getNeighborList().size() > 0, "Prince_Edward_Island should have neighbors");
    }

    @Test
    void testReadInvalidFile() {
        mapLoader.read("non_existent_file.map");
        assertNull(mapLoader.getLoadedMap(), "Loading an invalid file should keep loadedMap as null");
    }

    @Test
    void testIsValidWithValidMap() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("LoadingMaps/canada.map");

        // Ensure that the map file exists in the resources folder
        assertNotNull(inputStream, "Map file should be available in resources");
        assertTrue(mapLoader.isValid("LoadingMaps/canada.map"), "Valid map should return true");
    }
}
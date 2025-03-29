package com.Game.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.Game.model.Territory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link MapLoader} class.
 * These tests ensure that the map loading functionality behaves as expected under different scenarios.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapLoaderTest {

    /**
     * The MapLoader instance used for testing.
     */
    private MapLoader d_mapLoader;

    /**
     * Sets up the test environment by initializing a new instance of {@link MapLoader}.
     * This method is called before each test.
     */
    @Before
    public void setUp() {
        d_mapLoader = new MapLoader();
    }

    /**
     * Test the default constructor of the {@link MapLoader} class.
     * It ensures that the loaded map is initialized as null.
     */
    @Test
    public void testConstructor() {
        assertNull(d_mapLoader.getLoadedMap(), "Default constructor should initialize loadedMap as null");
    }

    /**
     * Test the copy constructor of the {@link MapLoader} class.
     * It verifies that the copied MapLoader is a different instance and that the map is properly copied.
     */
    @Test
    public void testCopyConstructor() {
        MapLoader original = new MapLoader();
        original.read("LoadingMaps/canada.map"); // Simulate reading a valid map

        MapLoader copy = new MapLoader(original);
        assertNotSame(original, copy, "Copy should be a different instance");
        assertNotNull(copy.getLoadedMap(), "Copied MapLoader should have a loaded map");
    }

    /**
     * Test the {@link MapLoader#read(String)} method with a valid map file.
     * It verifies that the map is loaded correctly and that territories and borders are processed as expected.
     * @throws IOException if an error occurs while reading the map file
     */
    @Test
    public void testReadValidFile() throws IOException {

        String filePath = "LoadingMaps/canada.map";
        d_mapLoader.read(filePath);

        // Ensure the map is loaded properly
        assertNotNull(d_mapLoader.getLoadedMap(), "Map should be loaded for a valid file");

        // Validate territories (countries)
        List<Territory> territories = d_mapLoader.getLoadedMap().getTerritoryList();
        assertNotNull(territories, "Territories should not be null");

        // Example check for a specific territory
        Territory newBrunswick = territories.stream().filter(t -> t.getName().equals("New_Brunswick")).findFirst().orElse(null);
        assertNotNull(newBrunswick, "New_Brunswick should be found in the territories");
        Assert.assertEquals("New_Brunswick should belong to Atlantic_Provinces", "Atlantic_Provinces", newBrunswick.getContinent());

        // Check for borders
        Assert.assertTrue("New_Brunswick should have neighbors", newBrunswick.getNeighborList().size() > 0);

        // Check the existence of another territory and its border
        Territory princeEdwardIsland = territories.stream().filter(t -> t.getName().equals("Prince_Edward_Island")).findFirst().orElse(null);
        assertNotNull(princeEdwardIsland, "Prince_Edward_Island should be found in the territories");
        Assert.assertTrue("Prince_Edward_Island should have neighbors", princeEdwardIsland.getNeighborList().size() > 0);
    }

    /**
     * Test the {@link MapLoader#read(String)} method with an invalid map file.
     * It verifies that the map remains null when an invalid file is read.
     */
    @Test
    public void testReadInvalidFile() {
        d_mapLoader.read("non_existent_file.map");
        assertNull(d_mapLoader.getLoadedMap(), "Loading an invalid file should keep loadedMap as null");
    }

    /**
     * Test the {@link MapLoader#isValid(String)} method with a valid map file.
     * It ensures that the method returns true for a valid map.
     * @throws IOException if an error occurs while reading the map file
     */
    @Test
    public void testIsValidWithValidMap() throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("LoadingMaps/canada.map");

        // Ensure that the map file exists in the resources folder
        assertNotNull(inputStream, "Map file should be available in resources");
        assertTrue(d_mapLoader.isValid("LoadingMaps/canada.map"), "Valid map should return true");
    }

    /**
     * Tests the `validateMap` method by passing true for the `showMsg` flag,
     * which should display a message indicating whether the map is valid.
     *
     * @see MapLoader#validateMap(boolean)
     */
    @Test
    public void testValidateMap_withMessage() {
        String filePath = "LoadingMaps/canada.map";
        d_mapLoader.read(filePath);
        boolean result = d_mapLoader.validateMap(true);  // Show message should be true

        assertTrue(result, "Map should be valid.");
    }

    /**
     * Tests the `validateMap` method by passing false for the `showMsg` flag,
     * which should suppress the message indicating the map validity.
     *
     * @see MapLoader#validateMap(boolean)
     */
    @Test
    public void testValidateMap_withoutMessage() {
        String filePath = "LoadingMaps/canada.map";
        d_mapLoader.read(filePath);

        boolean result = d_mapLoader.validateMap(false);  // Show message should be false

        assertTrue(result, "Map should be valid.");
    }

    /**
     * Tests the `isMapExist` method by checking an existing map file.
     * It ensures that a valid map file returns a non-null BufferedReader.
     *
     * @throws IOException if an I/O error occurs during file operations.
     * @see MapLoader#isMapExist(String)
     */
    @Test
    public void testIsMapExist_existingFile() throws IOException {

        BufferedReader result = d_mapLoader.isMapExist("LoadingMaps/canada.map");

        assertNotNull(result, "BufferedReader should not be null for existing file.");
    }

    /**
     * Tests the `isMapExist` method by checking a non-existing map file.
     * It ensures that a non-existing map file returns null.
     *
     * @throws IOException if an I/O error occurs during file operations.
     * @see MapLoader#isMapExist(String)
     */
    @Test
    public void testIsMapExist_nonExistingFile() throws IOException {
        String nonExistingFile = "nonExistingMap.txt";

        BufferedReader result = d_mapLoader.isMapExist(nonExistingFile);

        assertNull(result, "BufferedReader should be null for non-existing file.");
    }
}

package com.Game.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Game.model.Map;

/**
 * Example JUnit tests for MapLoader.
 * 
 * Note: For thorough testing of read(...) and isValid(...), you need actual map files.
 * Below, we either create a minimal temporary file or rely on a test resource file 
 * in "src/test/resources/LoadingMaps/" if your project allows.
 */
public class MapLoaderTest {

    private MapLoader mapLoader;

    @BeforeEach
    void setUp() {
        mapLoader = new MapLoader();
    }

    @Test
    void testDefaultConstructor() {
        // By default, d_loadedMap is null
        assertNull(mapLoader.getLoadedMap(), 
                   "Initially, loadedMap should be null (per constructor).");
    }

    @Test
    void testCopyConstructor() {
        // First, create a MapLoader and set a loaded map
        MapLoader original = new MapLoader();
        original.setLoadedMap(new Map());
        // Copy it
        MapLoader copy = new MapLoader(original);

        assertNotNull(copy.getLoadedMap(), 
                      "Copy constructor should produce a new loadedMap, not null.");
        // The map inside copy is a new Map(...) copy of original's map, so not the same instance
        assertNotSame(original.getLoadedMap(), copy.getLoadedMap(), 
                      "Should be a separate instance of Map.");
    }

    @Test
    void testResetLoadedMap() {
        mapLoader.setLoadedMap(new Map());
        assertNotNull(mapLoader.getLoadedMap());
        
        mapLoader.resetLoadedMap();
        // Now we should have a new empty map
        assertNotNull(mapLoader.getLoadedMap(), 
                      "After reset, loadedMap should be a new empty Map, not null.");
        assertTrue(mapLoader.getLoadedMap().getTerritoryList().isEmpty(), 
                   "Reset map should have no territories.");
    }

    @Test
    void testIsMapExist_NonExistent() {
        // isMapExist("someFakePath") => null if doesn't exist
        BufferedReader reader = mapLoader.isMapExist("someFakePathThatDoesNotExist.map");
        assertNull(reader, "Expected null if file/resource is not found.");
    }

    @Test
    void testIsMapExist_TempFile() throws IOException {
        // Create a small temp file
        File tempFile = File.createTempFile("testMap", ".map");
        tempFile.deleteOnExit();  // Clean up after test
        
        // isMapExist => should return a valid BufferedReader
        BufferedReader reader = mapLoader.isMapExist(tempFile.getAbsolutePath());
        assertNotNull(reader, "Expected a non-null reader for an existing file.");

        // Always close
        reader.close();
    }

    @Test
    void testIsValid_EmptyFile() throws IOException {
        // Create empty temp file
        File tempFile = File.createTempFile("empty", ".map");
        tempFile.deleteOnExit();

        // isValid => false, because file lacks [continents], [countries], [borders]
        boolean valid = mapLoader.isValid(tempFile.getAbsolutePath());
        assertFalse(valid, "Empty file should be invalid.");
    }

    @Test
    void testIsValid_MinimalMap() throws IOException {
        // We'll create a minimal map file that has [continents], [countries], [borders]
        // even if they have no content, presence of the sections satisfies isValid logic
        String minimalMapContent = 
              "[continents]\n"
            + "[countries]\n"
            + "[borders]\n";

        File tempFile = File.createTempFile("minimal", ".map");
        tempFile.deleteOnExit();

        // Write to the file
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(minimalMapContent);
        }

        // Now check isValid => true, because it has all 3 sections
        boolean valid = mapLoader.isValid(tempFile.getAbsolutePath());
        assertTrue(valid, "Minimal map with the 3 sections should be considered valid by isValid().");
    }

    @Test
    void testRead_NonExistingFile() {
        // Attempt to read a non-existent file => should not throw, but prints an error
        // We'll check that it doesn't blow up or set loadedMap
        mapLoader.read("noSuchFile.map");

        // Because read(...) does "System.err.println(File not found:...)" or returns silently,
        // d_loadedMap might remain null or become new Map. Let's see the code:
        // In read(...) we do "d_loadedMap = new Map();" near the top if we found the file 
        // or if we found a resource. If not found, it does return.
        // So after read(...) if file doesn't exist, d_loadedMap is still null.
        assertNull(mapLoader.getLoadedMap(), "For a non-existent file, loadedMap should remain null or not set.");
    }

    @Test
    void testRead_ValidButMinimalMap() throws IOException {
        // Same as the minimalMap test, but we use read(...) which populates d_loadedMap
        String minimalMapContent = 
              "[continents]\n"
            + "Asia 5\n\n" // a continent line
            + "[countries]\n"
            + "1 Japan 1\n\n"
            + "[borders]\n"
            + "1\n";

        File tempFile = File.createTempFile("testReadValid", ".map");
        tempFile.deleteOnExit();

        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(minimalMapContent);
        }

        // read the file
        mapLoader.read(tempFile.getAbsolutePath());

        // After reading, d_loadedMap should not be null
        assertNotNull(mapLoader.getLoadedMap(), "Loaded map should not be null after reading a valid file.");

        // For the minimal lines, we expect:
        // 1 territory named "Japan" in the continent "Asia" with bonus=5
        assertFalse(mapLoader.getLoadedMap().getTerritoryList().isEmpty(),
                    "Expected at least 1 territory from the read file.");

        // Optionally, we can check the territory name or continent. e.g.:
        com.Game.model.Territory t = mapLoader.getLoadedMap().getTerritoryByName("Japan");
        assertNotNull(t, "Territory 'Japan' should exist in the loaded map.");
        assertEquals("Asia", t.getContinent(), "Should parse the continent as 'Asia' with bonus 5.");

        // Also check that the map is validated at the end of read() if the file was found
        // read() calls validateMap(false) in the finally block.
        // We won't do advanced checks here, but we can see if map is logically connected, etc.
    }

    @Test
    void testValidateMap_NoMapLoaded() {
        // If no map is loaded, d_loadedMap is null => we call validateMap => it tries:
        // Actually from code, after read(...) we get d_loadedMap. If never read, it's null by default.
        // But the code calls "d_loadedMap.mapValidation()" => that would cause NPE if d_loadedMap is null
        // However, in the constructor, it's null, but let's see:
        // Actually the code might throw NullPointer or do something. 
        // We'll call validateMap manually.

        // Because we haven't read anything, d_loadedMap is null
        assertNull(mapLoader.getLoadedMap());

        // We'll try calling validateMap():
        // The code tries to do "d_loadedMap.mapValidation() && d_loadedMap.continentValidation()"
        // on a null pointer => that might cause an exception. Let's see or let's catch it.
        // Typically we might expect an NPE or false result. 
        // The code as is would produce a NullPointerException if d_loadedMap is truly null.
        // So let's see how we handle it in a test.
        // We'll do a safe call and check if it doesn't crash:
        assertThrows(NullPointerException.class, () -> mapLoader.validateMap(),
                     "Expected a NullPointerException if no map has been loaded.");
    }

    @Test
    void testValidateMap_EmptyMap() {
        // If we do resetLoadedMap => we get an empty map object (which is not null).
        // Then validateMap => should fail because no territories => not connected
        mapLoader.resetLoadedMap();
        // Now d_loadedMap is a new empty Map
        boolean result = mapLoader.validateMap(false);
        assertFalse(result, "An empty map can't be valid (not connected).");
    }
}


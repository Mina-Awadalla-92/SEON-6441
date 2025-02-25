package com.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;

/**
 * Loads a game map from a file. The map consists of continents, countries (territories), and borders (neighbor relationships).
 * This class is responsible for parsing the map file, validating its format, and storing the data in a Map object.
 */
public class MapLoader {
    private Map d_loadedMap;

    /**
     * Default constructor. Initializes the loaded map to null.
     */
    public MapLoader() {

        this.d_loadedMap = null;
    }

    /**
     * Copy constructor. Creates a new MapLoader instance that is a copy of another MapLoader.
     *
     * @param p_other The MapLoader instance to copy.
     */
    public MapLoader(MapLoader p_other) {
        this.d_loadedMap = new Map(p_other.d_loadedMap);
    }

    /**
     * Gets the loaded map.
     *
     * @return The loaded map object.
     */
    public Map getLoadedMap() {
        return d_loadedMap;
    }

    /**
     * Sets the loaded map.
     */
    public void resetLoadedMap() {
        this.d_loadedMap = new Map();;
    }

    /**
     * Reads a map from a file.
     *
     * @param p_fileName The name of the map file to read.
     */
    public void read(String p_fileName) {
        BufferedReader reader = null;

        try {
            // First, try reading from the local filesystem
            File file = new File(p_fileName);
            if (file.exists())
            {
                reader = new BufferedReader(new FileReader(file));
            }
            else
            {
                // Fallback to reading from classpath resources
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(p_fileName);
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                } else {
                    //System.err.println("File not found: " + fileName);
                    return;
                }
            }

            d_loadedMap = new Map();
            // Reading the file content
            String line;
            List<String> continentNames = new ArrayList<>();
            List<Integer> bonuses = new ArrayList<>();
            List<Territory> territories = new ArrayList<>();
            java.util.Map<String, Integer> continentMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                if (line.equals("[continents]")) {
                    while (!(line = reader.readLine()).isEmpty()) {
                        String[] parts = line.split(" ");
                        continentNames.add(parts[0]);
                        bonuses.add(Integer.parseInt(parts[1]));
                        continentMap.put(parts[0], Integer.parseInt(parts[1]));
                    }
                    d_loadedMap.setContinents(continentMap);
                } else if (line.equals("[countries]")) {
                    while (!(line = reader.readLine()).isEmpty()) {
                        String[] parts = line.split(" ");
                        Territory t = new Territory(parts[1], continentNames.get(Integer.parseInt(parts[2]) - 1), bonuses.get(Integer.parseInt(parts[2]) - 1));
                        territories.add(t);
                        d_loadedMap.addTerritory(t);
                    }
                } else if (line.equals("[borders]")) {
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(" ");
                        Territory t = territories.get(Integer.parseInt(parts[0]) - 1);
                        for (int i = 1; i < parts.length; i++) {
                            t.addNeighbor(territories.get(Integer.parseInt(parts[i]) - 1));
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Validates the format of a map file by checking the structure of its sections: [continents], [countries], and [borders].
     *
     * @param p_mapFile The name of the map file to validate.
     * @return true if the map file is valid, false otherwise.
     */
    public boolean isValid(String p_mapFile) {
        // Correct path relative to the resources folder
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(p_mapFile);

        if (inputStream == null) {
            //System.err.println("File not found: " + mapFile);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            // Validate [continents] section
            while ((line = reader.readLine()) != null && !line.equals("[continents]")) {}
            if (line == null) return false;

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(" ");
                if (parts.length < 2 || !parts[1].matches("\\d+")) return false;
            }

            // Validate [countries] section
            if ((line = reader.readLine()) == null || !line.equals("[countries]")) return false;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(" ");
                if (parts.length < 3 || !parts[0].matches("\\d+") || !parts[2].matches("\\d+")) return false;
            }

            // Validate [borders] section
            if ((line = reader.readLine()) == null || !line.equals("[borders]")) return false;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length < 1 || !parts[0].matches("\\d+")) return false; // Allow empty neighbor lists
            }
        } catch (IOException e) {
            e.printStackTrace();  // Log the error to see what went wrong
            return false;
        }
        return true;
    }
}
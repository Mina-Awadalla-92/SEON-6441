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

    /**
     * Represents the loaded map data for the game.
     */
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
     * Resets the loaded map.
     */
    public void resetLoadedMap() {
        this.d_loadedMap = new Map();
    }

    /**
     * Reads a map from a file.
     *
     * @param p_fileName The name of the map file to read.
     */
    public void read(String p_fileName) {
        BufferedReader l_reader = null;

        try {
            // First, try reading from the local filesystem
            File l_file = new File(p_fileName);
            if (l_file.exists()) {
                l_reader = new BufferedReader(new FileReader(l_file));
            } else {
                // Fallback to reading from classpath resources
                String l_resourcePath = p_fileName;
                // If path is in LoadingMaps folder but doesn't include it in the path
                if (!l_resourcePath.contains("LoadingMaps/") && !l_resourcePath.contains("LoadingMaps\\")) {
                    l_resourcePath = "LoadingMaps/" + l_resourcePath;
                }
                
                InputStream l_inputStream = getClass().getClassLoader().getResourceAsStream(l_resourcePath);
                if (l_inputStream != null) {
                    l_reader = new BufferedReader(new InputStreamReader(l_inputStream));
                } else {
                    System.err.println("File not found: " + p_fileName);
                    return;
                }
            }

            d_loadedMap = new Map();
            // Reading the file content
            String l_line;
            List<String> l_continentNames = new ArrayList<>();
            List<Integer> l_bonuses = new ArrayList<>();
            List<Territory> l_territories = new ArrayList<>();
            java.util.Map<String, Integer> l_continentMap = new HashMap<>();

            while ((l_line = l_reader.readLine()) != null) {
                // Skip comments and sections we don't need
                if (l_line.startsWith(";") || l_line.equals("[files]")) {
                    continue;
                }
                
                if (l_line.equals("[continents]")) {
                    while ((l_line = l_reader.readLine()) != null && !l_line.isEmpty() && !l_line.startsWith("[")) {
                        // Skip comment lines
                        if (l_line.startsWith(";")) continue;
                        
                        String[] l_parts = l_line.split(" ");
                        l_continentNames.add(l_parts[0]);
                        int l_bonus = Integer.parseInt(l_parts[1]);
                        l_bonuses.add(l_bonus);
                        l_continentMap.put(l_parts[0], l_bonus);
                    }
                    d_loadedMap.setContinents(l_continentMap);
                    
                    // If we reached another section, rewind
                    if (l_line != null && l_line.startsWith("[")) {
                        continue;
                    }
                } 
                
                if (l_line != null && l_line.equals("[countries]")) {
                    while ((l_line = l_reader.readLine()) != null && !l_line.isEmpty() && !l_line.startsWith("[")) {
                        // Skip comment lines
                        if (l_line.startsWith(";")) continue;
                        
                        String[] l_parts = l_line.split(" ");
                        if (l_parts.length >= 3) {
                            int l_continentIndex = Integer.parseInt(l_parts[2]) - 1;
                            if (l_continentIndex >= 0 && l_continentIndex < l_continentNames.size()) {
                                Territory l_territory = new Territory(l_parts[1], l_continentNames.get(l_continentIndex), l_bonuses.get(l_continentIndex));
                                l_territories.add(l_territory);
                                d_loadedMap.addTerritory(l_territory);
                            }
                        }
                    }
                    
                    // If we reached another section, continue to process it
                    if (l_line != null && l_line.startsWith("[")) {
                        continue;
                    }
                } 
                
                if (l_line != null && l_line.equals("[borders]")) {
                    while ((l_line = l_reader.readLine()) != null && !l_line.isEmpty() && !l_line.startsWith("[")) {
                        // Skip comment lines
                        if (l_line.startsWith(";")) continue;
                        
                        String[] l_parts = l_line.split(" ");
                        if (l_parts.length >= 1) {
                            int l_territoryIndex = Integer.parseInt(l_parts[0]) - 1;
                            if (l_territoryIndex >= 0 && l_territoryIndex < l_territories.size()) {
                                Territory l_territory = l_territories.get(l_territoryIndex);
                                for (int i = 1; i < l_parts.length; i++) {
                                    int l_neighborIndex = Integer.parseInt(l_parts[i]) - 1;
                                    if (l_neighborIndex >= 0 && l_neighborIndex < l_territories.size()) {
                                        l_territory.addNeighbor(l_territories.get(l_neighborIndex));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing map file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (l_reader != null) {
                    l_reader.close();
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
        // First try to check the local file
        File l_file = new File(p_mapFile);
        BufferedReader l_reader = null;
        
        try {
            if (l_file.exists()) {
                l_reader = new BufferedReader(new FileReader(l_file));
            } else {
                // Try with LoadingMaps prefix
                String l_resourcePath = p_mapFile;
                if (!l_resourcePath.contains("LoadingMaps/") && !l_resourcePath.contains("LoadingMaps\\")) {
                    l_resourcePath = "LoadingMaps/" + l_resourcePath;
                }
                
                InputStream l_inputStream = getClass().getClassLoader().getResourceAsStream(l_resourcePath);
                if (l_inputStream == null) {
                    return false;
                }
                l_reader = new BufferedReader(new InputStreamReader(l_inputStream));
            }
            
            // Basic validation - check for required sections
            boolean l_hasContinents = false;
            boolean l_hasCountries = false;
            boolean l_hasBorders = false;
            
            String l_line;
            while ((l_line = l_reader.readLine()) != null) {
                if (l_line.equals("[continents]")) {
                    l_hasContinents = true;
                } else if (l_line.equals("[countries]")) {
                    l_hasCountries = true;
                } else if (l_line.equals("[borders]")) {
                    l_hasBorders = true;
                }
            }
            
            return l_hasContinents && l_hasCountries && l_hasBorders;
            
        } catch (IOException e) {
            return false;
        } finally {
            if (l_reader != null) {
                try {
                    l_reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
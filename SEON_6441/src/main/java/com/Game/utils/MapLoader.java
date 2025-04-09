package com.Game.utils;

import com.Game.model.Map;
import com.Game.model.Territory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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
     * Sets the loaded map.
     * 
     * @param p_loadedMap The map to set as loaded.
     */
    public void setLoadedMap(Map p_loadedMap) {
        this.d_loadedMap = p_loadedMap;
    }

    /**
     * Resets the loaded map.
     */
    public void resetLoadedMap() {
        this.d_loadedMap = new Map();
    }
    
    /**
     * Validates a collection of maps for use in the tournament mode.
     * 
     * @param p_mapFiles List of map file paths to validate
     * @return Map of validation results (map path -> validation result)
     */
    public java.util.Map<String, Boolean> validateMapsForTournament(List<String> p_mapFiles) {
        java.util.Map<String, Boolean> validationResults = new HashMap<>();
        
        for (String mapFile : p_mapFiles) {
            System.out.println("Validating map: " + mapFile);
            
            // Check if map exists
            BufferedReader reader = isMapExist(mapFile);
            if (reader == null) {
                System.out.println("  Error: Map file not found - " + mapFile);
                validationResults.put(mapFile, false);
                continue;
            }
            
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Check map format
            boolean isValidFormat = isValid(mapFile);
            if (!isValidFormat) {
                System.out.println("  Error: Invalid map format - " + mapFile);
                validationResults.put(mapFile, false);
                continue;
            }
            
            // Load and validate map connectivity
            resetLoadedMap();
            read(mapFile);
            boolean isValidMap = validateMap(false);
            
            if (!isValidMap) {
                System.out.println("  Error: Map validation failed (connectivity or other issues) - " + mapFile);
                validationResults.put(mapFile, false);
            } else {
                System.out.println("  Map is valid - " + mapFile);
                validationResults.put(mapFile, true);
            }
        }
        
        return validationResults;
    }

    /**
     * Checks if a map is completely valid:
     * 1. File exists
     * 2. Format is valid
     * 3. Map is connected
     * 4. Continents are connected
     * 
     * @param p_mapFile Path to the map file
     * @return true if the map is completely valid, false otherwise
     */
    public boolean isMapCompletelyValid(String p_mapFile) {
        // Check if map exists
        BufferedReader reader = isMapExist(p_mapFile);
        if (reader == null) {
            return false;
        }
        
        try {
            reader.close();
        } catch (IOException e) {
            return false;
        }
        
        // Check map format
        if (!isValid(p_mapFile)) {
            return false;
        }
        
        // Load and validate map connectivity
        resetLoadedMap();
        read(p_mapFile);
        return validateMap(false);
    }

    /**
     * Validates the format of a map file by checking the structure of its sections: [continents], [countries], and [borders].
     *
     * @param p_mapFile The name of the map file to validate.
     * @return true if the map file is valid, false otherwise.
     */
    public boolean isValid(String p_mapFile) {
        BufferedReader l_reader = null;
        
        try {
            l_reader = isMapExist(p_mapFile);
            if (l_reader == null) {
                return false;
            }

            // Basic validation - check for required sections
            boolean l_hasContinents = false;
            boolean l_hasCountries = false;
            boolean l_hasBorders = false;
            boolean l_haBMap = false;
            boolean l_hasTerritories = false;

            String l_line;
            while ((l_line = l_reader.readLine()) != null) {
                if (l_line.equalsIgnoreCase("[continents]"))
                {
                    l_hasContinents = true;
                }
                else if (l_line.equals("[countries]"))
                {
                    l_hasCountries = true;
                }
                else if (l_line.equals("[borders]"))
                {
                    l_hasBorders = true;
                }
                else if (l_line.equals("[Map]"))
                {
                    l_haBMap = true;
                }
                else if (l_line.equals("[Territories]"))
                {
                    l_hasTerritories = true;
                }
            }
            if (l_haBMap)
            {
                return l_hasTerritories && l_hasContinents;
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

    /**
     * Validates the map by checking if both the map and continents are valid.
     * This method defaults to printing the validation message.
     *
     * @return true if both the map and continents are valid, false otherwise.
     */
    public boolean validateMap() {
        return validateMap(true);
    }

    /**
     * Validates the map by checking if both the map and continents are valid.
     * Optionally prints the validation result message based on the value of showMsg.
     *
     * @param p_showMsg a boolean indicating whether to display the validation message (true to display, false to suppress).
     * @return true if both the map and continents are valid, false otherwise.
     */
    public boolean validateMap(boolean p_showMsg) {
        boolean l_isValid = d_loadedMap.mapValidation() && d_loadedMap.continentValidation();

        if (p_showMsg) {
            System.out.println(l_isValid ? "The map is valid." : "The map is invalid.");
        }

        return l_isValid;
    }

    /**
     * Checks if a map file exists either locally or as a resource, and returns a BufferedReader to read it.
     * If the file does not exist or cannot be found, it returns null.
     *
     * @param p_mapFile the path or name of the map file to be checked.
     * @return a BufferedReader for reading the map file if it exists, null otherwise.
     */
    public BufferedReader isMapExist(String p_mapFile) {
        File l_file = new File(p_mapFile);
        BufferedReader l_reader = null;

        try {
            if (l_file.exists()) {
                // File exists locally, open it for reading
                l_reader = new BufferedReader(new FileReader(l_file));
            } else {
                // Try with LoadingMaps prefix
                String l_resourcePath = p_mapFile;
                if (!l_resourcePath.contains("LoadingMaps/") && !l_resourcePath.contains("LoadingMaps\\")) {
                    l_resourcePath = "LoadingMaps/" + l_resourcePath;
                }

                InputStream l_inputStream = getClass().getClassLoader().getResourceAsStream(l_resourcePath);
                if (l_inputStream == null) {
                    return null;
                }
                // Resource found, create a reader
                l_reader = new BufferedReader(new InputStreamReader(l_inputStream));
            }
        } catch (IOException e) {
            return null;
        }

        return l_reader;
    }

    /**
     * Reads a map from a file.
     *
     * @param p_fileName The name of the map file to read.
     */
    public void read(String p_fileName) {
        BufferedReader l_reader = null;

        try {
            File l_file = new File(p_fileName);
            if (l_file.exists()) {
                l_reader = new BufferedReader(new FileReader(l_file));
            } else {
                String l_resourcePath = p_fileName;
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

            String l_line;
            while ((l_line = l_reader.readLine()) != null) {
                l_line = l_line.trim();
                if (l_line.isEmpty() || l_line.startsWith(";")) {
                    continue;
                }

                // Check first relevant line
                if (l_line.equalsIgnoreCase("[Map]")) {
                    System.out.println("Detected Conquest map format.");
                    readConquestMap(p_fileName);
                    return;
                } else {
                    System.out.println("Detected Domination map format.");
                    readDominationMap(p_fileName);
                    return;
                }
            }

            System.err.println("Could not determine map format: " + p_fileName);
        } catch (IOException e) {
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
     * Reads a conquest map from a file.
     *
     * @param p_fileName The name of the map file to read.
     */
    public void readConquestMap(String p_fileName) {
        BufferedReader l_reader = null;

        try {
            File l_file = new File(p_fileName);
            if (l_file.exists()) {
                l_reader = new BufferedReader(new FileReader(l_file));
            } else {
                String l_resourcePath = p_fileName;
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
            String l_line;
            boolean inTerritories = false;

            java.util.Map<String, Territory> l_territoryMap = new HashMap<>();
            java.util.Map<String, List<String>> l_neighborsMap = new HashMap<>();
            Set<String> l_continents = new HashSet<>();

            while ((l_line = l_reader.readLine()) != null) {
                l_line = l_line.trim();

                if (l_line.startsWith(";") || l_line.isEmpty()) {
                    continue;
                }

                if (l_line.equals("[Territories]")) {
                    inTerritories = true;
                    continue;
                }

                if (inTerritories) {
                    String[] l_parts = l_line.split(",");
                    if (l_parts.length < 4) continue;

                    String l_countryName = l_parts[0].trim();
                    String l_continentName = l_parts[3].trim();

                    l_continents.add(l_continentName);

                    Territory l_territory = new Territory(l_countryName, l_continentName, 0); // bonus = 0 (ignored)
                    l_territoryMap.put(l_countryName, l_territory);
                    d_loadedMap.addTerritory(l_territory);

                    List<String> l_neighbors = new ArrayList<>();
                    for (int i = 4; i < l_parts.length; i++) {
                        l_neighbors.add(l_parts[i].trim());
                    }
                    l_neighborsMap.put(l_countryName, l_neighbors);
                }
            }

            // Assign neighbors
            for (java.util.Map.Entry<String, List<String>> entry : l_neighborsMap.entrySet()) {
                Territory l_territory = l_territoryMap.get(entry.getKey());
                for (String l_neighborName : entry.getValue()) {
                    Territory l_neighbor = l_territoryMap.get(l_neighborName);
                    if (l_neighbor != null) {
                        l_territory.addNeighbor(l_neighbor);
                    } else {
                        System.err.println("Neighbor not found: " + l_neighborName);
                    }
                }
            }

            // Optionally, set continents with dummy bonuses (0)
            java.util.Map<String, Integer> l_continentMap = new HashMap<>();
            for (String l_continentName : l_continents) {
                l_continentMap.put(l_continentName, 0);
            }
            d_loadedMap.setContinents(l_continentMap);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (l_reader != null) {
                    l_reader.close();
                    validateMap(true); // validate true = conquest
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads a domination map from a file.
     *
     * @param p_fileName The name of the map file to read.
     */
    public void readDominationMap(String p_fileName) {
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

                    validateMap(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
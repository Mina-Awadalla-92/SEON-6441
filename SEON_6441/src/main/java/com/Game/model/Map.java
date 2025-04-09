package com.Game.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;

/**
 * Represents a game map consisting of territories and continents.
 * Provides methods to manage territories, neighbors, continents, and save the map to a file.
 */
public class Map {

    /**
     * A list of all territories in the game.
     */
    private List<Territory> d_territoryList;

    /**
     * A map of continent names to their control values.
     * The key is the continent name, and the value is the control value associated with that continent.
     */
    private java.util.Map<String, Integer> d_continents;

    /**
     * Indicates whether all territories in the game are unique.
     */
    private boolean d_hasUniqueTerritories;

    /**
     * Default constructor that initializes an empty map with no territories or continents.
     */
    public Map() {
        this.d_territoryList = new ArrayList<>();
        this.d_continents = new HashMap<>();
        this.d_hasUniqueTerritories = true;
    }

    /**
     * Copy constructor that creates a deep copy of the provided map.
     *
     * @param p_map The map to copy.
     */
    public Map(Map p_map) {
        this();
        this.d_hasUniqueTerritories = p_map.d_hasUniqueTerritories;
        for (Territory l_territory : p_map.d_territoryList) {
            this.d_territoryList.add(new Territory(l_territory));
        }
        this.d_continents = new HashMap<>(p_map.d_continents);
    }

    /**
     * Retrieves the list of territories in the map.
     *
     * @return A list of territories.
     */
    public List<Territory> getTerritoryList() {
        return d_territoryList;
    }

    /**
     * Sets the territory list.
     * 
     * @param p_territoryList The territory list to set.
     */
    public void setTerritoryList(List<Territory> p_territoryList) {
        this.d_territoryList = p_territoryList;
    }

    /**
     * Gets the continents map.
     * 
     * @return The map of continents to their control values.
     */
    public java.util.Map<String, Integer> getContinents() {
        return d_continents;
    }

    /**
     * Adds a new territory to the map. Ensures that each territory has a unique name.
     *
     * @param p_newTerritory The territory to be added.
     */
    public void addTerritory(Territory p_newTerritory) {
        if (getTerritoryByName(p_newTerritory.getName()) != null) {
            d_hasUniqueTerritories = false;
        }
        d_territoryList.add(p_newTerritory);
    }

    /**
     * Retrieves a territory by its name.
     *
     * @param p_name The name of the territory.
     * @return The territory if found, otherwise null.
     */
    public Territory getTerritoryByName(String p_name) {
        for (Territory l_territory : d_territoryList) {
            if (l_territory.getName().equals(p_name)) {
                return l_territory;
            }
        }
        return null;
    }

    /**
     * Returns a string representation of the map, listing all territories.
     *
     * @return A string containing the map details.
     */
    @Override
    public String toString() {
        StringBuilder l_sb = new StringBuilder("****Map*****\n");
        for (Territory l_territory : d_territoryList) {
            l_sb.append(l_territory).append("\n");
        }
        return l_sb.toString();
    }

    /**
     * Adds a continent with a given ID and value.
     *
     * @param p_continentID The unique identifier for the continent.
     * @param p_continentValue The value associated with the continent.
     */
    public void addContinent(String p_continentID, int p_continentValue) {
        d_continents.put(p_continentID, p_continentValue);
    }

    /**
     * Removes a continent and all territories associated with it.
     *
     * @param p_continentID The ID of the continent to remove.
     */
    public void removeContinent(String p_continentID) {
        d_continents.remove(p_continentID);
        d_territoryList.removeIf(t -> t.getContinent().equals(p_continentID));
    }

    /**
     * Adds a country (territory) to a continent if the continent exists.
     *
     * @param p_countryID The name of the country.
     * @param p_continentID The ID of the continent the country belongs to.
     */
    public void addCountry(String p_countryID, String p_continentID) {
        if (!d_continents.containsKey(p_continentID)) {
            System.out.println("Continent does not exist.");
            return;
        }
        d_territoryList.add(new Territory(p_countryID, p_continentID, d_continents.get(p_continentID)));
    }

    /**
     * Removes a country (territory) from the map.
     *
     * @param p_countryID The name of the country to remove.
     */
    public void removeCountry(String p_countryID) {
        // First, remove p_countryID from the neighbor lists of all territories
        for (Territory l_territory : d_territoryList) {
            l_territory.getNeighborList().removeIf(neighbor -> neighbor.getName().equals(p_countryID));
        }
        // Then, remove the country itself from the territory list
        d_territoryList.removeIf(t -> t.getName().equals(p_countryID));
    }

    /**
     * Adds a neighboring relationship between two territories.
     *
     * @param p_countryID The ID of the country.
     * @param p_neighborCountryID The ID of the neighboring country.
     */
    public void addNeighbor(String p_countryID, String p_neighborCountryID) {
        Territory l_country = getTerritoryByName(p_countryID);
        Territory l_neighbor = getTerritoryByName(p_neighborCountryID);
        if (l_country != null && l_neighbor != null) {
            l_country.addNeighbor(l_neighbor);
            l_neighbor.addNeighbor(l_country);
        }
    }

    /**
     * Removes a neighboring relationship between two territories.
     *
     * @param p_countryID The ID of the country.
     * @param p_neighborCountryID The ID of the neighboring country.
     */
    public void removeNeighbor(String p_countryID, String p_neighborCountryID) {
        Territory l_country = getTerritoryByName(p_countryID);
        Territory l_neighbor = getTerritoryByName(p_neighborCountryID);
        if (l_country != null && l_neighbor != null) {
            l_country.getNeighborList().remove(l_neighbor);
            l_neighbor.getNeighborList().remove(l_country);
        }
    }

    /**
     * Sets the continents of the map.
     *
     * @param p_continents A map of continent IDs to their corresponding values.
     */
    public void setContinents(java.util.Map<String, Integer> p_continents) {
        this.d_continents = new HashMap<>(p_continents);
    }

    /**
     * Saves the map structure to a file in a specific format.
     * The file includes continents, countries, and their neighboring relationships.
     *
     * @param p_filePath The path to the file where the map should be saved.
     */
    public void saveToFile(String p_filePath) {
        // Debugging: Print the absolute path to confirm it's correct
        System.out.println("Saving to file: " + new File(p_filePath).getAbsolutePath());

        // Check if the directory exists, create it if not
        File l_file = new File(p_filePath);
        File l_parentDir = l_file.getParentFile();
        if (l_parentDir != null && !l_parentDir.exists()) {
            System.out.println("Directory does not exist. Creating directory: " + l_parentDir.getAbsolutePath());
            l_parentDir.mkdirs();  // Creates the directories if they do not exist
        }

        try (BufferedWriter l_writer = new BufferedWriter(new FileWriter(l_file))) {
            // Writing continents section
            l_writer.write("[continents]\n");
            for (java.util.Map.Entry<String, Integer> l_entry : d_continents.entrySet()) {
                l_writer.write(l_entry.getKey() + " " + l_entry.getValue() + "\n");
            }
            l_writer.write("\n");

            // Writing countries section
            l_writer.write("[countries]\n");
            for (int i = 0; i < d_territoryList.size(); i++) {
                Territory l_t = d_territoryList.get(i);
                l_writer.write((i + 1) + " " + l_t.getName() + " " + (new ArrayList<>(d_continents.keySet()).indexOf(l_t.getContinent()) + 1) + "\n");
            }
            l_writer.write("\n");

            // Writing borders section
            l_writer.write("[borders]\n");
            for (int i = 0; i < d_territoryList.size(); i++) {
                Territory l_t = d_territoryList.get(i);
                l_writer.write((i + 1) + "");
                for (Territory l_neighbor : l_t.getNeighborList()) {
                    l_writer.write(" " + (d_territoryList.indexOf(l_neighbor) + 1));
                }
                l_writer.write("\n");
                l_writer.flush(); // Forces any data in the buffer to be written to the file
            }

            System.out.println("File saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving the file: " + e.getMessage());
        }
    }

    /**
     * Saves the current map in Conquest format to a specified file.
     *
     * <p>Includes sections for map metadata, continents with bonuses, and territories with coordinates and neighbors.</p>
     *
     * @param p_filePath The path to save the map, including the file name.
     */
    public void saveToConquestFile(String p_filePath) {
        // Debugging: Print the absolute path to confirm it's correct
        System.out.println("Saving to Conquest file: " + new File(p_filePath).getAbsolutePath());

        // Check if the directory exists, create it if not
        File l_file = new File(p_filePath);
        File l_parentDir = l_file.getParentFile();
        if (l_parentDir != null && !l_parentDir.exists()) {
            System.out.println("Directory does not exist. Creating directory: " + l_parentDir.getAbsolutePath());
            l_parentDir.mkdirs();  // Creates the directories if they do not exist
        }

        try (BufferedWriter l_writer = new BufferedWriter(new FileWriter(l_file))) {
            // Writing Map section
            l_writer.write("[Map]\n");

            // Writing Continents section
            l_writer.write("[Continents]\n");
            for (java.util.Map.Entry<String, Integer> l_entry : d_continents.entrySet()) {
                l_writer.write(l_entry.getKey() + "=" + l_entry.getValue() + "\n");
            }
            l_writer.write("\n");

            // Writing Territories section
            l_writer.write("[Territories]\n");
            for (int i = 0; i < d_territoryList.size(); i++) {
                Territory l_t = d_territoryList.get(i);
                // Territory format: Name,x,y,Continent,Neighbor1,Neighbor2,...
                StringBuilder territoryLine = new StringBuilder(l_t.getName() + ",");

                // Add territory coordinates (example: dummy data for now, replace with real coordinates)
                territoryLine.append(11 + "," + 22+ ",");  // Replace with actual X,Y values if needed

                // Add continent name
                territoryLine.append(l_t.getContinent() + ",");

                // Add neighbors
                for (Territory l_neighbor : l_t.getNeighborList()) {
                    territoryLine.append(l_neighbor.getName() + ",");
                }
                // Remove the trailing comma if any
                if (territoryLine.charAt(territoryLine.length() - 1) == ',') {
                    territoryLine.deleteCharAt(territoryLine.length() - 1);
                }

                l_writer.write(territoryLine.toString() + "\n");
            }
            l_writer.write("\n");

            // Success message
            System.out.println("Conquest map file saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving the Conquest file: " + e.getMessage());
        }
    }

    /**
     * Saves the current state of the game to a map file.
     *
     * <p>This method creates or overwrites a file with the specified name (appending ".map" if not present)
     * inside the {@code /resources/LoadingMaps} directory. It writes details about the players, their owned
     * territories, and delegates the map saving logic to {@code saveMapToFile()}.</p>
     *
     * @param p_fileName The name of the file to which the game state should be saved.
     * @param d_players  A list of players whose game state (including owned territories) is to be saved.
     */
    public void saveGameState(String p_fileName, List<Player> d_players) {
        // Ensure the file name ends with .map
        if (!p_fileName.endsWith(".map")) {
            p_fileName += ".map";
        }

        String directoryPath = Paths.get(System.getProperty("user.dir"),
                        "SEON_6441", "src", "main", "resources", "LoadingMaps")
                .toAbsolutePath().toString();
        File l_file = new File(directoryPath, p_fileName);

        // Debugging: Print the absolute path to confirm it's correct
        System.out.println("Saving game state to file: " + l_file.getAbsolutePath());

        File l_parentDir = l_file.getParentFile();
        if (l_parentDir != null && !l_parentDir.exists()) {
            System.out.println("Directory does not exist. Creating directory: " + l_parentDir.getAbsolutePath());
            l_parentDir.mkdirs();
        }

        try (BufferedWriter l_writer = new BufferedWriter(new FileWriter(l_file))) {
            // Writing game state header
            l_writer.write("[Game is Saved]\n\n");

            // Writing players section
            l_writer.write("[Players]\n");
            for (Player l_player : d_players) {
                l_writer.write(l_player.getName() + " " + l_player.getClass().getSimpleName() + "\n");
            }
            l_writer.write("\n");

            l_writer.write("[Territory Owner]\n");
            for (Player l_player : d_players) {
                for (Territory l_territory : l_player.getOwnedTerritories()) {
                    l_writer.write(l_player.getName() + " owns " + l_territory.getName() +
                            " with " + l_territory.getNumOfArmies() + " armies\n");
                }
            }
            l_writer.write("\n");

            // Now call saveMapToFile to write the map information
            saveMapToFile(l_writer);

            System.out.println("Game state saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving the game state: " + e.getMessage());
        }
    }

    /**
     * Writes the map data to the provided {@link BufferedWriter} in a structured format.
     *
     * <p>The map data includes:</p>
     * <ul>
     *   <li>The list of continents with their control values under the <b>[continents]</b> section.</li>
     *   <li>The list of countries (territories) with their continent association under the <b>[countries]</b> section.</li>
     *   <li>The adjacency (borders) between countries under the <b>[borders]</b> section.</li>
     * </ul>
     *
     * <p>This method is typically called as part of saving the entire game state.</p>
     *
     * @param p_writer The writer used to output the map data to a file.
     * @throws IOException If an I/O error occurs during writing.
     */
    private void saveMapToFile(BufferedWriter p_writer) throws IOException {
        // Writing continents section
        p_writer.write("[continents]\n");
        for (java.util.Map.Entry<String, Integer> l_entry : d_continents.entrySet()) {
            p_writer.write(l_entry.getKey() + " " + l_entry.getValue() + "\n");
        }
        p_writer.write("\n");

        // Writing countries section
        p_writer.write("[countries]\n");
        for (int i = 0; i < d_territoryList.size(); i++) {
            Territory l_t = d_territoryList.get(i);
            p_writer.write((i + 1) + " " + l_t.getName() + " " + (new ArrayList<>(d_continents.keySet()).indexOf(l_t.getContinent()) + 1) + "\n");
        }
        p_writer.write("\n");

        // Writing borders section
        p_writer.write("[borders]\n");
        for (int i = 0; i < d_territoryList.size(); i++) {
            Territory l_t = d_territoryList.get(i);
            p_writer.write((i + 1) + "");
            for (Territory l_neighbor : l_t.getNeighborList()) {
                p_writer.write(" " + (d_territoryList.indexOf(l_neighbor) + 1));
            }
            p_writer.write("\n");
            p_writer.flush();
        }
    }

    /**
     * Validates if the entire map is a connected graph.
     *
     * @return true if the map is connected, otherwise false.
     */
    public boolean mapValidation() {
        if (d_territoryList.isEmpty()) return false;

        // Track visited territories
        boolean[] l_visited = new boolean[d_territoryList.size()];
        dfs(0, l_visited);  // Start DFS from the first territory

        // Check if all territories were visited
        for (boolean l_isVisited : l_visited) {
            if (!l_isVisited) return false;
        }
        return true;
    }

    /**
     * Validates if each continent in the map is a connected subgraph.
     *
     * @return true if all continents are connected subgraphs, otherwise false.
     */
    public boolean continentValidation() {
        // Iterate over each continent
        for (String l_continent : d_continents.keySet()) {
            if (!isContinentConnected(l_continent)) {
                return false;  // If any continent is not connected, return false
            }
        }
        return true;
    }

    /**
     * Checks if a specific continent is a connected subgraph.
     *
     * @param p_continentID The ID of the continent to validate.
     * @return true if the continent is connected, otherwise false.
     */
    private boolean isContinentConnected(String p_continentID) {
        List<Territory> l_continentTerritories = new ArrayList<>();

        // Collect territories belonging to the specified continent
        for (Territory l_territory : d_territoryList) {
            if (l_territory.getContinent().equals(p_continentID)) {
                l_continentTerritories.add(l_territory);
            }
        }

        if (l_continentTerritories.isEmpty()) return false;

        // Track visited territories within the continent
        boolean[] l_visited = new boolean[l_continentTerritories.size()];
        dfs(l_continentTerritories, 0, l_visited);  // Start DFS from the first continent territory

        // Check if all territories in the continent were visited
        for (boolean l_isVisited : l_visited) {
            if (!l_isVisited) return false;
        }
        return true;
    }

    /**
     * Depth-First Search (DFS) to explore territories.
     *
     * @param p_index Starting index for DFS.
     * @param p_visited Array to track visited territories.
     */
    private void dfs(int p_index, boolean[] p_visited) {
        p_visited[p_index] = true;
        Territory l_current = d_territoryList.get(p_index);

        // Explore neighbors
        for (Territory l_neighbor : l_current.getNeighborList()) {
            int l_neighborIndex = d_territoryList.indexOf(l_neighbor);
            if (l_neighborIndex != -1 && !p_visited[l_neighborIndex]) {
                dfs(l_neighborIndex, p_visited);
            }
        }
    }

    /**
     * Depth-First Search (DFS) for a specific continent.
     *
     * @param p_continentTerritories List of territories in the continent.
     * @param p_index Starting index for DFS within the continent.
     * @param p_visited Array to track visited territories in the continent.
     */
    private void dfs(List<Territory> p_continentTerritories, int p_index, boolean[] p_visited) {
        p_visited[p_index] = true;
        Territory l_current = p_continentTerritories.get(p_index);

        // Explore neighbors within the same continent
        for (Territory l_neighbor : l_current.getNeighborList()) {
            if (l_neighbor.getContinent().equals(l_current.getContinent())) {
                int l_neighborIndex = p_continentTerritories.indexOf(l_neighbor);
                if (l_neighborIndex != -1 && !p_visited[l_neighborIndex]) {
                    dfs(p_continentTerritories, l_neighborIndex, p_visited);
                }
            }
        }
    }
}
package com.Game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        for (Territory territory : p_map.d_territoryList) {
            this.d_territoryList.add(new Territory(territory));
        }
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
        for (Territory territory : d_territoryList) {
            if (territory.getName().equals(p_name)) {
                return territory;
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
        for (Territory territory : d_territoryList) {
            l_sb.append(territory).append("\n");
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
        for (Territory territory : d_territoryList) {
            territory.getNeighborList().removeIf(neighbor -> neighbor.getName().equals(p_countryID));
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(l_file))) {
            // Writing continents section
            writer.write("[continents]\n");
            for (java.util.Map.Entry<String, Integer> entry : d_continents.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            writer.write("\n");

            // Writing countries section
            writer.write("[countries]\n");
            for (int i = 0; i < d_territoryList.size(); i++) {
                Territory l_t = d_territoryList.get(i);
                writer.write((i + 1) + " " + l_t.getName() + " " + (new ArrayList<>(d_continents.keySet()).indexOf(l_t.getContinent()) + 1) + "\n");
            }
            writer.write("\n");

            // Writing borders section
            writer.write("[borders]\n");
            for (int i = 0; i < d_territoryList.size(); i++) {
                Territory l_t = d_territoryList.get(i);
                writer.write((i + 1) + "");
                for (Territory neighbor : l_t.getNeighborList()) {
                    writer.write(" " + (d_territoryList.indexOf(neighbor) + 1));
                }
                writer.write("\n");
                writer.flush(); // Forces any data in the buffer to be written to the file

            }

            System.out.println("File saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving the file: " + e.getMessage());
        }
    }
}
package com.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a territory in the game. A territory is associated with a continent, has a certain number of armies,
 * and can have neighbors (other territories). It also keeps track of its owner, reserved armies, and whether it has been visited.
 */
public class Territory {

    /**
     * The name of the territory.
     */
    private String d_name;

    /**
     * The continent to which the territory belongs.
     */
    private String d_continent;

    /**
     * The bonus value associated with controlling the continent of this territory.
     */
    private int d_bonus;

    /**
     * The number of armies currently stationed in the territory.
     */
    private int d_numOfArmies;

    /**
     * The player who owns the territory.
     */
    private Player d_owner;

    /**
     * The number of reserved armies in the territory, if any.
     */
    private int d_numOfReservedArmies;

    /**
     * Indicates whether the territory has been visited during traversal or search operations.
     */
    private boolean d_isVisited;

    /**
     * The list of neighboring territories connected to this territory.
     */
    private List<Territory> d_neighborList;

    /**
     * Constructor to initialize a territory with a name, continent, and bonus value.
     *
     * @param name The name of the territory.
     * @param continent The continent the territory belongs to.
     * @param bonus The bonus value of the territory.
     */
    public Territory(String name, String continent, int bonus) {
        this.d_name = name;
        this.d_continent = continent;
        this.d_bonus = bonus;
        this.d_numOfArmies = 0;
        this.d_owner = null;
        this.d_numOfReservedArmies = 0;
        this.d_isVisited = false;
        this.d_neighborList = new ArrayList<>();
    }

    /**
     * Copy constructor that creates a new Territory object based on another Territory.
     *
     * @param p_territory The Territory to copy.
     */
    public Territory(Territory p_territory) {
        this(p_territory.d_name, p_territory.d_continent, p_territory.d_bonus);
        this.d_numOfArmies = p_territory.d_numOfArmies;
        this.d_numOfReservedArmies = p_territory.d_numOfReservedArmies;
        this.d_owner = p_territory.d_owner;

        for (Territory neighbor : p_territory.d_neighborList) {
            this.d_neighborList.add(neighbor);
        }
    }

    /**
     * Gets the name of the territory.
     *
     * @return The name of the territory.
     */
    public String getName() { return d_name; }

    /**
     * Gets the continent that the territory belongs to.
     *
     * @return The continent of the territory.
     */
    public String getContinent() { return d_continent; }

    /**
     * Gets the bonus value of the territory.
     *
     * @return The bonus value of the territory.
     */
    public int getBonus() { return d_bonus; }

    /**
     * Gets the number of armies in the territory.
     *
     * @return The number of armies in the territory.
     */
    public int getNumOfArmies() { return d_numOfArmies; }

    /**
     * Sets the number of armies in the territory.
     *
     * @param p_numOfArmies The number of armies to set.
     */
    public void setNumOfArmies(int p_numOfArmies) {
        this.d_numOfArmies = p_numOfArmies; }

    /**
     * Gets the owner of the territory.
     *
     * @return The player who owns the territory.
     */
    public Player getOwner() { return d_owner; }

    /**
     * Sets the owner of the territory.
     *
     * @param p_owner The player to set as the owner.
     */
    public void setOwner(Player p_owner) { this.d_owner = p_owner; }

    /**
     * Gets the number of reserved armies in the territory.
     *
     * @return The number of reserved armies in the territory.
     */
    public int getNumOfReservedArmies() { return d_numOfReservedArmies; }

    /**
     * Sets the number of reserved armies in the territory.
     *
     * @param p_numOfReservedArmies The number of reserved armies to set.
     */
    public void setNumOfReservedArmies(int p_numOfReservedArmies) {
        this.d_numOfReservedArmies = p_numOfReservedArmies; }

    /**
     * Gets the list of neighboring territories.
     *
     * @return A list of neighboring territories.
     */
    public List<Territory> getNeighborList() { return d_neighborList; }

    /**
     * Checks if a given territory is a neighbor of this territory.
     *
     * @param p_target The target territory to check.
     * @return true if the target territory is a neighbor, false otherwise.
     */
    public boolean hasNeighbor(Territory p_target) {

        return d_neighborList.contains(p_target);
    }

    /**
     * Adds a neighbor territory to the territory.
     *
     * @param p_neighbor The neighbor territory to add.
     */
    public void addNeighbor(Territory p_neighbor) {

        d_neighborList.add(p_neighbor);
    }

    /**
     * Gets a list of enemy neighbors. These are territories that are owned by a different player.
     *
     * @return A list of enemy neighbor territories.
     */
    public List<Territory> getEnemyNeighbors() {
        List<Territory> l_enemyNeighbors = new ArrayList<>();
        for (Territory neighbor : d_neighborList) {
            if (neighbor.getOwner() != this.d_owner) {
                l_enemyNeighbors.add(neighbor);
            }
        }
        return l_enemyNeighbors;
    }

    /**
     * Checks if this territory is equal to another territory based on name and continent.
     *
     * @param p_obj The object to compare this territory to.
     * @return true if the territories are equal, false otherwise.
     */
    @Override
    public boolean equals(Object p_obj) {
        if (p_obj == null || p_obj.getClass() != this.getClass()) {
            return false;
        }
        Territory l_t = (Territory)p_obj;
        return l_t.getName() == this.d_name && l_t.getContinent() == this.d_continent;
    }

    /**
     * Returns a string representation of the territory, including its name, continent, number of armies, and neighbors.
     *
     * @return A string representing the territory.
     */
    @Override
    public String toString() {
        StringBuilder l_sb = new StringBuilder();
        l_sb.append("Territory Name: ").append(d_name)
                .append(" Continent: ").append(d_continent)
                .append(" Number of Armies: ").append(d_numOfArmies)
                .append(" Neighbors: ");
        for (Territory neighbor : d_neighborList) {
            l_sb.append(neighbor.getName()).append(", ");
        }
        return l_sb.toString();
    }
}
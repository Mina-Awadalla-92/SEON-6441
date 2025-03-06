package com.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a territory in the game. A territory is associated with a continent, has a certain number of armies,
 * and can have neighbors (other territories). It also keeps track of its owner, reserved armies, and whether it has been visited.
 */
public class Territory {
    private String l_name;
    private String l_continent;
    private int l_bonus;
    private int l_numOfArmies;
    private Player l_owner;
    private int l_numOfReservedArmies;
    private boolean l_isVisited;
    private List<Territory> l_neighborList;

    /**
     * Constructor to initialize a territory with a name, continent, and bonus value.
     *
     * @param name The name of the territory.
     * @param continent The continent the territory belongs to.
     * @param bonus The bonus value of the territory.
     */
    public Territory(String name, String continent, int bonus) {
        this.l_name = name;
        this.l_continent = continent;
        this.l_bonus = bonus;
        this.l_numOfArmies = 0;
        this.l_owner = null;
        this.l_numOfReservedArmies = 0;
        this.l_isVisited = false;
        this.l_neighborList = new ArrayList<>();
    }

    /**
     * Copy constructor that creates a new Territory object based on another Territory.
     *
     * @param p_territory The Territory to copy.
     */
    public Territory(Territory p_territory) {
        this(p_territory.l_name, p_territory.l_continent, p_territory.l_bonus);
        this.l_numOfArmies = p_territory.l_numOfArmies;
        this.l_numOfReservedArmies = p_territory.l_numOfReservedArmies;
        this.l_owner = p_territory.l_owner;

        for (Territory neighbor : p_territory.l_neighborList) {
            this.l_neighborList.add(neighbor);
        }
    }

    /**
     * Gets the name of the territory.
     *
     * @return The name of the territory.
     */
    public String getName() { return l_name; }

    /**
     * Gets the continent that the territory belongs to.
     *
     * @return The continent of the territory.
     */
    public String getContinent() { return l_continent; }

    /**
     * Gets the bonus value of the territory.
     *
     * @return The bonus value of the territory.
     */
    public int getBonus() { return l_bonus; }

    /**
     * Gets the number of armies in the territory.
     *
     * @return The number of armies in the territory.
     */
    public int getNumOfArmies() { return l_numOfArmies; }

    /**
     * Sets the number of armies in the territory.
     *
     * @param p_numOfArmies The number of armies to set.
     */
    public void setNumOfArmies(int p_numOfArmies) {
        this.l_numOfArmies = p_numOfArmies; }

    /**
     * Gets the owner of the territory.
     *
     * @return The player who owns the territory.
     */
    public Player getOwner() { return l_owner; }

    /**
     * Sets the owner of the territory.
     *
     * @param p_owner The player to set as the owner.
     */
    public void setOwner(Player p_owner) { this.l_owner = p_owner; }

    /**
     * Gets the number of reserved armies in the territory.
     *
     * @return The number of reserved armies in the territory.
     */
    public int getNumOfReservedArmies() { return l_numOfReservedArmies; }

    /**
     * Sets the number of reserved armies in the territory.
     *
     * @param p_numOfReservedArmies The number of reserved armies to set.
     */
    public void setNumOfReservedArmies(int p_numOfReservedArmies) {
        this.l_numOfReservedArmies = p_numOfReservedArmies; }

    /**
     * Gets the list of neighboring territories.
     *
     * @return A list of neighboring territories.
     */
    public List<Territory> getNeighborList() { return l_neighborList; }

    /**
     * Checks if a given territory is a neighbor of this territory.
     *
     * @param p_target The target territory to check.
     * @return true if the target territory is a neighbor, false otherwise.
     */
    public boolean hasNeighbor(Territory p_target) {

        return l_neighborList.contains(p_target);
    }

    /**
     * Adds a neighbor territory to the territory.
     *
     * @param p_neighbor The neighbor territory to add.
     */
    public void addNeighbor(Territory p_neighbor) {

        l_neighborList.add(p_neighbor);
    }

    /**
     * Gets a list of enemy neighbors. These are territories that are owned by a different player.
     *
     * @return A list of enemy neighbor territories.
     */
    public List<Territory> getEnemyNeighbors() {
        List<Territory> enemyNeighbors = new ArrayList<>();
        for (Territory neighbor : l_neighborList) {
            if (neighbor.getOwner() != this.l_owner) {
                enemyNeighbors.add(neighbor);
            }
        }
        return enemyNeighbors;
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
        Territory t = (Territory)p_obj;
        return t.getName() == this.l_name && t.getContinent() == this.l_continent;
    }

    /**
     * Returns a string representation of the territory, including its name, continent, number of armies, and neighbors.
     *
     * @return A string representing the territory.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Territory Name: ").append(l_name)
                .append(" Continent: ").append(l_continent)
                .append(" Number of Armies: ").append(l_numOfArmies)
                .append(" Neighbors: ");
        for (Territory neighbor : l_neighborList) {
            sb.append(neighbor.getName()).append(", ");
        }
        return sb.toString();
    }
}
package com.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents a player in the game with territories and orders.
 */
public class Player {

    private String l_name;
    private int l_nbrOfReinforcementArmies;
    private List<Territory> l_ownedTerritories;
    private List<Order> l_orders;

    /**
     * Constructor initializing player with a name.
     * @param p_name Player's name
     */
    public Player(String p_name) {
        this.l_name = p_name;
        this.l_ownedTerritories = new ArrayList<>();
        this.l_orders = new ArrayList<>();
        this.l_nbrOfReinforcementArmies = 0;
    }

    /**
     * Constructor initializing player with a name and reinforcement armies.
     * @param p_name Player's name
     * @param p_nbrOfReinforcementArmies Number of reinforcement armies
     */
    public Player(String p_name, int p_nbrOfReinforcementArmies) {
        this.l_name = p_name;
        this.l_ownedTerritories = new ArrayList<>();
        this.l_orders = new ArrayList<>();
        this.l_nbrOfReinforcementArmies = p_nbrOfReinforcementArmies;
    }

    /**
     * Adds a territory to the player's owned territories.
     * @param p_territory Territory to be added
     */
    public void addTerritory(Territory p_territory) {
        l_ownedTerritories.add(p_territory);
    }

    /**
     * Removes a territory from the player's owned territories.
     * @param p_territory Territory to be removed
     */
    public void removeTerritory(Territory p_territory) {
        l_ownedTerritories.remove(p_territory);
    }

    /**
     * Allows the player to issue an order based on user input.
     */
    public void issueOrder() {
    	
    	Scanner l_scanner = new Scanner(System.in);
    	
    	while(true) {
    		System.out.print(this.l_name + "'s order: ");
            
            String l_command = l_scanner.nextLine();
            String[] l_commandParts = l_command.split(" ");
           

            if (l_commandParts.length < 3) {
                System.out.println("Invalid command format. Usage: <OrderType> <territoryName> <numArmies>");
                continue;
            }

            String l_orderType = l_commandParts[0];
            String l_targetTerritoryName = l_commandParts[1];
            int l_numberOfArmies;
            
            try {
                l_numberOfArmies = Integer.parseInt(l_commandParts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number of armies: " + l_commandParts[2]);
                continue;
            }

            Territory l_targetTerritory = findTerritoryByName(l_targetTerritoryName);

            if (l_targetTerritory == null) {
                System.out.println("Territory not found!: " + l_commandParts[1]);
                continue;
            }

            if (l_numberOfArmies > this.l_nbrOfReinforcementArmies) {
                System.out.println("Not enough reinforcements. You have " + this.l_nbrOfReinforcementArmies + " available.");
                continue;
            }
            
            
            if (l_orderType.equals("deploy")) {
                DeployOrder l_deployOrder = new DeployOrder(this, l_targetTerritory, l_numberOfArmies);
                l_orders.add(l_deployOrder);
                this.l_nbrOfReinforcementArmies -= l_numberOfArmies;
                System.out.println("Order issued: Deploy " + l_numberOfArmies + " armies to " + l_targetTerritoryName);
                break;
            }
    	}
        
        

        
    }

    /**
     * Retrieves the next order from the player's order queue.
     * @return Next order or null if queue is empty.
     */
    public Order nextOrder() {
        return l_orders.isEmpty() ? null : l_orders.remove(0);
    }

    public int getNbrOfReinforcementArmies() {
        return l_nbrOfReinforcementArmies;
    }

    public void setNbrOfReinforcementArmies(int p_nbrOfReinforcementArmies) {
        this.l_nbrOfReinforcementArmies = p_nbrOfReinforcementArmies;
    }

    public String getName() {
        return l_name;
    }

    public void setName(String p_name) {
        this.l_name = p_name;
    }

    public List<Territory> getOwnedTerritories() {
        return l_ownedTerritories;
    }

    public void setOwnedTerritories(List<Territory> p_ownedTerritories) {
        this.l_ownedTerritories = p_ownedTerritories;
    }

    public List<Order> getOrders() {
        return l_orders;
    }

    public void setOrders(List<Order> p_orders) {
        this.l_orders = p_orders;
    }

    public void clearOrders() {
        this.l_orders.clear();
    }

    /**
     * Finds a territory owned by the player based on its name.
     * @param p_territoryName Name of the territory
     * @return Territory object if found, else null
     */
    public Territory findTerritoryByName(String p_territoryName) {
        for (Territory l_territory : l_ownedTerritories) {
            if (l_territory.getName().equals(p_territoryName)) {
                return l_territory;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "\nPlayer: " + this.l_name +
               "\nNumber of Reinforcement Armies: " + this.l_nbrOfReinforcementArmies;
    }

    public static void main(String[] args) {
        
    }
}
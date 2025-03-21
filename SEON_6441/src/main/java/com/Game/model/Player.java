package com.Game.model;


import java.util.ArrayList;
import java.util.List;
import com.Game.model.order.DeployOrder;
import com.Game.model.order.Order;
import java.util.InputMismatchException;
import com.Game.model.CardType;
import com.Game.model.order.AdvanceAttack;
import com.Game.model.order.AdvanceMove;
import com.Game.model.Map;
import java.util.HashMap;


/**
 * Represents a player in the game who owns territories and can issue orders.
 * Players can deploy armies, acquire territories, and manage their reinforcements.
 */
public class Player {

    /**
     * The name of the player.
     */
    private String d_name;
    
    private boolean d_hasConqueredThisTurn;

    /**
     * The number of reinforcement armies available to the player.
     */
    private int d_nbrOfReinforcementArmies;

    /**
     * A list of territories owned by the player.
     */
    private List<Territory> d_ownedTerritories;

    /**
     * A list of orders issued by the player.
     */
    private List<Order> d_orders;
    private int d_territoriesConqueredPerTurn;
    
    
    private HashMap<CardType, Integer> cards; // Collection of cards
    
    /**
     * Constructor initializing player with a name.
     * 
     * @param p_name Player's name
     */
    public Player(String p_name) {
        this.d_name = p_name;
        this.d_ownedTerritories = new ArrayList<>();
        this.d_orders = new ArrayList<>();
        this.d_nbrOfReinforcementArmies = 0;
        this.cards = new HashMap<>();
        this.d_hasConqueredThisTurn = false;
      
    }

    /**
     * Constructor initializing player with a name and reinforcement armies.
     * 
     * @param p_name Player's name
     * @param p_nbrOfReinforcementArmies Number of reinforcement armies
     */
    public Player(String p_name, int p_nbrOfReinforcementArmies) {
        this.d_name = p_name;
        this.d_ownedTerritories = new ArrayList<>();
        this.d_orders = new ArrayList<>();
        this.d_nbrOfReinforcementArmies = p_nbrOfReinforcementArmies;
        this.cards = new HashMap<>();
        this.d_hasConqueredThisTurn = false;
    }
    
    

    public boolean getHasConqueredThisTurn() {
		return d_hasConqueredThisTurn;
	}

	public void setHasConqueredThisTurn(boolean d_hasConqueredThisTurn) {
		this.d_hasConqueredThisTurn = d_hasConqueredThisTurn;
	}

	/**
     * Adds a territory to the player's owned territories.
     * 
     * @param p_territory Territory to be added
     */
    public void addTerritory(Territory p_territory) {
        d_ownedTerritories.add(p_territory);
    }

    /**
     * Removes a territory from the player's owned territories.
     * 
     * @param p_territory Territory to be removed
     */
    public void removeTerritory(Territory p_territory) {
        d_ownedTerritories.remove(p_territory);
    }

    /**
     * Creates a deploy order based on the provided command parameters.
     * This is an MVC-friendly version that doesn't depend on direct Scanner input.
     * 
     * @param p_targetTerritoryName The name of the target territory
     * @param p_numberOfArmies The number of armies to deploy
     * @return true if order creation was successful, false otherwise
     */
    public boolean createDeployOrder(String p_targetTerritoryName, int p_numberOfArmies) {
        Territory l_targetTerritory = findTerritoryByName(p_targetTerritoryName);
        
        if (l_targetTerritory == null) {
            return false;
        }
        
        if (p_numberOfArmies > this.d_nbrOfReinforcementArmies) {
            return false;
        }
        
        DeployOrder l_deployOrder = new DeployOrder(this, l_targetTerritory, p_numberOfArmies);
        d_orders.add(l_deployOrder);
        this.d_nbrOfReinforcementArmies -= p_numberOfArmies;
        return true;
    }
    /**
     * Validates the 'deploy' command.
     * 
     * @param p_parts The split command parts (e.g. ["deploy", "TerritoryName", "5"]).
     * @return true if valid; false otherwise.
     */
    private boolean validateDeploy(String[] p_parts) {
        // Expect exactly 3 parts: deploy <territoryName> <numArmies>
        if (p_parts.length != 3) {
            return false;
        }

        // Check if the number of armies is a valid integer
        int l_numberOfArmies;
        try {
            l_numberOfArmies = Integer.parseInt(p_parts[2]);
        } catch (NumberFormatException e) {
            return false;
        }

        // Check territory exists
        String l_targetTerritoryName = p_parts[1];
        Territory l_targetTerritory = findTerritoryByName(l_targetTerritoryName);
        if (l_targetTerritory == null) {
            return false;
        }

        // Check if player has enough reinforcement armies
        if (l_numberOfArmies > this.d_nbrOfReinforcementArmies) {
            return false;
        }

        // Check if the territory belongs to this player
        if (!l_targetTerritory.getOwner().getName().equals(this.d_name)) {
            return false;
        }

        // If all checks pass, return true
        return true;
    }
    
    /**
     * Validates the 'advance' command.
     * 
     * @param p_parts The split command parts (e.g. ["advance", "FromTerritory", "ToTerritory", "3"]).
     * @param p_map   The Map object (or similar) used to find territories.
     * @return true if valid; false otherwise.
     */
    private boolean validateAdvance(String[] p_parts, Map p_map) {
        // Expect exactly 4 parts: advance <fromTerritory> <toTerritory> <numArmies>
        if (p_parts.length != 4) {
            return false;
        }

        // Check if the territories exist
        Territory l_territoryFrom = findTerritoryByName(p_parts[1]);
        Territory l_territoryTo = p_map.getTerritoryByName(p_parts[2]);
        if (l_territoryFrom == null || l_territoryTo == null) {
            System.err.println("Territorie(s) not found!");
            return false;
        }

        // Check the number of armies
        int l_numberOfArmies;
        try {
            l_numberOfArmies = Integer.parseInt(p_parts[3]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number of armies!");
            return false;
        }

        // Check if 'from' territory belongs to this player
        if (!l_territoryFrom.getOwner().getName().equals(this.getName())) {
            System.err.println(l_territoryFrom + " does not belong to " + this.getName());
            return false;
        }

        // Check adjacency
        if (!l_territoryFrom.hasNeighbor(l_territoryTo)) {
            System.err.println(l_territoryFrom.getName() + " is not adjacent to " + l_territoryTo.getName());
            return false;
        }

        // Check if we have enough armies
        if (l_territoryFrom.getNumOfArmies() - l_numberOfArmies < 0) {
            System.err.println("Not enough armies on " + l_territoryFrom.getName() + ". Only " 
                               + l_territoryFrom.getNumOfArmies() + " available!");
            return false;
        }

        // All checks pass
        return true;
    }
    
    public boolean validateBomb(String[] l_parts, Map p_map) {
    	if(l_parts.length != 2) {
    		return false;
    	}
    	
    	Territory l_territoryTo = p_map.getTerritoryByName(l_parts[1]);
    	
    	if (l_territoryTo == null) {
    		System.err.println("Invalid Territory Name!");
    		return false;
    	}
    	
    	Boolean l_found = false;
    	for(Territory l_territory: this.getOwnedTerritories()) {
    		if(l_territory.hasNeighbor(l_territoryTo)) {
    			l_found = true;
    			break;
    		}
    	}
    	if(!l_found) {
    		System.err.println(l_territoryTo.getName() + " is not adjacent to any of your owned territories!");
    		return false;
    	}
    	
    	//check cards
    	CardType[] allCardTypes = CardType.values();
    	
    	if(!this.removeCard(allCardTypes[0])) {
    		System.err.println("No Bomb cards available!");
    		return false;
    	}
    	
    	//create bomb order object
    	
    	return true;
    }

    
    public boolean issueOrder(String p_command, Map p_map) {
        // Split the command
        String[] l_parts = p_command.split(" ");
        if (l_parts.length < 2 || l_parts.length > 4) {
            // Basic sanity check: we expect 2-4 parts (e.g., "deploy X Y" or "advance A B C")
            return false;
        }

        String l_orderType = l_parts[0].toLowerCase();

        // Handle "deploy"
        if (l_orderType.equals("deploy")) {
            if (!validateDeploy(l_parts)) {
                return false;  // Validation failed
            }

            // If we reach here, validation succeeded. We can parse the input again or 
            // retrieve the same values inside the function. Let's do a quick parse:
            String l_targetTerritoryName = l_parts[1];
            int l_numberOfArmies = Integer.parseInt(l_parts[2]);

            Territory l_targetTerritory = findTerritoryByName(l_targetTerritoryName);
            DeployOrder l_deployOrder = new DeployOrder(this, l_targetTerritory, l_numberOfArmies);
            d_orders.add(l_deployOrder);

            // Decrease the player's reinforcement pool
            this.d_nbrOfReinforcementArmies -= l_numberOfArmies;

            return true;
        }

        // Handle "advance"
        else if (l_orderType.equals("advance")) {
            if (!validateAdvance(l_parts, p_map)) {
                return false;  // Validation failed
            }

            // If we reach here, validation succeeded
            // We'll re-parse the arguments
            Territory l_territoryFrom = findTerritoryByName(l_parts[1]);
            Territory l_territoryTo = p_map.getTerritoryByName(l_parts[2]);
            int l_numberOfArmies = Integer.parseInt(l_parts[3]);

            // Subtract armies from 'from' territory (we know it's safe from validation)
            l_territoryFrom.setNumOfArmies(l_territoryFrom.getNumOfArmies() - l_numberOfArmies);

            // Check if it's a friendly move or an attack
            if (l_territoryTo.getOwner().getName().equals(this.getName())) {
                // Friendly territory -> AdvanceMove
                AdvanceMove advanceMove = new AdvanceMove(this, l_territoryFrom, l_territoryTo, l_numberOfArmies);
                d_orders.add(advanceMove);
            } else {
                // Enemy territory -> AdvanceAttack
                AdvanceAttack advanceAttack = new AdvanceAttack(this, l_territoryFrom, l_territoryTo, l_numberOfArmies);
                d_orders.add(advanceAttack);
            }
            return true;
        }
        else if (l_orderType.equalsIgnoreCase("bomb")) {
        	if(!validateBomb(l_parts, p_map)) {
        		return false;
        	}
        	
        }

        // Neither deploy nor advance
        return false;
    }

    
   

    /**
     * Legacy method for issuing orders directly through user input.
     * To be refactored in future updates to follow MVC pattern.
     */
    
    
    
    public void issueOrder() {
        // Legacy method - to be fully refactored in future updates
        // Current implementation left for backward compatibility
        java.util.Scanner l_scanner = new java.util.Scanner(System.in);
        
        while(true) {
            System.out.print("Hi " + this.d_name + ", please enter your next deploy order, or type FINISH: ");
            
            String l_command = l_scanner.nextLine();
            String[] l_commandParts = l_command.split(" ");
            
            if (l_command.equalsIgnoreCase("FINISH")) {
                break;
            }
           
            if (l_commandParts.length != 3) {
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
                System.out.println("Territory not found!: " + l_targetTerritoryName);
                continue;
            }

            if (l_numberOfArmies > this.d_nbrOfReinforcementArmies) {
                System.out.println("Not enough reinforcements. You have " + this.d_nbrOfReinforcementArmies + " available.");
                continue;
            }
            
            if (l_orderType.equalsIgnoreCase("deploy")) {
                DeployOrder l_deployOrder = new DeployOrder(this, l_targetTerritory, l_numberOfArmies);
                d_orders.add(l_deployOrder);
                this.d_nbrOfReinforcementArmies -= l_numberOfArmies;
                System.out.println(this.d_name + "'s deploy order issued: Deploy " + l_numberOfArmies + " armies to " + l_targetTerritoryName);
                continue;
            }
        }
    }

    /**
     * Retrieves the next order from the player's order queue.
     * 
     * @return Next order or null if queue is empty.
     */
    public Order nextOrder() {
        return d_orders.isEmpty() ? null : d_orders.remove(0);
    }

    /**
     * Gets the number of reinforcement armies available.
     * 
     * @return The number of reinforcement armies.
     */
    public int getNbrOfReinforcementArmies() {
        return d_nbrOfReinforcementArmies;
    }

    /**
     * Sets the number of reinforcement armies available.
     * 
     * @param p_nbrOfReinforcementArmies The number of reinforcement armies to set.
     */
    public void setNbrOfReinforcementArmies(int p_nbrOfReinforcementArmies) {
        this.d_nbrOfReinforcementArmies = p_nbrOfReinforcementArmies;
    }

    /**
     * Gets the name of the player.
     * 
     * @return The player's name.
     */
    public String getName() {
        return d_name;
    }

    /**
     * Sets the name of the player.
     * 
     * @param p_name The name to set for the player.
     */
    public void setName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Gets the list of territories owned by the player.
     * 
     * @return A list of owned territories.
     */
    public List<Territory> getOwnedTerritories() {
        return d_ownedTerritories;
    }

    /**
     * Sets the list of territories owned by the player.
     * 
     * @param p_ownedTerritories The list of territories to set.
     */
    public void setOwnedTerritories(List<Territory> p_ownedTerritories) {
        this.d_ownedTerritories = p_ownedTerritories;
    }

    /**
     * Gets the list of orders issued by the player.
     * 
     * @return A list of orders.
     */
    public List<Order> getOrders() {
        return d_orders;
    }

    /**
     * Sets the list of orders issued by the player.
     * 
     * @param p_orders The list of orders to set.
     */
    public void setOrders(List<Order> p_orders) {
        this.d_orders = p_orders;
    }

    /**
     * Clears all orders issued by the player.
     */
    public void clearOrders() {
        this.d_orders.clear();
    }
    
    /**
     * Adds a new card to the player's collection.
     */
    public void addCard(CardType p_card) {
    	cards.put(p_card, cards.getOrDefault(p_card, 0) + 1);
    }
    
    public int getCardCount(CardType p_card) {
        return cards.getOrDefault(p_card, 0);
    }

    /**
     * Returns the player's current list of cards.
     */
    public HashMap<CardType, Integer> getCards() {
        return cards;
    }

    /**
     * (Optional) Use or remove a card from the player's collection.
     */
    public boolean removeCard(CardType p_card) {
        // Check if the player has at least one of this card
        if (cards.containsKey(p_card) && cards.get(p_card) > 0) {
        	cards.put(p_card, cards.get(p_card) - 1);
            // If count goes to zero, remove the entry
            if (cards.get(p_card) == 0) {
            	cards.remove(p_card);
            }
            return true;
        }
        return false;
    }
    
    public String getFormattedCards() {
        if (cards.isEmpty()) {
            return "None";
        }

        StringBuilder sb = new StringBuilder();
        for (HashMap.Entry<CardType, Integer> entry : cards.entrySet()) {
            CardType cardType = entry.getKey();
            int cardCount = entry.getValue();
            sb.append("    - ")
              .append(cardType)
              .append(": ")
              .append(cardCount)
              .append("\n");
        }
        return sb.toString();
    }

    /**
     * Finds a territory owned by the player based on its name.
     * 
     * @param p_territoryName Name of the territory
     * @return Territory object if found, else null
     */
    public Territory findTerritoryByName(String p_territoryName) {
        for (Territory l_territory : d_ownedTerritories) {
            if (l_territory.getName().equals(p_territoryName)) {
                return l_territory;
            }
        }
        return null;
    }

    /**
     * Returns a string representation of the player, including the player's name
     * and the number of reinforcement armies available.
     * 
     * @return A string representation of the player.
     */
    @Override
    public String toString() {
        return "\nPlayer: " + this.d_name +
               "\nNumber of Reinforcement Armies: " + this.d_nbrOfReinforcementArmies;
    }
}
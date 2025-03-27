package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;

/**
 * Represents an airlift move order, which moves armies from one territory to another using airlift mechanics.
 * If the target territory is neutral, it is conquered by the issuing player.
 * In the Command pattern, this is a concrete Command implementation.
 */
public class AirliftMove extends AirliftOrder {
    
    /**
     * Default constructor for AirliftMove.
     */
    public AirliftMove() {
        super();
    }
    
    /**
     * Constructs an AirliftMove with the specified issuer, source territory, target territory, 
     * and number of armies.
     * 
     * @param p_issuer The player issuing the airlift move.
     * @param p_territoryFrom The territory from which armies are airlifted.
     * @param p_territoryTo The territory receiving the airlifted armies.
     * @param p_numberOfArmies The number of armies to airlift.
     */
    public AirliftMove(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
        super(p_issuer, p_territoryFrom, p_territoryTo, p_numberOfArmies);
    }
    
    /**
     * Constructs an AirliftMove by copying data from an existing AdvanceAttack order.
     * This allows reusing the parameters of an AdvanceAttack for an airlift move.
     * 
     * @param airMove The AdvanceAttack order to copy the details from.
     */
    public AirliftMove(AdvanceAttack airMove) {
        super(airMove.getIssuer(), airMove.getD_territoryFrom(), airMove.getD_territoryTo(), airMove.getD_numberOfArmies());
    }
    
    /**
     * Executes the airlift move order.
     * Moves the specified number of armies from the source territory to the target territory.
     * If the target territory is neutral (has no owner), it is conquered by the issuing player.
     * In the Command pattern, this is the concrete implementation of the execute() method.
     */
    @Override
    public void execute() {
        //getD_territoryFrom().setNumOfArmies(getD_territoryFrom().getNumOfArmies() - getD_numberOfArmies());
        String l_logMessage;
        boolean l_conquered = false;
        
        if(getD_territoryTo().getOwner() == null) { // if its a neutral territory
            l_conquered = true;
            this.d_issuer.addTerritory(d_territoryTo);
            d_territoryTo.setOwner(d_issuer);
            
            l_logMessage = "Player " + this.d_issuer.getName() + 
                          " conquered the neutral territory " + getD_territoryTo().getName() + 
                          " using AIRLIFT with " + this.d_numberOfArmies + " armies.";
            
            System.out.println(this.d_issuer.getName() + " conquered the neutral territory " 
                + getD_territoryTo().getName() + " using AIRLIFT and now has " 
                + getD_territoryTo().getNumOfArmies() + " armies.");
        } else {
            l_logMessage = "Player " + this.d_issuer.getName() + 
                          " airlifted " + this.d_numberOfArmies + " armies from " + 
                          getD_territoryFrom().getName() + " to " + getD_territoryTo().getName() + ".";
            
            System.out.println(this.d_numberOfArmies + " armie(s) were moved from " 
                + getD_territoryFrom().getName() + " to " + getD_territoryTo().getName());
        }
        
        // Update the territory's army count
        getD_territoryTo().setNumOfArmies(getD_territoryTo().getNumOfArmies() + getD_numberOfArmies());
        
        // Add additional status info to the log
        l_logMessage += " " + getD_territoryTo().getName() + " now has " + 
                       getD_territoryTo().getNumOfArmies() + " armies.";
        
        logOrderExecution(l_logMessage);
        
        // If a territory was conquered, update the player's conquest status
        if (l_conquered) {
            this.d_issuer.setHasConqueredThisTurn(true);
            logOrderExecution("Player " + this.d_issuer.getName() + " has conquered at least one territory this turn.");
        }
    }
}
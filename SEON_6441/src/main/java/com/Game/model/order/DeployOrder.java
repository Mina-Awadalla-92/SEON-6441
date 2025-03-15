package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;

/**
 * Represents a deploy order where a player deploys armies to a target territory.
 * Deploy orders are created during the reinforcement phase and executed during the execution phase.
 */
public class DeployOrder extends Order {
    
    /**
     * The territory where armies will be deployed.
     */
    private Territory d_targetTerritory; 
    
    /**
     * The number of armies deployed in the target territory.
     */
    private int d_numberOfArmies;

    /**
     * Default constructor.
     */
    public DeployOrder() {
        super();
    }
    
    /**
     * Constructor initializing a deploy order with an issuer, target territory, and number of armies.
     * 
     * @param p_issuer The player issuing the order
     * @param p_targetTerritory The territory where armies are deployed
     * @param p_numberOfArmies The number of armies to deploy
     */
    public DeployOrder(Player p_issuer, Territory p_targetTerritory, int p_numberOfArmies) {
        super(p_issuer);
        this.d_targetTerritory = p_targetTerritory;
        this.d_numberOfArmies = p_numberOfArmies;
    }
    
    /**
     * Copy constructor.
     * 
     * @param p_order The order to copy
     */
    public DeployOrder(DeployOrder p_order) {
        this.d_issuer = p_order.d_issuer;
        this.d_targetTerritory = p_order.d_targetTerritory;
        this.d_numberOfArmies = p_order.d_numberOfArmies;
    }
    
    /**
     * Gets the target territory where armies will be deployed.
     * 
     * @return The target territory
     */
    public Territory getTargetTerritory() {
        return d_targetTerritory;
    }
    
    /**
     * Sets the target territory where armies will be deployed.
     * 
     * @param p_targetTerritory The territory to set as the target
     */
    public void setTargetTerritory(Territory p_targetTerritory) {
        this.d_targetTerritory = p_targetTerritory;
    }
    
    /**
     * Gets the number of armies to be deployed.
     * 
     * @return The number of armies
     */
    public int getNumberOfArmies() {
        return d_numberOfArmies;
    }
    
    /**
     * Sets the number of armies to be deployed.
     * 
     * @param p_numberOfArmies The number of armies to set
     */
    public void setNumberOfArmies(int p_numberOfArmies) {
        this.d_numberOfArmies = p_numberOfArmies;
    }
    
    /**
     * Executes the deploy order, adding the specified number of armies to the target territory.
     * This method will be called during the order execution phase.
     */
    @Override
    public void execute() {
        int l_currentNumberOfArmies = this.d_targetTerritory.getNumOfArmies();
        this.d_targetTerritory.setNumOfArmies(l_currentNumberOfArmies + this.d_numberOfArmies);
        
        System.out.println("Order Executed: " + this.d_targetTerritory.getName() + " has now " 
                           + this.d_targetTerritory.getNumOfArmies() + " armies.");
    }
}
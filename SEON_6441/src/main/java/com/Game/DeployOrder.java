package com.Game;

/**
 * Represents a deploy order where a player deploys armies to a target territory.
 */
public class DeployOrder extends Order {
    
    private Territory l_targetTerritory; 
    private int l_numberOfArmies; // The number of armies deployed in the target territory

    /**
     * Default constructor.
     */
    public DeployOrder() {
        super();
    }
    
    /**
     * Constructor initializing a deploy order with an issuer, target territory, and number of armies.
     * @param p_issuer The player issuing the order
     * @param p_targetTerritory The territory where armies are deployed
     * @param p_numberOfArmies The number of armies to deploy
     */
    public DeployOrder(Player p_issuer, Territory p_targetTerritory, int p_numberOfArmies) {
        super(p_issuer);
        this.l_targetTerritory = p_targetTerritory;
        this.l_numberOfArmies = p_numberOfArmies;
    }
    
    /**
     * Copy constructor.
     * @param p_order The order to copy
     */
    public DeployOrder(DeployOrder p_order) {
        this.l_issuer = p_order.l_issuer;
    }
    
    /**
     * Executes the deploy order, adding the specified number of armies to the target territory.
     */
    @Override
    public void execute() {
        int l_currentNumberOfArmies = this.l_targetTerritory.getNumOfArmies();
        l_targetTerritory.setNumOfArmies(l_currentNumberOfArmies + l_numberOfArmies);
        
        System.out.println("Order Executed: " + this.l_targetTerritory.getName() + " has now " + this.l_targetTerritory.getNumOfArmies() + " armies.");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}

package com.Game.model.order;

import com.Game.model.Player;

/**
 * Abstract class representing an order in the game.
 * Orders are commands issued by players to be executed during the game's execution phase.
 */
public abstract class Order {

    /**
     * The player who issued the order.
     */
    protected Player d_issuer;
    
    /**
     * Default constructor initializing the issuer to null.
     */
    public Order() {
        this.d_issuer = null;
    }
    
    /**
     * Constructor initializing the order with an issuer.
     * 
     * @param p_issuer The player issuing the order
     */
    public Order(Player p_issuer) {
        this.d_issuer = p_issuer;
    }
    
    /**
     * Copy constructor.
     * 
     * @param p_order The order to copy
     */
    public Order(Order p_order) {
        this.d_issuer = p_order.d_issuer;
    }
    
    /**
     * Gets the issuer of the order.
     * 
     * @return The player issuing the order
     */
    public Player getIssuer() {
        return d_issuer;
    }
    
    /**
     * Sets the issuer of the order.
     * 
     * @param p_issuer The player to set as the issuer
     */
    public void setIssuer(Player p_issuer) {
        this.d_issuer = p_issuer;
    }
    
    /**
     * Abstract method to execute the order.
     * This method must be implemented by all concrete order classes.
     */
    public abstract void execute();
}
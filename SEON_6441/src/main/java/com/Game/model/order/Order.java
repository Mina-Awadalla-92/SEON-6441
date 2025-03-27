package com.Game.model.order;

import com.Game.model.Player;
import com.Game.observer.GameLogger;

/**
 * Abstract class representing an order in the game, implementing the Command pattern.
 * Orders are commands issued by players to be executed during the game's execution phase.
 * In the Command pattern, Order serves as the Command class.
 */
public abstract class Order {

    /**
     * The player who issued the order (in Command pattern: the receiver).
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
     * In the Command pattern, this is the execute() method that carries out the command.
     */
    public abstract void execute();
    
    /**
     * Logs the execution of this order.
     * 
     * @param p_message The log message describing the execution
     */
    protected void logOrderExecution(String p_message) {
        GameLogger logger = GameLogger.getInstance();
        if (logger != null) {
            logger.logAction("Order execution: " + p_message);
        }
    }
}
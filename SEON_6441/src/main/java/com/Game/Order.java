package com.Game;

/**
 * Abstract class representing an order in the game.
 */
public abstract class Order {
    
    protected Player l_issuer;
    
    /**
     * Default constructor initializing the issuer to null.
     */
    public Order() {
        this.l_issuer = null;
    }
    
    /**
     * Constructor initializing the order with an issuer.
     * @param p_issuer The player issuing the order
     */
    public Order(Player p_issuer) {
        this.l_issuer = p_issuer;
    }
    
    /**
     * Copy constructor.
     * @param p_order The order to copy
     */
    public Order(Order p_order) {
        this.l_issuer = p_order.l_issuer;
    }
    
    /**
     * Gets the issuer of the order.
     * @return The player issuing the order
     */
    public Player getIssuer() {
        return l_issuer;
    }
    
    /**
     * Abstract method to execute the order.
     */
    public abstract void execute();
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}

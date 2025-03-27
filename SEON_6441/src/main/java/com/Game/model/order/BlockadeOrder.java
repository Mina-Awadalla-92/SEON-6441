package com.Game.model.order;
import com.Game.model.Territory;
import com.Game.model.Player;

/**
 * Represents a blockade order in the game.
 * When executed, the specified territory is turned into a neutral territory, its owner is removed, 
 * and the number of armies is tripled.
 * In the Command pattern, this is a concrete Command implementation.
 */
public class BlockadeOrder extends Order {
	
	/**
	 * The territory on which the blockade is to be executed.
	 */
	private Territory d_territoryTo;
	
	/**
	 * Default constructor for BlockadeOrder.
	 */
	public BlockadeOrder() {
		super();
	}
	
	/**
	 * Constructs a BlockadeOrder with the specified issuer and target territory.
	 * 
	 * @param p_issuer The player issuing the blockade order.
	 * @param p_territoryTo The territory to be blockaded.
	 */
	public BlockadeOrder(Player p_issuer, Territory p_territoryTo) {
		super(p_issuer);
		this.d_territoryTo = p_territoryTo;
	}
	
	/**
	 * Copy constructor for BlockadeOrder.
	 * 
	 * @param blockadeOrder The BlockadeOrder to copy.
	 */
	public BlockadeOrder(BlockadeOrder blockadeOrder) {
		super(blockadeOrder.getIssuer());
		this.d_territoryTo = blockadeOrder.getTerritoryTo();
	}
	
	/**
	 * Executes the blockade order.
	 * The method removes the territory from its current owner, sets the territory's owner to null,
	 * triples the number of armies in the territory, and prints the details of the operation.
	 * In the Command pattern, this is the concrete implementation of the execute() method.
	 */
	@Override
	public void execute() {
		System.out.println();
		int l_initialArmies = d_territoryTo.getNumOfArmies();
		System.out.println("Number of armies in " + d_territoryTo.getName() + ": " + l_initialArmies);
		System.out.println("Using BLOCKADE on " + d_territoryTo.getName() + ". Will become a neutral territory!");
		
		Player l_originalOwner = this.getIssuer();
		String l_originalOwnerName = l_originalOwner.getName();
		
		this.getIssuer().removeTerritory(d_territoryTo);
		d_territoryTo.setOwner(null);
		d_territoryTo.setNumOfArmies(d_territoryTo.getNumOfArmies() * 3);
		int l_finalArmies = d_territoryTo.getNumOfArmies();
		
		System.out.println("Number of armies in " + d_territoryTo.getName() + ": " + l_finalArmies);
		System.out.println();
		
		String l_logMessage = "Blockade Order Executed: Player " + l_originalOwnerName + 
							  " blockaded " + d_territoryTo.getName() + 
							  ". Territory is now neutral with " + l_finalArmies + 
							  " armies (tripled from " + l_initialArmies + ").";
							  
		logOrderExecution(l_logMessage);
	}
	
	/**
	 * Returns the target territory of the blockade order.
	 * 
	 * @return the d_territoryTo
	 */
	public Territory getTerritoryTo() {
		return d_territoryTo;
	}

	/**
	 * Sets the target territory for the blockade order.
	 * 
	 * @param d_territoryTo the territory to be blockaded.
	 */
	public void setTerritoryTo(Territory d_territoryTo) {
		this.d_territoryTo = d_territoryTo;
	}
}
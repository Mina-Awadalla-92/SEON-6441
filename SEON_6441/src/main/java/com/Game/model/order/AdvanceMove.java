package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;

/**
 * Represents an advance move order, which moves armies from one territory to an adjacent territory.
 * If the target territory is neutral, it is conquered by the issuer.
 * In the Command pattern, this is a concrete Command implementation.
 */
public class AdvanceMove extends AdvanceOrder {
	
	/**
	 * Default constructor for AdvanceMove.
	 */
	public AdvanceMove() {
		super();
	}
	
	/**
	 * Constructs an AdvanceMove with the specified issuer, source territory, target territory, 
	 * and number of armies.
	 * 
	 * @param p_issuer The player issuing the move.
	 * @param p_territoryFrom The territory from which armies are moved.
	 * @param p_territoryTo The territory to which armies are moved.
	 * @param p_numberOfArmies The number of armies to move.
	 */
	public AdvanceMove(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
		super(p_issuer, p_territoryFrom, p_territoryTo, p_numberOfArmies);
	}
	
	/**
	 * Copy constructor for AdvanceMove using an AdvanceAttack object.
	 * 
	 * @param advMove The AdvanceAttack instance to copy data from.
	 */
	public AdvanceMove(AdvanceAttack advMove) {
		super(advMove.getIssuer(), advMove.getD_territoryFrom(), advMove.getD_territoryTo(), advMove.getD_numberOfArmies());
	}
	
	/**
	 * Executes the advance move order.
	 * Moves the specified number of armies from the source territory to the target territory.
	 * If the target territory is neutral (has no owner), it is conquered by the issuer.
	 * In the Command pattern, this is the concrete implementation of the execute() method.
	 */
	@Override
	public void execute() {
		String l_logMessage;
		boolean l_conquered = false;
		
		// Update the target territory's army count
		getD_territoryTo().setNumOfArmies(getD_territoryTo().getNumOfArmies() + getD_numberOfArmies());
		
		if(getD_territoryTo().getOwner() == null) { // its a neutral territory
			l_conquered = true;
			this.d_issuer.addTerritory(d_territoryTo);
			d_territoryTo.setOwner(d_issuer);
			
			l_logMessage = "Player " + this.d_issuer.getName() + 
						  " advanced and conquered neutral territory " + getD_territoryTo().getName() + 
						  " with " + getD_numberOfArmies() + " armies.";
			
			System.out.println(this.d_issuer.getName() + " conquered the neutral territory " + 
							  getD_territoryTo().getName() + " and now has " + 
							  getD_territoryTo().getNumOfArmies() + " armies.");
		} else {
			l_logMessage = "Player " + this.d_issuer.getName() + 
						  " advanced " + this.d_numberOfArmies + " armies from " + 
						  getD_territoryFrom().getName() + " to " + getD_territoryTo().getName() + ".";
			
			System.out.println(this.d_numberOfArmies + " armie(s) were moved from " + 
							  getD_territoryFrom().getName() + " to " + getD_territoryTo().getName());
		}
		
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
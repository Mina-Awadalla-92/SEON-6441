package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;
import java.util.Random;

/**
 * Represents an advance attack order, which is a type of advance order where the attacking 
 * player attempts to conquer an enemy territory.
 * In the Command pattern, this is a concrete Command implementation.
 */
public class AdvanceAttack extends AdvanceOrder {
	
	/**
	 * Default constructor for AdvanceAttack.
	 */
	public AdvanceAttack() {
		super();
	}
	
	/**
	 * Constructs an AdvanceAttack with the specified issuer, source territory, target territory, 
	 * and number of armies.
	 * 
	 * @param p_issuer The player issuing the attack.
	 * @param p_territoryFrom The territory from which the attack is launched.
	 * @param p_territoryTo The territory being attacked.
	 * @param p_numberOfArmies The number of armies used in the attack.
	 */
	public AdvanceAttack(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
		super(p_issuer, p_territoryFrom, p_territoryTo, p_numberOfArmies);
	}
	
	/**
	 * Copy constructor for AdvanceAttack.
	 * 
	 * @param advAttack The AdvanceAttack instance to copy.
	 */
	public AdvanceAttack(AdvanceAttack advAttack) {
		super(advAttack.getIssuer(), advAttack.getD_territoryFrom(), advAttack.getD_territoryTo(), advAttack.getD_numberOfArmies());
	}
	
	/**
	 * Executes the advance attack order. The method simulates battle outcomes using random chance 
	 * for each army's success rate, calculates casualties, and updates territories accordingly. 
	 * If diplomacy is in effect between the issuing player and the defender, the attack is undone.
	 * In the Command pattern, this is the concrete implementation of the execute() method.
	 */
	@Override
	public void execute() {
		String l_logMessage;
		
		if(getIssuer().getNegociatedPlayersPerTurn().contains(d_territoryTo.getOwner())) {
			d_territoryFrom.setNumOfArmies(d_territoryFrom.getNumOfArmies() + d_numberOfArmies);
			
			l_logMessage = "Advance Attack cancelled: Player " + getIssuer().getName() + 
						   " has a diplomacy agreement with " + d_territoryTo.getOwner().getName();
						   
			System.out.println("Undo Attack order from: " + getIssuer().getName());
			System.out.println("Diplomacy between: " + getIssuer().getName() + " and " + d_territoryTo.getOwner().getName());
			
			logOrderExecution(l_logMessage);
			return;
		}
		
	    int attackingArmies = getD_numberOfArmies();
	    String l_opposingPlayerName = getD_territoryTo().getOwner().getName();
	    int defendingArmies = getD_territoryTo().getNumOfArmies();
	    
	    Random rand = new Random();
	    int attackerKills = 0;
	    int defenderKills = 0;
	    
	    // Each attacking army unit has a 60% chance to kill one defending army unit
	    for (int i = 0; i < attackingArmies; i++) {
	        if (rand.nextDouble() < 0.6) {
	            attackerKills++;
	        }
	    }
	    
	    // Each defending army unit has a 70% chance to kill one attacking army unit
	    for (int i = 0; i < defendingArmies; i++) {
	        if (rand.nextDouble() < 0.7) {
	            defenderKills++;
	        }
	    }
	    
	    // The actual casualties cannot exceed the number of armies on each side
	    int actualDefenderCasualties = Math.min(defendingArmies, attackerKills);
	    int actualAttackerCasualties = Math.min(attackingArmies, defenderKills);
	    
	    int survivingAttackingArmies = attackingArmies - actualAttackerCasualties;
	    int survivingDefendingArmies = defendingArmies - actualDefenderCasualties;
	    
	    boolean l_conquered = false;
	    
	    if (survivingDefendingArmies <= 0) {
	        // All defending armies are eliminated: attacker captures the territory
	    	getD_territoryTo().getOwner().removeTerritory(d_territoryTo);
	        getD_territoryTo().setOwner(getIssuer());
	        getIssuer().addTerritory(d_territoryTo);
	        // The surviving attacking armies occupy the conquered territory
	        getD_territoryTo().setNumOfArmies(survivingAttackingArmies);
	        
	        getIssuer().setHasConqueredThisTurn(true);
	        l_conquered = true;
	    } else {
	        // The defender still holds the territory
	        getD_territoryTo().setNumOfArmies(survivingDefendingArmies);
	        // The surviving attacking armies return to the original territory
	        getD_territoryFrom().setNumOfArmies(survivingAttackingArmies);
	    }
	    
	    // Log the battle result
	    System.out.println("Battle results:");
	    System.out.println(getD_territoryFrom().getName() + " (attacking) sent: " + attackingArmies + " armies");
	    System.out.println(getD_territoryTo().getName() + " (defending) had: " + defendingArmies + " armies");
	    System.out.println(getD_territoryFrom().getName() + " (attacking) inflicted " + actualDefenderCasualties + " casualties");
	    System.out.println(getD_territoryTo().getName() + " (defending) inflicted " + actualAttackerCasualties + " casualties");
	    
	    if (l_conquered) {
	        System.out.println(getIssuer().getName() + " captured "+ getD_territoryTo().getName() + 
	                          " and the territory now has " + survivingAttackingArmies + " armies.");
	        
	        l_logMessage = "Advance Attack succeeded: Player " + getIssuer().getName() + 
	                       " conquered " + getD_territoryTo().getName() + 
	                       " from " + l_opposingPlayerName + ". " +
	                       survivingAttackingArmies + " attacking armies now occupy the territory.";
	    } else {
	        System.out.println(l_opposingPlayerName +" retained " + getD_territoryTo().getName() + 
	                          " and the territory now has " + survivingDefendingArmies + " armies remaining.");
	        System.out.println(getIssuer().getName() + " retained "+ getD_territoryFrom().getName() + 
	                          " and the territory now has " + survivingAttackingArmies + " armies.");
	        
	        l_logMessage = "Advance Attack failed: Player " + getIssuer().getName() + 
	                       " attacked " + getD_territoryTo().getName() + 
	                       " owned by " + l_opposingPlayerName + ". " +
	                       "Defender retained territory with " + survivingDefendingArmies + " armies. " +
	                       survivingAttackingArmies + " attacking armies returned to " + getD_territoryFrom().getName() + ".";
	    }
	    
	    logOrderExecution(l_logMessage);
	}
}
package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;
import java.util.Random;

/**
 * Represents an airlift attack order, which is a specialized form of airlift order where
 * the attacking player attempts to capture an enemy territory using airlifted armies.
 * In the Command pattern, this is a concrete Command implementation.
 */
public class AirliftAttack extends AirliftOrder {
	
	/**
	 * Default constructor for AirliftAttack.
	 */
	public AirliftAttack() {
		super();
	}
	
	/**
	 * Constructs an AirliftAttack with the specified issuer, source territory, target territory,
	 * and number of armies.
	 * 
	 * @param p_issuer The player issuing the airlift attack.
	 * @param p_territoryFrom The territory from which armies are airlifted.
	 * @param p_territoryTo The territory being attacked.
	 * @param p_numberOfArmies The number of armies to airlift for the attack.
	 */
	public AirliftAttack(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
		super(p_issuer, p_territoryFrom, p_territoryTo, p_numberOfArmies);
	}
	
	/**
	 * Copy constructor for AirliftAttack.
	 * 
	 * @param airliftAttack The AirliftAttack instance to copy.
	 */
	public AirliftAttack(AirliftAttack airliftAttack) {
		super(airliftAttack.getIssuer(), airliftAttack.getD_territoryFrom(), airliftAttack.getD_territoryTo(), airliftAttack.getD_numberOfArmies());
	}
	
	/**
	 * Executes the airlift attack order. This method simulates a battle by calculating
	 * casualties based on random chance. If diplomacy is in effect, the attack is undone.
	 * Depending on the outcome, the method updates the territories' ownership and army counts.
	 * In the Command pattern, this is the concrete implementation of the execute() method.
	 */
	@Override
	public void execute() {
	    String l_logMessage;

	    if (getIssuer().getNegociatedPlayersPerTurn().contains(d_territoryTo.getOwner())) {
	        d_territoryFrom.setNumOfArmies(d_territoryFrom.getNumOfArmies() + d_numberOfArmies);
	        l_logMessage = "Airlift Attack cancelled: Player " + getIssuer().getName() +
	                       " has a diplomacy agreement with " + d_territoryTo.getOwner().getName();

	        System.out.println("Undo Airlift order from: " + getIssuer().getName());
	        System.out.println("Diplomacy between: " + getIssuer().getName() + " and " + d_territoryTo.getOwner().getName());
	        logOrderExecution(l_logMessage);
	        return;
	    }

	    int attackingArmies = getD_numberOfArmies();
	    int defendingArmies = getD_territoryTo().getNumOfArmies();
	    String l_opposingPlayerName = getD_territoryTo().getOwner().getName();

	    // ✅ Subtract attacking armies from source
	    getD_territoryFrom().setNumOfArmies(getD_territoryFrom().getNumOfArmies() - attackingArmies);

	    Random rand = new Random();
	    int attackerKills = 0;
	    int defenderKills = 0;

	    for (int i = 0; i < attackingArmies; i++) {
	        if (rand.nextDouble() < 0.6) attackerKills++;
	    }
	    for (int i = 0; i < defendingArmies; i++) {
	        if (rand.nextDouble() < 0.7) defenderKills++;
	    }

	    int actualDefenderCasualties = Math.min(defendingArmies, attackerKills);
	    int actualAttackerCasualties = Math.min(attackingArmies, defenderKills);

	    int survivingAttackingArmies = attackingArmies - actualAttackerCasualties;
	    int survivingDefendingArmies = defendingArmies - actualDefenderCasualties;

	    boolean l_conquered = false;

	    if (survivingDefendingArmies <= 0) {
	        // ✅ Use d_issuer for exact reference in tests
	        Player defender = d_territoryTo.getOwner();
	        defender.removeTerritory(d_territoryTo);
	        d_territoryTo.setOwner(d_issuer);
	        d_issuer.addTerritory(d_territoryTo);
	        d_territoryTo.setNumOfArmies(survivingAttackingArmies);
	        d_issuer.setHasConqueredThisTurn(true);
	        l_conquered = true;
	    } else {
	        d_territoryTo.setNumOfArmies(survivingDefendingArmies);
	        d_territoryFrom.setNumOfArmies(d_territoryFrom.getNumOfArmies() + survivingAttackingArmies);
	    }

	    // ✅ Console + log messages
	    System.out.println("Airlift Battle results:");
	    System.out.println(getD_territoryFrom().getName() + " (attacking) sent: " + attackingArmies + " armies");
	    System.out.println(getD_territoryTo().getName() + " (defending) had: " + defendingArmies + " armies");
	    System.out.println(getD_territoryFrom().getName() + " (attacking) inflicted " + actualDefenderCasualties + " casualties");
	    System.out.println(getD_territoryTo().getName() + " (defending) inflicted " + actualAttackerCasualties + " casualties");

	    if (l_conquered) {
	        System.out.println(d_issuer.getName() + " captured " + getD_territoryTo().getName() +
	                           " and the territory now has " + survivingAttackingArmies + " armies.");

	        l_logMessage = "Airlift Attack succeeded: Player " + d_issuer.getName() +
	                       " conquered " + getD_territoryTo().getName() + " from " + l_opposingPlayerName + ". " +
	                       survivingAttackingArmies + " attacking armies survived.";
	    } else {
	        System.out.println(l_opposingPlayerName + " retained " + getD_territoryTo().getName() +
	                           " and the territory now has " + survivingDefendingArmies + " armies remaining.");
	        System.out.println(d_issuer.getName() + " retained " + getD_territoryFrom().getName() +
	                           " and the territory now has " + d_territoryFrom.getNumOfArmies() + " armies.");

	        l_logMessage = "Airlift Attack failed: Player " + d_issuer.getName() +
	                       " attacked " + getD_territoryTo().getName() + " owned by " + l_opposingPlayerName + ". " +
	                       "Defender retained territory with " + survivingDefendingArmies + " armies. " +
	                       survivingAttackingArmies + " attacking armies returned to " + getD_territoryFrom().getName() + ".";
	    }

	    logOrderExecution(l_logMessage);
	}

}
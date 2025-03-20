package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;
import java.util.Random;
import com.Game.model.CardType;

public class AdvanceAttack extends AdvanceOrder {
	
	public AdvanceAttack() {
		super();
	}
	
	public AdvanceAttack(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
		super(p_issuer, p_territoryFrom, p_territoryTo, p_numberOfArmies);
	}
	
	public AdvanceAttack(AdvanceAttack advAttack) {
		super(advAttack.getIssuer(), advAttack.getD_territoryFrom(), advAttack.getD_territoryTo(), advAttack.getD_numberOfArmies());
	}
	
	@Override
	public void execute() {
		//System.out.println("DEBUG: Attack");
	    int attackingArmies = getD_numberOfArmies();
	    
	    String l_opposingPlayerName = getD_territoryTo().getOwner().getName();
	    
	    int defendingArmies = getD_territoryTo().getNumOfArmies();
	    
	    // Remove the attacking armies from the originating territory
	    
	    //getD_territoryFrom().setNumOfArmies(getD_territoryFrom().getNumOfArmies() - this.d_numberOfArmies);
	    
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
	    
	    // The actual casualties cannot exceed the number of armies on each side.
	    // it should be attacker kills but it should never go beyond the number of defending Armies.
	    int actualDefenderCasualties = Math.min(defendingArmies, attackerKills);
	    
	    // it should normally be defender kills but it should never go beyond the number of attacking armies 
	    int actualAttackerCasualties = Math.min(attackingArmies, defenderKills);
	    
	    int survivingAttackingArmies = attackingArmies - actualAttackerCasualties;
	    int survivingDefendingArmies = defendingArmies - actualDefenderCasualties;
	    CardType randomCard = null;
	    if (survivingDefendingArmies <= 0) {
	        // All defending armies are eliminated: attacker captures the territory.
	    	//get old player that owned the lost territory and remove territory list from him/her
	    	getD_territoryTo().getOwner().removeTerritory(d_territoryTo);
	        getD_territoryTo().setOwner(getIssuer());
	        getIssuer().addTerritory(d_territoryTo);
	        // The surviving attacking armies occupy the conquered territory.
	        getD_territoryTo().setNumOfArmies(survivingAttackingArmies);
	        
	        CardType[] allCardTypes = CardType.values();
	        int randomIndex = new Random().nextInt(allCardTypes.length);
	        randomCard = allCardTypes[randomIndex];
	        getIssuer().addCard(randomCard);
	        
	        //Increment the number of conquered territories per turn for card distribution (should be reset to 0 after round ends in GamePlay)
	    } else {
	        // The defender still holds the territory.
	        getD_territoryTo().setNumOfArmies(survivingDefendingArmies);
	        // The surviving attacking armies return to the original territory.
	        getD_territoryFrom().setNumOfArmies(survivingAttackingArmies);
	    }
	    
	    // Log the battle result.
	    System.out.println("Battle results:");
	    System.out.println(getD_territoryFrom().getName() + " (attacking) sent: " + attackingArmies + " armies");
	    System.out.println(getD_territoryTo().getName() + " (defending) had: " + defendingArmies + " armies");
	    System.out.println(getD_territoryFrom().getName() + " (attacking) inflicted " + actualDefenderCasualties + " casualties");
	    System.out.println(getD_territoryTo().getName() + " (defending) inflicted " + actualAttackerCasualties + " casualties");
	    if (survivingDefendingArmies <= 0) {
	        System.out.println(getIssuer().getName() + " captured "+ getD_territoryTo().getName() + " and the territory now has " + survivingAttackingArmies + " armies.");
	        System.out.println(getIssuer().getName() + " will recieve a random card! Drum rolls... " + randomCard.name() + "!");
	    } else {
	        System.out.println(l_opposingPlayerName +" retained " + getD_territoryTo().getName() + " and the territory now has " + survivingDefendingArmies + " armies remaining.");
	        System.out.println(getIssuer().getName() + " retained "+ getD_territoryFrom().getName() + " and the territory now has " + survivingAttackingArmies + " armies.");
	    }
	}
}

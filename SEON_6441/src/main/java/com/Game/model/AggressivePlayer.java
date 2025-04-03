package com.Game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.Game.model.order.AdvanceAttack;
import com.Game.model.order.AdvanceMove;
import com.Game.model.order.AirliftAttack;
import com.Game.model.order.AirliftMove;
import com.Game.model.order.BlockadeOrder;
import com.Game.model.order.BombOrder;
import com.Game.model.order.DeployOrder;
import com.Game.model.order.NegotiateOrder;
import com.Game.model.order.Order;
import com.Game.observer.GameLogger;

/**
 * Represents a player in the game who owns territories and can issue orders.
 * Players can deploy armies, acquire territories, manage their reinforcements,
 * cards, conquered territories count, and negotiated players per turn.
 */
public class AggressivePlayer extends Player {

	/**
	 * Constructor initializing human player with a name.
	 *
	 * @param p_name Player's name
	 */
	public AggressivePlayer(String p_name) {
		super(p_name);
		
	}

	/**
	 * Constructor initializing human player with a name and reinforcement armies.
	 *
	 * @param p_name                     Player's name
	 * @param p_nbrOfReinforcementArmies Number of reinforcement armies
	 */
	public AggressivePlayer(String p_name, int p_nbrOfReinforcementArmies) {
		super(p_name, p_nbrOfReinforcementArmies);
	}

	
	@Override
	public boolean issueOrder(String p_command, Map p_map, List<Player> p_players) {
	    // p_command is passed as empty. We ignore its value and decide orders based on an aggressive strategy.
	    boolean orderIssued = false;

	    // ===== Aggressive Deployment =====
	    // Find the strongest territory that has at least one enemy neighbor.
	    Territory bestDeployTarget = null;
	    int maxArmies = -1;
	    for (Territory territory : d_ownedTerritories) {
	        if (!territory.getEnemyNeighbors().isEmpty() && territory.getNumOfArmies() > maxArmies) {
	            bestDeployTarget = territory;
	            maxArmies = territory.getNumOfArmies();
	        }
	    }
	    // Deploy all reinforcement armies to that territory.
	    if (bestDeployTarget != null && this.d_nbrOfReinforcementArmies > 0) {
	        DeployOrder deployOrder = new DeployOrder(this, bestDeployTarget, this.d_nbrOfReinforcementArmies);
	        d_orders.add(deployOrder);
	        // After deployment, set reinforcement armies to zero.
	        this.d_nbrOfReinforcementArmies = 0;
	        orderIssued = true;
	    }

	    // ===== Aggressive Attacks =====
	    // Choose the strongest territory (with enemy neighbors) as the attacking source.
	    Territory attackingTerritory = null;
	    maxArmies = -1;
	    for (Territory territory : d_ownedTerritories) {
	        if (!territory.getEnemyNeighbors().isEmpty() && territory.getNumOfArmies() > maxArmies) {
	            attackingTerritory = territory;
	            maxArmies = territory.getNumOfArmies();
	        }
	    }
	    if (attackingTerritory != null) {
	        // Select an enemy target: here we pick the enemy neighbor with the fewest armies.
	        Territory targetTerritory = null;
	        int minEnemyArmies = Integer.MAX_VALUE;
	        for (Territory enemy : attackingTerritory.getEnemyNeighbors()) {
	            if (enemy.getNumOfArmies() < minEnemyArmies) {
	                targetTerritory = enemy;
	                minEnemyArmies = enemy.getNumOfArmies();
	            }
	        }
	        if (targetTerritory != null) {
	            // 2.1: If a bomb card is available and the bomb order is valid, issue a Bomb order.
	            if (this.hasCard(CardType.BOMB) &&
	                validateBomb(new String[] { "bomb", targetTerritory.getName() }, p_map)) {
	                BombOrder bombOrder = new BombOrder(this, targetTerritory);
	                d_orders.add(bombOrder);
	                orderIssued = true;
	            }
	            // 2.2: If the target is adjacent, issue an AdvanceAttack order.
	            if (attackingTerritory.hasNeighbor(targetTerritory)) {
	                // Example: send half the armies (this is arbitrary; adjust strategy as needed)
	                int armiesToAttack = attackingTerritory.getNumOfArmies();
	                AdvanceAttack advanceAttack = new AdvanceAttack(this, attackingTerritory, targetTerritory, armiesToAttack);
	                d_orders.add(advanceAttack);
	                orderIssued = true;
	            }
	            // 2.3: Otherwise, if an airlift card is available, issue an AirliftAttack order.
	            else if (this.hasCard(CardType.AIRLIFT) &&
	                     validateAirlift(new String[] { "airlift", attackingTerritory.getName(), targetTerritory.getName(),
	                         String.valueOf(attackingTerritory.getNumOfArmies()) }, p_map)) {
	                int armiesToAttack = attackingTerritory.getNumOfArmies();
	                AirliftAttack airliftAttack = new AirliftAttack(this, attackingTerritory, targetTerritory, armiesToAttack);
	                d_orders.add(airliftAttack);
	                orderIssued = true;
	            }
	        }
	    }

	    return orderIssued;
	}

	
	@Override
	public String toString() {
		return "\nAggressive Player: " + this.d_name + "\nNumber of Reinforcement Armies: " + this.d_nbrOfReinforcementArmies;
	}
}


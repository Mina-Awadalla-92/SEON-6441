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
public class HumanPlayer extends Player {

	/**
	 * Constructor initializing human player with a name.
	 *
	 * @param p_name Player's name
	 */
	public HumanPlayer(String p_name) {
		super(p_name);
		
	}

	/**
	 * Constructor initializing human player with a name and reinforcement armies.
	 *
	 * @param p_name                     Player's name
	 * @param p_nbrOfReinforcementArmies Number of reinforcement armies
	 */
	public HumanPlayer(String p_name, int p_nbrOfReinforcementArmies) {
		super(p_name, p_nbrOfReinforcementArmies);
	}

	
	@Override
	public boolean issueOrder(String p_command, Map p_map, List<Player> p_players) {
		String[] l_parts = p_command.split(" ");
		if (l_parts.length < 2 || l_parts.length > 4) {
			return false;
		}

		String l_orderType = l_parts[0].toLowerCase();
		Order l_order = null;

		// Create the appropriate order object based on the command type
		if (l_orderType.equals("deploy")) {
			if (!validateDeploy(l_parts)) {
				return false;
			}

			String l_targetTerritoryName = l_parts[1];
			int l_numberOfArmies = Integer.parseInt(l_parts[2]);
			Territory l_targetTerritory = findTerritoryByName(l_targetTerritoryName);

			// Create the Deploy Order
			l_order = new DeployOrder(this, l_targetTerritory, l_numberOfArmies);
			this.d_nbrOfReinforcementArmies -= l_numberOfArmies;
		} else if (l_orderType.equals("advance")) {
			if (!validateAdvance(l_parts, p_map)) {
				return false;
			}

			Territory l_territoryFrom = findTerritoryByName(l_parts[1]);
			Territory l_territoryTo = p_map.getTerritoryByName(l_parts[2]);
			int l_numberOfArmies = Integer.parseInt(l_parts[3]);

			// Deduct armies from source territory
			l_territoryFrom.setNumOfArmies(l_territoryFrom.getNumOfArmies() - l_numberOfArmies);

			// Create the appropriate Advance Order (Move or Attack)
			if (l_territoryTo.getOwner() == null || l_territoryTo.getOwner().getName().equals(this.getName())) {
				l_order = new AdvanceMove(this, l_territoryFrom, l_territoryTo, l_numberOfArmies);
			} else {
				l_order = new AdvanceAttack(this, l_territoryFrom, l_territoryTo, l_numberOfArmies);
			}
		} else if (l_orderType.equalsIgnoreCase("bomb")) {
			if (!validateBomb(l_parts, p_map)) {
				return false;
			}

			Territory l_territoryTo = p_map.getTerritoryByName(l_parts[1]);
			l_order = new BombOrder(this, l_territoryTo);
		} else if (l_orderType.equalsIgnoreCase("blockade")) {
			if (!validateBlockade(l_parts)) {
				return false;
			}

			Territory l_territoryTo = findTerritoryByName(l_parts[1]);
			l_order = new BlockadeOrder(this, l_territoryTo);
		} else if (l_orderType.equalsIgnoreCase("airlift")) {
			if (!validateAirlift(l_parts, p_map)) {
				return false;
			}

			Territory l_territoryFrom = findTerritoryByName(l_parts[1]);
			Territory l_territoryTo = p_map.getTerritoryByName(l_parts[2]);
			int l_numberOfArmies = Integer.parseInt(l_parts[3]);

			// Deduct armies from source territory
			l_territoryFrom.setNumOfArmies(l_territoryFrom.getNumOfArmies() - l_numberOfArmies);

			// Create the appropriate Airlift Order (Move or Attack)
			if (l_territoryTo.getOwner() == null || l_territoryTo.getOwner().getName().equals(this.getName())) {
				l_order = new AirliftMove(this, l_territoryFrom, l_territoryTo, l_numberOfArmies);
			} else {
				l_order = new AirliftAttack(this, l_territoryFrom, l_territoryTo, l_numberOfArmies);
			}
		} else if (l_orderType.equalsIgnoreCase("negotiate")) {
			if (!validateNegociate(l_parts, p_players)) {
				return false;
			}

			Player l_playerToNegotiateWith = null;
			for (Player l_player : p_players) {
				if (l_player.getName().equals(l_parts[1])) {
					l_playerToNegotiateWith = l_player;
					break;
				}
			}

			l_order = new NegotiateOrder(this, l_playerToNegotiateWith);
			// Execute the negotiate order immediately
			l_order.execute();
			return true;
		} else {
			return false;
		}

		// Add the created order to the player's order list (except negotiate which
		// executed immediately)
		if (l_order != null) {
			d_orders.add(l_order);
			return true;
		}

		return false;
	}

	
	@Override
	public String toString() {
		return "\nHuman Player: " + this.d_name + "\nNumber of Reinforcement Armies: " + this.d_nbrOfReinforcementArmies;
	}
}


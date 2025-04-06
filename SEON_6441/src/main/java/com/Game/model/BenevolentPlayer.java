package com.Game.model;

import java.util.List;
import com.Game.model.order.AdvanceMove;
import com.Game.model.order.DeployOrder;

/**
 * Represents a Benevolent player in the game who owns territories and can issue orders.
 * Players can deploy armies, acquire territories, manage their reinforcements,
 * cards, conquered territories count, and negotiated players per turn.
 */
public class BenevolentPlayer extends Player {

    /**
     * Constructor initializing the benevolent player with a name.
     * @param p_name Player's name.
     * @param p_playerType Type of player.
     */
    public BenevolentPlayer(String p_name, String p_playerType) {
        super(p_name, p_playerType);
    }

    /**
     * Constructor initializing the benevolent player with a name and reinforcement armies.
     * @param p_name Player's name.
     * @param p_nbrOfReinforcementArmies Number of reinforcement armies.
     * @param p_playerType Type of player.
     */
    public BenevolentPlayer(String p_name, int p_nbrOfReinforcementArmies, String p_playerType) {
        super(p_name, p_nbrOfReinforcementArmies, p_playerType);
    }

    /**
     * Issues orders based on a benevolent strategy:
     * 1. Reinforces the weakest territory with all available reinforcements.
     * 2. Reallocates armies from stronger territories to adjacent, weaker ones.
     * 3. Avoids any form of attack.
     * 
     * @param p_command The command string (ignored for benevolent strategy).
     * @param p_map The map containing the territories.
     * @param p_players A list of all players.
     * @return true if at least one order was issued, false otherwise.
     */
    @Override
    public boolean issueOrder(String p_command, Map p_map, List<Player> p_players) {
        if (p_command == null) {
            return false;
        }

        boolean orderIssued = false;

        // ===== Phase 1: Reinforcement =====
        // Find the weakest territory (i.e. the one with the fewest armies).
        Territory weakestTerritory = null;
        int minArmies = Integer.MAX_VALUE;
        for (Territory territory : d_ownedTerritories) {
            if (territory.getNumOfArmies() < minArmies) {
                weakestTerritory = territory;
                minArmies = territory.getNumOfArmies();
            }
        }
        // Deploy all reinforcement armies to the weakest territory.
        if (weakestTerritory != null && this.d_nbrOfReinforcementArmies > 0) {
            DeployOrder deployOrder = new DeployOrder(this, weakestTerritory, this.d_nbrOfReinforcementArmies);
            d_orders.add(deployOrder);
            // Set reinforcement armies to zero after deployment.
            this.d_nbrOfReinforcementArmies = 0;
            orderIssued = true;
        }

        // ===== Phase 2: Reallocation =====
        // Reallocate armies from stronger territories to adjacent, weaker ones.
        for (Territory source : d_ownedTerritories) {
            for (Territory neighbor : source.getNeighborList()) {
                // Only consider friendly neighbors.
                if (neighbor.getOwner() != null && neighbor.getOwner().getName().equals(this.getName())) {
                    // If the source territory has significantly more armies than the neighbor...
                    if (source.getNumOfArmies() > neighbor.getNumOfArmies() + 1) {
                        // Use half the difference as a heuristic for how many armies to move.
                        int armiesToMove = (source.getNumOfArmies() - neighbor.getNumOfArmies()) / 2;
                        if (armiesToMove > 0) {
                            // Validate the move order using the parent's validation method.
                            if (validateAdvance(new String[]{"advance", source.getName(), neighbor.getName(), String.valueOf(armiesToMove)}, p_map)) {
                                AdvanceMove moveOrder = new AdvanceMove(this, source, neighbor, armiesToMove);
                                d_orders.add(moveOrder);
                                // Deduct the moved armies from the source territory.
                                source.setNumOfArmies(source.getNumOfArmies() - armiesToMove);
                                orderIssued = true;
                            }
                        }
                    }
                }
            }
        }

        // Benevolent players do not issue any attack orders.
        return orderIssued;
    }

    @Override
    public String toString() {
        return "\nBenevolent Player: " + this.d_name + "\nReinforcement Armies: " + this.d_nbrOfReinforcementArmies;
    }
}

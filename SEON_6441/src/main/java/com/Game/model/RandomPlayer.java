package com.Game.model;

import java.util.List;
import java.util.Random;

import com.Game.model.order.AdvanceAttack;
import com.Game.model.order.AdvanceMove;
import com.Game.model.order.AirliftAttack;
import com.Game.model.order.DeployOrder;

/**
 * Represents a Random in the game who owns territories and can issue orders.
 * Players can deploy armies, acquire territories, manage their reinforcements,
 * cards, conquered territories count, and negotiated players per turn.
 */
public class RandomPlayer extends Player {

    private Random rand;

    /**
     * Constructor initializing the random player with a name.
     * @param p_name Player's name.
     * @param p_playerType Type of player.
     */
    public RandomPlayer(String p_name, String p_playerType) {
        super(p_name, p_playerType);
        this.rand = new Random();
    }

    /**
     * Constructor initializing the random player with a name and reinforcement armies.
     * @param p_name Player's name.
     * @param p_nbrOfReinforcementArmies Number of reinforcement armies.
     * @param p_playerType Type of player.
     */
    public RandomPlayer(String p_name, int p_nbrOfReinforcementArmies, String p_playerType) {
        super(p_name, p_nbrOfReinforcementArmies, p_playerType);
        this.rand = new Random();
    }

    /**
     * Issues orders based on a random strategy:
     * 1. Reinforces a randomly chosen owned territory.
     * 2. For each owned territory with enemy neighbors, randomly selects one enemy to attack.
     * 3. Randomly shifts armies between adjacent friendly territories.
     * 
     * @param p_command The command string (ignored in this strategy).
     * @param p_map The map containing the territories.
     * @param p_players A list of all players in the game.
     * @return true if at least one order was issued, false otherwise.
     */
    @Override
    public boolean issueOrder(String p_command, Map p_map, List<Player> p_players) {
        if (p_command == null) {
            return false;
        }

        boolean orderIssued = false;

        // ===== Phase 1: Reinforcement =====
        if (!d_ownedTerritories.isEmpty() && this.d_nbrOfReinforcementArmies > 0) {
            int index = rand.nextInt(d_ownedTerritories.size());
            Territory randomTerritory = d_ownedTerritories.get(index);
            DeployOrder deployOrder = new DeployOrder(this, randomTerritory, this.d_nbrOfReinforcementArmies);
            d_orders.add(deployOrder);
            // After deployment, set reinforcement armies to zero.
            this.d_nbrOfReinforcementArmies = 0;
            orderIssued = true;
        }

        // ===== Phase 2: Attack =====
        // For each territory that has enemy neighbors, launch an attack.
        for (Territory territory : d_ownedTerritories) {
            List<Territory> enemyNeighbors = territory.getEnemyNeighbors();
            if (!enemyNeighbors.isEmpty() && territory.getNumOfArmies() > 1) {
                int enemyIndex = rand.nextInt(enemyNeighbors.size());
                Territory target = enemyNeighbors.get(enemyIndex);
                int availableArmies = territory.getNumOfArmies();
                // Ensure at least one army remains behind.
                int maxArmiesToAttack = availableArmies - 1;
                if (maxArmiesToAttack > 0) {
                    int armiesToAttack = 1 + rand.nextInt(maxArmiesToAttack);
                    // If the territories are adjacent, use AdvanceAttack.
                    if (territory.hasNeighbor(target)) {
                        AdvanceAttack attackOrder = new AdvanceAttack(this, territory, target, armiesToAttack);
                        d_orders.add(attackOrder);
                        territory.setNumOfArmies(availableArmies - armiesToAttack);
                        orderIssued = true;
                    } else if (this.hasCard(CardType.AIRLIFT) && 
                               validateAirlift(new String[] { "airlift", territory.getName(), target.getName(), String.valueOf(armiesToAttack) }, p_map)) {
                        AirliftAttack airliftAttack = new AirliftAttack(this, territory, target, armiesToAttack);
                        d_orders.add(airliftAttack);
                        territory.setNumOfArmies(availableArmies - armiesToAttack);
                        orderIssued = true;
                    }
                }
            }
        }

        // ===== Phase 3: Reallocation =====
        // Randomly pick two different owned territories and attempt to reallocate armies if they are adjacent.
        if (d_ownedTerritories.size() > 1) {
            int sourceIndex = rand.nextInt(d_ownedTerritories.size());
            int destIndex = rand.nextInt(d_ownedTerritories.size());
            // Ensure source and destination are different.
            while (destIndex == sourceIndex) {
                destIndex = rand.nextInt(d_ownedTerritories.size());
            }
            Territory source = d_ownedTerritories.get(sourceIndex);
            Territory dest = d_ownedTerritories.get(destIndex);
            // Only reallocate if the two territories are adjacent.
            if (source.hasNeighbor(dest) && source.getNumOfArmies() > 1) {
                int availableArmies = source.getNumOfArmies();
                // Move a random number of armies (ensuring at least one stays).
                int maxArmiesToMove = availableArmies - 1;
                int armiesToMove = 1 + rand.nextInt(maxArmiesToMove);
                // Validate the move order.
                if (validateAdvance(new String[]{"advance", source.getName(), dest.getName(), String.valueOf(armiesToMove)}, p_map)) {
                    AdvanceMove moveOrder = new AdvanceMove(this, source, dest, armiesToMove);
                    d_orders.add(moveOrder);
                    source.setNumOfArmies(availableArmies - armiesToMove);
                    orderIssued = true;
                }
            }
        }

        return orderIssued;
    }

    @Override
    public String toString() {
        return "\nRandom Player: " + this.d_name + "\nReinforcement Armies: " + this.d_nbrOfReinforcementArmies;
    }
}

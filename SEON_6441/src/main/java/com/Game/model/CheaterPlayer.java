package com.Game.model;

import java.util.ArrayList;
import java.util.List;

import com.Game.observer.GameLogger;

public class CheaterPlayer extends Player {

    /**
     * Constructor initializing the cheater player with a name.
     * @param p_name Player's name.
     */
    public CheaterPlayer(String p_name) {
        super(p_name);
    }

    /**
     * Constructor initializing the cheater player with a name and reinforcement armies.
     * @param p_name Player's name.
     * @param p_nbrOfReinforcementArmies Number of reinforcement armies.
     */
    public CheaterPlayer(String p_name, int p_nbrOfReinforcementArmies) {
        super(p_name, p_nbrOfReinforcementArmies);
    }

    /**
     * Cheater strategy: Automatically capture all adjacent enemy territories
     * and double the number of armies on any territory that borders enemies.
     * This method directly manipulates the map rather than generating explicit orders.
     *
     * @param p_command The command string (ignored in this strategy).
     * @param p_map The map containing the territories.
     * @param p_players A list of all players in the game.
     * @return true after the strategy has been executed.
     */
    @Override
    public boolean issueOrder(String p_command, Map p_map, List<Player> p_players) {
        // Create a copy of owned territories to avoid concurrent modification issues.
        List<Territory> ownedCopy = new ArrayList<>(d_ownedTerritories);
        GameLogger logger = GameLogger.getInstance();
        
        // Iterate over each territory owned by the cheater.
        for (Territory t : ownedCopy) {
            // Get enemy neighbors of territory t.
            List<Territory> enemyNeighbors = t.getEnemyNeighbors();
            
            if (!enemyNeighbors.isEmpty()) {
                // Double the armies on territory t.
                int currentArmies = t.getNumOfArmies();
                t.setNumOfArmies(currentArmies * 2);
                if (logger != null) {
                    logger.logAction("CheaterPlayer " + this.d_name + " doubled armies on " + t.getName());
                }
                
                // Capture all adjacent enemy territories.
                for (Territory enemy : enemyNeighbors) {
                    if (enemy.getOwner() != this) {
                        Player enemyOwner = enemy.getOwner();
                        if (enemyOwner != null) {
                            enemyOwner.removeTerritory(enemy);
                        }
                        enemy.setOwner(this);
                        if (!d_ownedTerritories.contains(enemy)) {
                            d_ownedTerritories.add(enemy);
                        }
                        if (logger != null) {
                            logger.logAction("CheaterPlayer " + this.d_name + " captured " + enemy.getName());
                        }
                    }
                }
            }
        }
        // The strategy is executed immediately; no explicit orders are generated.
        return true;
    }

    @Override
    public String toString() {
        return "\nCheater Player: " + this.d_name + "\nOwned Territories: " + d_ownedTerritories.size();
    }
}

package com.Game.command;

import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.model.order.DeployOrder;

/**
 * Represents a deploy command in the Command pattern.
 * This command creates a deploy order for a player.
 * This is an example implementation of the Command interface for the Command pattern.
 * This class will be fully integrated in Build 2.
 */
public class DeployCommand implements Command {
    
    /**
     * The player issuing the command.
     */
    private Player d_player;
    
    /**
     * The target territory for deployment.
     */
    private Territory d_targetTerritory;
    
    /**
     * The number of armies to deploy.
     */
    private int d_numberOfArmies;
    
    /**
     * Constructor initializing the command with necessary information.
     * 
     * @param p_player The player issuing the command
     * @param p_targetTerritory The target territory for deployment
     * @param p_numberOfArmies The number of armies to deploy
     */
    public DeployCommand(Player p_player, Territory p_targetTerritory, int p_numberOfArmies) {
        this.d_player = p_player;
        this.d_targetTerritory = p_targetTerritory;
        this.d_numberOfArmies = p_numberOfArmies;
    }
    
    /**
     * Executes the deploy command, creating a deploy order for the player.
     */
    @Override
    public void execute() {
        if (validate()) {
            DeployOrder l_deployOrder = new DeployOrder(d_player, d_targetTerritory, d_numberOfArmies);
            d_player.getOrders().add(l_deployOrder);
            d_player.setNbrOfReinforcementArmies(d_player.getNbrOfReinforcementArmies() - d_numberOfArmies);
            System.out.println(d_player.getName() + " issued deploy order: " + d_numberOfArmies + 
                               " armies to " + d_targetTerritory.getName());
        } else {
            System.out.println("Deploy command failed validation.");
        }
    }
    
    /**
     * Undoes the deploy command, if possible.
     * This is a placeholder implementation for potential future undo functionality.
     */
    @Override
    public void undo() {
        // This would restore the armies to the player's reinforcement pool
        // and remove the order from the player's order list
        // For now, this is just a placeholder for Build 2
        System.out.println("Undo not implemented for deploy command yet.");
    }
    
    /**
     * Gets the name of the command.
     * 
     * @return The command name
     */
    @Override
    public String getCommandName() {
        return "deploy";
    }
    
    /**
     * Validates whether the deploy command can be executed.
     * Checks if:
     * 1. The player owns the target territory
     * 2. The player has enough reinforcement armies
     * 
     * @return true if the command can be executed, false otherwise
     */
    @Override
    public boolean validate() {
        // Check if player owns the territory
        if (!d_player.getOwnedTerritories().contains(d_targetTerritory)) {
            System.out.println("Error: " + d_player.getName() + " does not own " + d_targetTerritory.getName());
            return false;
        }
        
        // Check if player has enough reinforcement armies
        if (d_player.getNbrOfReinforcementArmies() < d_numberOfArmies) {
            System.out.println("Error: " + d_player.getName() + " only has " + 
                              d_player.getNbrOfReinforcementArmies() + " armies available.");
            return false;
        }
        
        // All checks passed
        return true;
    }
}
package com.Game.command;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.Game.model.Player;
import com.Game.model.Territory;

/**
 * A comprehensive, mock-based unit test for DeployCommand, covering multiple scenarios:
 * - Territory ownership: yes/no
 * - Reinforcement armies: enough, zero, negative
 * - Valid/invalid execute() calls
 */
public class DeployCommandTest {

    private Player mockPlayer;
    private Territory mockTerritory;
    private DeployCommand deployCommand;

    @BeforeEach
    void setUp() {
        // Create mock objects
        mockPlayer = mock(Player.class);
        mockTerritory = mock(Territory.class);

        // We always deploy 5 armies in these tests, but you can parameterize as needed
        deployCommand = new DeployCommand(mockPlayer, mockTerritory, 5);
    }

    //
    // -------------------- EXECUTE() TESTS --------------------
    //

    @Test
    void testExecute_Valid() {
        // Player owns territory, has 10 armies => Enough to deploy 5
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(10);
        when(mockPlayer.getName()).thenReturn("Alice");
        when(mockTerritory.getName()).thenReturn("TerrX");

        deployCommand.execute();

        // verify that an order was created and armies were reduced
        verify(mockPlayer).getOrders();
        verify(mockPlayer).setNbrOfReinforcementArmies(5); // 10 - 5
        // no direct check that a DeployOrder object was added, but we verify the method calls.
    }

    @Test
    void testExecute_Invalid_PlayerDoesNotOwnTerritory() {
        // Player doesn't own territory => fail validation => no order
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.emptyList());
        when(mockPlayer.getName()).thenReturn("Bob");
        when(mockTerritory.getName()).thenReturn("TerrY");

        deployCommand.execute();

        // No calls to getOrders() or setNbrOfReinforcementArmies() if validation fails
        verify(mockPlayer, never()).getOrders();
        verify(mockPlayer, never()).setNbrOfReinforcementArmies(anyInt());
    }

    @Test
    void testExecute_Invalid_NotEnoughArmies() {
        // Player owns territory but has only 2 armies => not enough to deploy 5 => fail
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(2);
        when(mockPlayer.getName()).thenReturn("Carol");
        when(mockTerritory.getName()).thenReturn("TerrZ");

        deployCommand.execute();

        // No calls to getOrders() or setNbrOfReinforcementArmies() if validation fails
        verify(mockPlayer, never()).getOrders();
        verify(mockPlayer, never()).setNbrOfReinforcementArmies(anyInt());
    }

    @Test
    void testExecute_ZeroReinforcementArmies() {
        // Player owns territory but has 0 armies => cannot deploy 5 => fail
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(0);

        deployCommand.execute();

        verify(mockPlayer, never()).getOrders();
        verify(mockPlayer, never()).setNbrOfReinforcementArmies(anyInt());
    }

    @Test
    void testExecute_NegativeReinforcementArmies() {
        // Player owns territory but has negative armies => definitely can't deploy => fail
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(-10);

        deployCommand.execute();

        verify(mockPlayer, never()).getOrders();
        verify(mockPlayer, never()).setNbrOfReinforcementArmies(anyInt());
    }

    //
    // -------------------- UNDO() TEST --------------------
    //

    @Test
    void testUndo() {
        // The undo method is just a placeholder right now. We call it to ensure no unexpected behavior.
        deployCommand.undo();
        // Typically, you'd verify no interactions or a specific message if implemented.
        // For now, this just ensures the method doesn't crash.
    }

    //
    // -------------------- GETCOMMANDNAME() TEST --------------------
    //

    @Test
    void testGetCommandName() {
        assertEquals("deploy", deployCommand.getCommandName(),
                "Expected the command name to be 'deploy'.");
    }

    //
    // -------------------- VALIDATE() TESTS --------------------
    //

    @Test
    void testValidate_Valid() {
        // Player owns territory, has enough armies (e.g., 7)
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(7);

        assertTrue(deployCommand.validate(), 
            "Should be valid if territory is owned and reinforcement armies >= required.");
    }

    @Test
    void testValidate_PlayerDoesNotOwnTerritory() {
        // Player does not own territory
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.emptyList());
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(10);

        assertFalse(deployCommand.validate(),
            "Should be invalid if player doesn't own territory.");
    }

    @Test
    void testValidate_NotEnoughReinforcementArmies() {
        // Player owns territory but does not have enough armies
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(3);

        assertFalse(deployCommand.validate(),
            "Should be invalid if the player doesn't have the required number of armies.");
    }

    @Test
    void testValidate_ZeroReinforcementArmies() {
        // Zero armies available, but needs 5
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(0);

        assertFalse(deployCommand.validate(),
            "Should be invalid if the player has zero armies but tries to deploy more than 0.");
    }

    @Test
    void testValidate_NegativeReinforcementArmies() {
        // Negative armies doesn't make sense, so validation fails
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.singletonList(mockTerritory));
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(-5);

        assertFalse(deployCommand.validate(),
            "Should be invalid if the player has negative armies.");
    }

    @Test
    void testValidate_PlayerDoesNotOwnTerritory_ZeroArmies() {
        // Combines ownership = no, armies = 0
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.emptyList());
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(0);

        assertFalse(deployCommand.validate(),
            "Should fail if territory is not owned, regardless of zero armies.");
    }

    @Test
    void testValidate_PlayerDoesNotOwnTerritory_NegativeArmies() {
        // Combines ownership = no, armies = negative
        when(mockPlayer.getOwnedTerritories()).thenReturn(Collections.emptyList());
        when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(-1);

        assertFalse(deployCommand.validate(),
            "Should fail if territory is not owned, regardless of negative armies.");
    }
}

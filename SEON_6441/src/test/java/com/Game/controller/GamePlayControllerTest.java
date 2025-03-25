package com.Game.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.matches;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Territory;
import com.Game.view.CommandPromptView;
import com.Game.view.GameView;

/**
 * This test uses:
 *  - A real GamePlayController, 
 *  - Mocks for GameController, Map, and the View classes to avoid infinite loops or actual I/O.
 *  - A mixture of real and mock Player objects where needed.
 */
public class GamePlayControllerTest {

    private GameController mockGameController;
    private Map mockMap;
    private List<Player> players;
    private GameView mockView;
    private CommandPromptView mockCommandPrompt;
    private GamePlayController gamePlayController;

    @BeforeEach
    void setUp() {
        // 1) Create mocks for GameController, Map, and the Views
        mockGameController = mock(GameController.class);
        mockMap = mock(Map.class);
        mockView = mock(GameView.class);
        mockCommandPrompt = mock(CommandPromptView.class);

        // 2) Create a small list of Player objects (real, for demonstration)
        players = new ArrayList<>();

        // The GameController is expected to return our mockView and mockCommandPromptView
        when(mockGameController.getView()).thenReturn(mockView);
        when(mockGameController.getCommandPromptView()).thenReturn(mockCommandPrompt);

        // 3) Create a real GamePlayController with these dependencies
        gamePlayController = new GamePlayController(mockGameController, mockMap, players);
    }

    @Test
    void testHandleCommand_ShowMap() {
        // Command "showmap" should call displayMap on the view
        String[] commandParts = {"showmap"};
        gamePlayController.handleCommand(commandParts, "showmap");

        verify(mockView).displayMap(mockMap, players);
        verifyNoMoreInteractions(mockView);
    }

    @Test
    void testHandleCommand_UnknownCommand() {
        // An unknown command -> displayError
        String[] commandParts = {"xyz"};
        gamePlayController.handleCommand(commandParts, "xyz");

        verify(mockView).displayError("Unknown command or invalid for current phase: xyz");
    }

    @Test
    void testHandleReinforcement_GameNotStarted() {
        // If game is not started => show error
        when(mockGameController.isGameStarted()).thenReturn(false);

        gamePlayController.handleReinforcement();

        verify(mockView).displayError("Game has not started yet. Use 'startgame' command.");
        verifyNoMoreInteractions(mockView);
    }

    @Test
    void testHandleReinforcement_GameStarted() {
        // If game is started, each player gets at least 3 armies 
        when(mockGameController.isGameStarted()).thenReturn(true);

        // Let's add a few players with territories
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        players.add(p1);
        players.add(p2);

        // Suppose p1 owns 5 territories, p2 owns 1 territory
        for (int i = 0; i < 5; i++) {
            p1.addTerritory(new Territory("A" + i, "ContA", 0));
        }
        p2.addTerritory(new Territory("B0", "ContB", 0));

        // Execute
        gamePlayController.handleReinforcement();

        // Check that the phase prompts were displayed
        verify(mockView).displayReinforcementPhase();
        // p1 => max(3, 5/3=1) => 3
        // p2 => max(3, 1/3=0) => 3
        verify(mockView).displayReinforcementAllocation("Alice", 3);
        verify(mockView).displayReinforcementAllocation("Bob", 3);
        verify(mockView).displayReinforcementComplete();

        // Ensure players actually got the armies
        assertEquals(3, p1.getNbrOfReinforcementArmies());
        assertEquals(3, p2.getNbrOfReinforcementArmies());
    }

    @Test
    void testHandleAssignCountries_TooFewPlayers() {
        // No players means we cannot assign countries
        // Expect an error
        boolean result = gamePlayController.handleAssignCountries();
        assertFalse(result);

        verify(mockView).displayError("Need at least 2 players to start the game.");
    }

    @Test
    void testHandleAssignCountries_NoTerritoriesInMap() {
        // We have 2 players, but the map has no territories
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));

        when(mockMap.getTerritoryList()).thenReturn(Collections.emptyList());

        boolean result = gamePlayController.handleAssignCountries();
        assertFalse(result);

        verify(mockView).displayError("No territories in the map. Cannot assign countries.");
    }

    @Test
    void testHandleAssignCountries_Success() {
        // 2 players, map with some territories
        Player p1 = new Player("Alice");
        Player p2 = new Player("Bob");
        players.add(p1);
        players.add(p2);

        // Suppose the map has 3 territories
        Territory t1 = new Territory("T1", "C1", 0);
        Territory t2 = new Territory("T2", "C1", 0);
        Territory t3 = new Territory("T3", "C2", 0);
        List<Territory> territoryList = new ArrayList<>();
        territoryList.add(t1);
        territoryList.add(t2);
        territoryList.add(t3);

        when(mockMap.getTerritoryList()).thenReturn(territoryList);

        boolean result = gamePlayController.handleAssignCountries();
        assertTrue(result);

        // After assignment, each territory should have an owner and 1 army
        // Because the code does random shuffling, we can't predict exact distribution,
        // but we know each territory is assigned to either p1 or p2.
        // So total territories = 3. p1 might own 2, p2 might own 1, or vice versa.

        // We can at least check that each territory has an owner set
        // and each player has territoryCount > 0.
        assertTrue(p1.getOwnedTerritories().size() > 0);
        assertTrue(p2.getOwnedTerritories().size() > 0);
        // Also each territory has 1 army
        for (Territory t : territoryList) {
            assertEquals(1, t.getNumOfArmies());
            assertNotNull(t.getOwner());
        }

        // Confirm that the user sees a message about successful assignment
        verify(mockView).displayMessage("Countries assigned to players:");
        verify(mockView, times(2)).displayMessage(matches(".* owns .* territories."));
        verify(mockView).displayMessage("Ready to start the game. Use 'startgame' command to begin.");
    }

    @Test
    void testStartMainGame_NotEnoughPlayers() {
        // 1 player => can't start game
        players.add(new Player("Alice"));

        boolean result = gamePlayController.startMainGame();
        assertFalse(result);

        verify(mockView).displayError("Need at least 2 players to start the game.");
        verifyNoMoreInteractions(mockView);
    }

    @Test
    void testStartMainGame_Success() {
        // Enough players
        players.add(new Player("Alice"));
        players.add(new Player("Bob"));

        // We'll make a spy of gamePlayController so we can verify it calls handleReinforcement()
        GamePlayController spyController = Mockito.spy(gamePlayController);

        // We re-inject the same fields into the spy
        // or just do `new GamePlayController(mockGameController, mockMap, players)` again.
        // Then do `spy(spyController)`.
        // We'll do it in a simpler way:
        spyController.setGameMap(mockMap);
        spyController.setPlayers(players);

        when(mockGameController.isGameStarted()).thenReturn(false);

        boolean result = spyController.startMainGame();
        assertTrue(result);

        // It should set the game started and phase to MAIN_GAME_PHASE
        verify(mockGameController).setGameStarted(true);
        verify(mockGameController).setCurrentPhase(GameController.MAIN_GAME_PHASE);

        // It should show a message and then call handleReinforcement()
        verify(mockView).displayMessage("Game started! Beginning reinforcement phase.");
        // Because we used a spy, verify that handleReinforcement() is invoked
        verify(spyController).handleReinforcement();
    }

    @Test
    void testHandleCommand_IssueOrder_GameNotStarted() {
        // "issueorder" when the game is not started => error
        when(mockGameController.isGameStarted()).thenReturn(false);

        String[] cmd = {"issueorder"};
        gamePlayController.handleCommand(cmd, "issueorder");

        verify(mockView).displayError("Game has not started yet. Use 'startgame' command.");
    }

    @Test
    void testHandleCommand_ExecuteOrders_GameNotStarted() {
        // "executeorders" when the game is not started => error
        when(mockGameController.isGameStarted()).thenReturn(false);

        String[] cmd = {"executeorders"};
        gamePlayController.handleCommand(cmd, "executeorders");

        verify(mockView).displayError("Game has not started yet. Use 'startgame' command.");
    }

    @Test
    void testHandleCommand_EndTurn_GameNotStarted() {
        // "endturn" when the game is not started => error
        when(mockGameController.isGameStarted()).thenReturn(false);

        String[] cmd = {"endturn"};
        gamePlayController.handleCommand(cmd, "endturn");

        verify(mockView).displayError("Game has not started yet. Use 'startgame' command.");
    }
}


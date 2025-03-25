package com.Game.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.Game.controller.GameController;
import com.Game.model.Map;
import com.Game.view.GameView;

/**
 * Unit tests for MapEditorPhase.
 * 
 * Since most methods in MapEditorPhase are placeholders, 
 * this test ensures they display the expected messages 
 * or make expected calls on the GameController.
 */
public class MapEditorPhaseTest {

    private GameController mockGameController;
    private GameView mockView;
    private Map mockMap;
    private MapEditorPhase mapEditorPhase;

    @BeforeEach
    void setUp() {
        // Create mocks
        mockGameController = mock(GameController.class);
        mockView = mock(GameView.class);
        mockMap = mock(Map.class);

        // Suppose mockGameController.getGameMap() returns mockMap
        when(mockGameController.getGameMap()).thenReturn(mockMap);
        // Suppose mockGameController.getView() returns mockView
        when(mockGameController.getView()).thenReturn(mockView);

        // Create a MapEditorPhase with the mocked GameController
        mapEditorPhase = new MapEditorPhase(mockGameController);
    }

    @Test
    void testEnter() {
        // Calling enter() should display a message and show map editing menu
        mapEditorPhase.enter();

        verify(mockView).displayMessage("Entering Map Editor Phase");
        verify(mockView).displayMapEditingMenu();
    }

    @Test
    void testExit() {
        // Calling exit() should display a message
        mapEditorPhase.exit();

        verify(mockView).displayMessage("Exiting Map Editor Phase");
    }

    @Test
    void testGetPhaseType() {
        // Should return "MapEditor"
        assertEquals("MapEditor", mapEditorPhase.getPhaseType());
    }

    @Test
    void testHandleCommand_EditContinent() {
        // "editcontinent"
        String[] cmdParts = {"editcontinent"};
        mapEditorPhase.handleCommand(cmdParts);

        // We expect the placeholder message
        verify(mockView).displayMessage("editcontinent command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_EditCountry() {
        // "editcountry"
        String[] cmdParts = {"editcountry"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMessage("editcountry command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_EditNeighbor() {
        // "editneighbor"
        String[] cmdParts = {"editneighbor"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMessage("editneighbor command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_ShowMap() {
        // "showmap"
        // This calls displayMap(d_gameMap, players)
        // We'll assume the game controller returns some players list
        when(mockGameController.getPlayers()).thenReturn(null); // or an empty list

        String[] cmdParts = {"showmap"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMap(mockMap, mockGameController.getPlayers());
    }

    @Test
    void testHandleCommand_SaveMap() {
        // "savemap"
        String[] cmdParts = {"savemap", "testmap"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMessage("savemap command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_EditMap() {
        // "editmap"
        String[] cmdParts = {"editmap", "someFile.map"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMessage("editmap command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_ValidateMap() {
        // "validatemap"
        String[] cmdParts = {"validatemap"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMessage("validatemap command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_LoadMap() {
        // "loadmap"
        String[] cmdParts = {"loadmap", "someMap.map"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayMessage("loadmap command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_GamePlayer() {
        // "gameplayer" -> setCurrentPhase(STARTUP_PHASE) 
        // + "gameplayer command will be handled in Build 2"
        String[] cmdParts = {"gameplayer", "-add", "Bob"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockGameController).setCurrentPhase(GameController.STARTUP_PHASE);
        verify(mockView).displayMessage("gameplayer command will be handled in Build 2");
    }

    @Test
    void testHandleCommand_Unknown() {
        // "foobar" => unknown => displayError
        String[] cmdParts = {"foobar"};
        mapEditorPhase.handleCommand(cmdParts);

        verify(mockView).displayError("Unknown command in Map Editor phase: foobar");
    }

    @Test
    void testHandleCommand_EmptyParts() {
        // If handleCommand() receives an empty array, it does nothing
        String[] cmdParts = {};
        mapEditorPhase.handleCommand(cmdParts);

        // We expect no interactions
        verifyNoInteractions(mockView);
        verifyNoInteractions(mockGameController);
    }
}

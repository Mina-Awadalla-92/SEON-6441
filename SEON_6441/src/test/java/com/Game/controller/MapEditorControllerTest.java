package com.Game.controller;

import com.Game.observer.GameLogger;
import com.Game.utils.MapLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Map;
import com.Game.view.GameView;

@RunWith(MockitoJUnitRunner.class)
public class MapEditorControllerTest {

    @Mock
    private GameController mockGameController;

    @Mock
    private GameLogger mockGameLogger;

    @Mock
    private Map mockGameMap;

    @Mock
    private MapLoader mockMapLoader;

    @Mock
    private GameView mockGameView;

    private MapEditorController mapEditorController;

    @Before
    public void setUp() {
        mockGameController = mock(GameController.class);
        mockGameMap = mock(Map.class);
        mockGameView = mock(GameView.class);
        mockGameLogger = mock(GameLogger.class);
        mockMapLoader = mock(MapLoader.class);
        mapEditorController = mock(MapEditorController.class);

        when(mockGameController.getView()).thenReturn(mockGameView);
        when(mockGameController.getMapFilePath()).thenReturn("test.map");

        mapEditorController = new MapEditorController(mockGameController, mockGameMap, mockMapLoader);
    }

    @Test
    public void testHandleCommand_editContinent_add() {
        String[] commandParts = {"editcontinent", "-add", "Asia", "5"};

        mapEditorController.handleCommand(commandParts, "editcontinent", true);

        // Verify that the method to add the continent was called
        verify(mockGameMap).addContinent("Asia", 5);
        verify(mockGameView).displayMessage("Continent added: Asia");
    }

    @Test
    public void testHandleCommand_editContinent_remove() {
        String[] commandParts = {"editcontinent", "-remove", "Asia"};

        mapEditorController.handleCommand(commandParts, "editcontinent", true);

        // Verify that the method to remove the continent was called
        verify(mockGameMap).removeContinent("Asia");
        verify(mockGameView).displayMessage("Continent removed: Asia");
    }

    @Test
    public void testHandleCommand_invalidContinentAction() {
        String[] commandParts = {"editcontinent", "-invalid", "Asia", "5"};

        mapEditorController.handleCommand(commandParts, "editcontinent", true);

        // Verify that an error message is displayed for invalid action
        verify(mockGameView).displayError("Usage: editcontinent -add continentID continentvalue -remove continentID");
    }

    @Test
    public void testHandleCommand_editCountry_add() {
        String[] commandParts = {"editcountry", "-add", "Canada", "Asia"};

        mapEditorController.handleCommand(commandParts, "editcountry", true);

        // Verify that the method to add the country was called
        verify(mockGameMap).addCountry("Canada", "Asia");
        verify(mockGameView).displayMessage("Country added: Canada");
    }

    @Test
    public void testHandleCommand_editNeighbor_add() {
        String[] commandParts = {"editneighbor", "-add", "Canada", "USA"};

        mapEditorController.handleCommand(commandParts, "editneighbor", true);

        // Verify that the method to add the neighbor was called
        verify(mockGameMap).addNeighbor("Canada", "USA");
        verify(mockGameView).displayMessage("Neighbor added between: Canada and USA");
    }

    @Test
    public void testHandleCommand_saveMap() {
        String[] commandParts = {"savemap", "test.map"};

        mapEditorController.handleCommand(commandParts, "savemap", true);

        // Verify that the save method was called on the map
        verify(mockGameMap).saveToFile("test.map");
    }

    @Test
    public void testHandleCommand_invalidMap() {
        when(mockMapLoader.isValid("test.map")).thenReturn(false);

        mapEditorController.handleCommand(new String[] {"validatemap"}, "validatemap", true);

        // Verify error message for invalid map
        verify(mockGameView).displayError("The map is invalid.");
    }
}


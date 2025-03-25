package com.Game.controller;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.Game.model.Map;
import com.Game.utils.MapLoader;
import com.Game.view.GameView;

/**
 * We use:
 *  - A real MapEditorController created with mock dependencies (GameController, Map, MapLoader).
 *  - The GameController itself is mocked to control or verify calls like getView(), setCurrentPhase(), * 
 * setMapFilePath().
 *  - The Map and MapLoader are also mocked, unless a test specifically requires real behavior.
 *  - We verify commands in handleCommand(...) route to the correct private methods 
 *    (handleEditContinent, handleEditCountry, etc.) by checking interactions on the mocks.
 */
public class MapEditorControllerTest {

    private GameController mockGameController;
    private Map mockMap;
    private MapLoader mockMapLoader;
    private GameView mockView;
    private MapEditorController mapEditorController;

    @BeforeEach
    void setUp() {
        // Create mocks
        mockGameController = mock(GameController.class);
        mockMap = mock(Map.class);
        mockMapLoader = mock(MapLoader.class);
        mockView = mock(GameView.class);

        // By default, when we call mockGameController.getView(), return mockView
        when(mockGameController.getView()).thenReturn(mockView);

        // Create a real MapEditorController with these mocks
        mapEditorController = new MapEditorController(mockGameController, mockMap, mockMapLoader);
    }

    @Test
    void testHandleCommand_EditContinent() {
        // Command: "editcontinent -add Asia 10"
        String[] commandParts = {"editcontinent", "-add", "Asia", "10"};
        boolean isMapLoaded = false;

        // Execute
        boolean result = mapEditorController.handleCommand(commandParts, "editcontinent", isMapLoaded);

        // The map should add a continent "Asia" with value 10
        verify(mockMap).addContinent("Asia", 10);
        // We also expect a success message
        verify(mockView).displayMessage("Continent added: Asia");

        // The returned isMapLoaded should remain the same (false) 
        assertFalse(result);
    }

    @Test
    void testHandleCommand_EditContinent_BadValue() {
        // "editcontinent -add Asia NotANumber"
        String[] commandParts = {"editcontinent", "-add", "Asia", "NotANumber"};
        boolean isMapLoaded = false;

        mapEditorController.handleCommand(commandParts, "editcontinent", isMapLoaded);

        // No call to addContinent should happen
        verify(mockMap, never()).addContinent(anyString(), anyInt());
        // Should show an error with "Invalid continent value..."
        verify(mockView).displayError("Invalid continent value: NotANumber");
    }

    @Test
    void testHandleCommand_EditCountry_Add() {
        // "editcountry -add Japan Asia"
        String[] commandParts = {"editcountry", "-add", "Japan", "Asia"};
        boolean isMapLoaded = true;

        mapEditorController.handleCommand(commandParts, "editcountry", isMapLoaded);

        verify(mockMap).addCountry("Japan", "Asia");
        verify(mockView).displayMessage("Country added: Japan");
    }

    @Test
    void testHandleCommand_EditCountry_Remove() {
        // "editcountry -remove Japan"
        String[] commandParts = {"editcountry", "-remove", "Japan"};
        boolean isMapLoaded = true;

        mapEditorController.handleCommand(commandParts, "editcountry", isMapLoaded);

        verify(mockMap).removeCountry("Japan");
        verify(mockView).displayMessage("Country removed: Japan");
    }

    @Test
    void testHandleCommand_EditNeighbor_Add() {
        // "editneighbor -add Japan Korea"
        String[] commandParts = {"editneighbor", "-add", "Japan", "Korea"};
        boolean isMapLoaded = false;

        mapEditorController.handleCommand(commandParts, "editneighbor", isMapLoaded);

        verify(mockMap).addNeighbor("Japan", "Korea");
        verify(mockView).displayMessage("Neighbor added between: Japan and Korea");
    }

    @Test
    void testHandleCommand_ShowMap() {
        // "showmap"
        String[] commandParts = {"showmap"};
        boolean isMapLoaded = true;

        mapEditorController.handleCommand(commandParts, "showmap", isMapLoaded);

        // Should display the map using the current map + the players from gameController
        verify(mockGameController).getPlayers(); // The code calls getPlayers() inside displayMap
        verify(mockView).displayMap(mockMap, mockGameController.getPlayers());
    }

    @Test
    void testHandleCommand_SaveMap() {
        // "savemap test.map"
        String[] commandParts = {"savemap", "test.map"};
        boolean isMapLoaded = true;

        mapEditorController.handleCommand(commandParts, "savemap", isMapLoaded);

        verify(mockMap).saveToFile("test.map");
    }

    @Test
    void testHandleCommand_EditMap_FileExists() throws Exception {
        // "editmap MyMap.map"
        // Suppose isMapExist() returns a non-null reader => map file exist
        when(mockMapLoader.isMapExist("MyMap.map")).thenReturn(mock(BufferedReader.class));
        // isValid => true
        when(mockMapLoader.isValid("MyMap.map")).thenReturn(true);
        // Then read() => loads a new Map in the loader
        Map loadedMap = mock(Map.class); // This is the new loaded map
        when(mockMapLoader.getLoadedMap()).thenReturn(loadedMap);
        // validateMap => returns true
        when(mockMapLoader.validateMap()).thenReturn(true);

        String[] commandParts = {"editmap", "MyMap.map"};
        boolean isMapLoaded = false;

        boolean result = mapEditorController.handleCommand(commandParts, "editmap", isMapLoaded);

        // Expect isMapLoaded => true after "editmap"
        assertTrue(result);

        // We also expect the loaded map is set in the gameController
        verify(mockMapLoader).read("MyMap.map");
        verify(mockGameController).setGameMap(loadedMap);
        verify(mockMapLoader).validateMap();
        // If the map is valid, we show "MyMap.map is loaded successfully."
        verify(mockView).displayMessage("MyMap.map is loaded successfully.");
    }

    @Test
    void testHandleCommand_ValidateMap_NoFilePath() {
        // If there's no mapFilePath, we show "No map loaded to validate."
        when(mockGameController.getMapFilePath()).thenReturn(null);

        String[] commandParts = {"validatemap"};
        boolean isMapLoaded = true;

        mapEditorController.handleCommand(commandParts, "validatemap", isMapLoaded);

        verify(mockView).displayError("No map loaded to validate.");
    }

    @Test
    void testHandleCommand_LoadMap_FileExistsAndValid() {
        // "loadmap RealMap.map"
        String[] commandParts = {"loadmap", "RealMap.map"};
        boolean isMapLoaded = false;

        // We expect setMapFilePath("RealMap.map")
        // Suppose isMapExist => return non-null
        when(mockMapLoader.isMapExist("RealMap.map")).thenReturn(mock(BufferedReader.class));
        // Suppose isValid => true
        when(mockMapLoader.isValid("RealMap.map")).thenReturn(true);
        // Suppose read => sets loadedMap in MapLoader
        Map loadedMap = mock(Map.class);
        when(mockMapLoader.getLoadedMap()).thenReturn(loadedMap);
        // Suppose validateMap => true
        when(mockMapLoader.validateMap()).thenReturn(true);

        boolean result = mapEditorController.handleCommand(commandParts, "loadmap", isMapLoaded);

        // Should set mapFilePath
        verify(mockGameController).setMapFilePath("RealMap.map");
        // read => load => set d_gameMap => ...
        verify(mockMapLoader).read("RealMap.map");
        // Then the loaded map is set in gameController
        verify(mockGameController).setGameMap(loadedMap);
        // validate => success => "RealMap.map is loaded successfully."
        verify(mockView).displayMessage("RealMap.map is loaded successfully.");
        // Then set phase => STARTUP_PHASE
        verify(mockGameController).setCurrentPhase(GameController.STARTUP_PHASE);

        // isMapLoaded => true
        assertTrue(result);
    }

    @Test
    void testHandleCommand_LoadMap_FileDoesNotExist() {
        // "loadmap Unknown.map"
        String[] commandParts = {"loadmap", "Unknown.map"};
        boolean isMapLoaded = false;

        // isMapExist => returns null => doesn't exist
        when(mockMapLoader.isMapExist("Unknown.map")).thenReturn(null);

        boolean result = mapEditorController.handleCommand(commandParts, "loadmap", isMapLoaded);

        verify(mockGameController).setMapFilePath("Unknown.map");
        verify(mockView).displayError("The specified map does not exist.");
        // We create a new map in that case => setGameMap(new Map())
        verify(mockGameController).setGameMap(any(Map.class));
        assertTrue(result, "After calling 'loadmap', we still set isMapLoaded = true by code logic");
    }

    @Test
    void testHandleCommand_GamePlayer() {
        // "gameplayer -add Bob"
        String[] commandParts = {"gameplayer", "-add", "Bob"};
        boolean isMapLoaded = true;

        boolean result = mapEditorController.handleCommand(commandParts, "gameplayer", isMapLoaded);

        // Should set phase => STARTUP_PHASE
        verify(mockGameController).setCurrentPhase(GameController.STARTUP_PHASE);
        // Then calls handleGamePlayer(...) on the game controller
        verify(mockGameController).handleGamePlayer("-add", "Bob");
        assertTrue(result);
    }

    @Test
    void testHandleCommand_Unknown() {
        // Some unknown command => "foobar"
        String[] commandParts = {"foobar"};
        boolean isMapLoaded = false;

        boolean result = mapEditorController.handleCommand(commandParts, "foobar", isMapLoaded);

        verify(mockView).displayError("Unknown command: foobar");
        // isMapLoaded remains false
        assertFalse(result);
    }
}

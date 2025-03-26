package com.Game.controller;

import com.Game.model.Map;
import com.Game.utils.MapLoader;
import com.Game.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MapEditorControllerTest {

    private MapEditorController mapEditorController;
    private GameController mockGameController;
    private Map mockGameMap;
    private MapLoader mockMapLoader;
    private GameView mockGameView;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockGameController = mock(GameController.class);
        mockGameMap = mock(Map.class);
        mockMapLoader = mock(MapLoader.class);
        mockGameView = mock(GameView.class);

        // Set up mock behavior
        when(mockGameController.getView()).thenReturn(mockGameView);

        // Initialize MapEditorController with mocked dependencies
        mapEditorController = new MapEditorController(mockGameController, mockGameMap, mockMapLoader);
    }

    @Test
    void testHandleCommand_ShowMap() {
        // Arrange
        String[] commandParts = {};
        String command = "showmap";

        // Act
        mapEditorController.handleCommand(commandParts, command, true);

        // Assert
        verify(mockGameView, times(1)).displayMap(mockGameMap, mockGameController.getPlayers());
    }

    @Test
    void testHandleEditContinent_AddContinent() {
        // Arrange
        String[] commandParts = {"editcontinent", "-add", "Asia", "5"};

        // Act
        mapEditorController.handleCommand(commandParts, "editcontinent", true);

        // Assert
        verify(mockGameMap, times(1)).addContinent("Asia", 5);
        verify(mockGameView, times(1)).displayMessage("Continent added: Asia");
    }

    @Test
    void testHandleEditContinent_RemoveContinent() {
        // Arrange
        String[] commandParts = {"editcontinent", "-remove", "Asia"};

        // Act
        mapEditorController.handleCommand(commandParts, "editcontinent", true);

        // Assert
        verify(mockGameMap, times(1)).removeContinent("Asia");
        verify(mockGameView, times(1)).displayMessage("Continent removed: Asia");
    }

    @Test
    void testHandleEditCountry_AddCountry() {
        // Arrange
        String[] commandParts = {"editcountry", "-add", "India", "Asia"};

        // Act
        mapEditorController.handleCommand(commandParts, "editcountry", true);

        // Assert
        verify(mockGameMap, times(1)).addCountry("India", "Asia");
        verify(mockGameView, times(1)).displayMessage("Country added: India");
    }

    @Test
    void testHandleEditCountry_RemoveCountry() {
        // Arrange
        String[] commandParts = {"editcountry", "-remove", "India"};

        // Act
        mapEditorController.handleCommand(commandParts, "editcountry", true);

        // Assert
        verify(mockGameMap, times(1)).removeCountry("India");
        verify(mockGameView, times(1)).displayMessage("Country removed: India");
    }

    @Test
    void testHandleLoadMap_MapExists() {
        // Arrange
        String[] commandParts = {"loadmap", "testmap.map"};
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockMapLoader.isMapExist("testmap.map")).thenReturn(mockReader);
        when(mockMapLoader.isValid("testmap.map")).thenReturn(true);
        when(mockMapLoader.getLoadedMap()).thenReturn(mockGameMap);
        when(mockMapLoader.validateMap()).thenReturn(true);

        // Act
        boolean result = mapEditorController.handleCommand(commandParts, "loadmap", false);

        // Assert
        assertTrue(result, "handleCommand should return true when the map is successfully loaded.");
        verify(mockGameController, times(1)).setGameMap(mockGameMap);
        verify(mockGameView, times(1)).displayMessage("testmap.map is loaded successfully.");
    }

    @Test
    void testHandleLoadMap_MapDoesNotExist() {
        // Arrange
        String[] commandParts = {"loadmap", "nonexistent.map"};
        when(mockMapLoader.isMapExist("nonexistent.map")).thenReturn(null);

        // Act
        boolean result = mapEditorController.handleCommand(commandParts, "loadmap", false);

        // Assert
        assertTrue(result, "handleCommand should return true even if the map does not exist (new map is created).");
        verify(mockGameView, times(1)).displayError("The specified map does not exist.");
    }

    @Test
    void testHandleValidateMap_ValidMap() {
        // Arrange
        when(mockGameController.getMapFilePath()).thenReturn("validmap.map");
        when(mockMapLoader.isValid("validmap.map")).thenReturn(true);

        // Act
        mapEditorController.handleCommand(new String[]{"validatemap"}, "validatemap", true);

        // Assert
        verify(mockMapLoader, times(1)).validateMap();
    }

    @Test
    void testHandleValidateMap_InvalidMap() {
        // Arrange
        when(mockGameController.getMapFilePath()).thenReturn("invalidmap.map");
        when(mockMapLoader.isValid("invalidmap.map")).thenReturn(false);

        // Act
        mapEditorController.handleCommand(new String[]{"validatemap"}, "validatemap", true);

        // Assert
        verify(mockGameView, times(1)).displayError("The map is invalid.");
    }
}
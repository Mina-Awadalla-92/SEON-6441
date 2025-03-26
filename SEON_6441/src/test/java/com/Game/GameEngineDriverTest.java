package com.Game;

import com.Game.controller.GameController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GameEngineDriverTest {

    @Test
    void testMain_StartsGameController() {
        // Mock the GameController
        GameController mockGameController = Mockito.mock(GameController.class);

        // Use Mockito to mock the static method call to create a GameController instance
        try (var mockedStatic = Mockito.mockStatic(GameController.class)) {
            mockedStatic.when(GameController::new).thenReturn(mockGameController);

            // Call the main method
            GameEngineDriver.main(new String[]{});

            // Verify that startGame was called
            Mockito.verify(mockGameController).startGame();
        }
    }

    @Test
    void testMain_NoExceptionsThrown() {
        // Mock the GameController
        GameController mockGameController = Mockito.mock(GameController.class);

        // Use Mockito to mock the static method call to create a GameController instance
        try (var mockedStatic = Mockito.mockStatic(GameController.class)) {
            mockedStatic.when(GameController::new).thenReturn(mockGameController);

            // Call the main method and ensure no exceptions are thrown
            try {
                GameEngineDriver.main(new String[]{});
            } catch (Exception e) {
                throw new AssertionError("Main method should not throw any exceptions.", e);
            }

            // Verify that startGame was called
            Mockito.verify(mockGameController).startGame();
        }
    }

    @Test
    void testMain_GameControllerInitialization() {
        // Mock the GameController
        GameController mockGameController = Mockito.mock(GameController.class);

        // Use Mockito to mock the static method call to create a GameController instance
        try (var mockedStatic = Mockito.mockStatic(GameController.class)) {
            mockedStatic.when(GameController::new).thenReturn(mockGameController);

            // Call the main method
            GameEngineDriver.main(new String[]{});

            // Verify that the GameController instance was created
            mockedStatic.verify(GameController::new);
        }
    }
}
        GameController mockGameController = Mockito.mock(GameController.class);

        // Use Mockito to mock the static method call to create a GameController instance
        try (var mockedStatic = Mockito.mockStatic(GameController.class)) {
            mockedStatic.when(GameController::new).thenReturn(mockGameController);

            // Call the main method
            GameEngineDriver.main(new String[]{});

            // Verify that startGame was called
            Mockito.verify(mockGameController).startGame();
        }
    }
}

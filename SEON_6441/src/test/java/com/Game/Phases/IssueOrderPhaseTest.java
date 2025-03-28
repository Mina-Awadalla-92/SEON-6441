package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.observer.GameLogger;
import com.Game.view.CommandPromptView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IssueOrderPhaseTest {
    private IssueOrderPhase issueOrderPhase;
    private GameController gameControllerMock;
    private CommandPromptView commandPromptViewMock;
    private Map gameMapMock;
    private Player playerMock;
    private GameLogger gameLoggerMock;

    @BeforeEach
    void setUp() {
        gameControllerMock = mock(GameController.class);
        commandPromptViewMock = mock(CommandPromptView.class);
        gameMapMock = mock(Map.class);
        playerMock = mock(Player.class);
        gameLoggerMock = mock(GameLogger.class);

        issueOrderPhase = new IssueOrderPhase();
    }

    @Test
    void testGetNextPhase() {
        assertEquals(PhaseType.ORDER_EXECUTION, issueOrderPhase.getNextPhase());
    }

    @Test
    void testValidateCommand_ValidCommands() {
        String[] validCommands = {"showmap", "deploy", "advance", "bomb", "blockade", "airlift", "negotiate"};
        for (String command : validCommands) {
            assertTrue(issueOrderPhase.validateCommand(command));
        }
    }

    @Test
    void testValidateCommand_InvalidCommands() {
        String[] invalidCommands = {"invalid", "attack", "move", "xyz"};
        for (String command : invalidCommands) {
            assertFalse(issueOrderPhase.validateCommand(command));
        }
    }
}

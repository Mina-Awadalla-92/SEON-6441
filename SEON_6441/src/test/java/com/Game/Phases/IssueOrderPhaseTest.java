package com.Game.Phases;

import static org.junit.Assert.*;

import com.Game.controller.GameController;
import com.Game.observer.GameLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;

@RunWith(MockitoJUnitRunner.class)
public class IssueOrderPhaseTest {

    @Mock
    private IssueOrderPhase issueOrderPhase;

    @Mock
    private GameController gameControllerMock;

    @Mock
    private CommandPromptView commandPromptViewMock;

    @Mock
    private Map gameMapMock;

    @Mock
    private Player playerMock;

    @Mock
    private GameLogger gameLoggerMock;

    @Before
    public void setUp() {
        gameControllerMock = mock(GameController.class);
        commandPromptViewMock = mock(CommandPromptView.class);
        gameMapMock = mock(Map.class);
        playerMock = mock(Player.class);
        gameLoggerMock = mock(GameLogger.class);

        issueOrderPhase = new IssueOrderPhase();
    }

    @Test
    public void testGetNextPhase() {
        assertEquals(PhaseType.ORDER_EXECUTION, issueOrderPhase.getNextPhase());
    }

    @Test
    public void testValidateCommand_ValidCommands() {
        String[] validCommands = {"showmap", "deploy", "advance", "bomb", "blockade", "airlift", "negotiate"};
        for (String command : validCommands) {
            assertTrue(issueOrderPhase.validateCommand(command));
        }
    }

    @Test
    public void testValidateCommand_InvalidCommands() {
        String[] invalidCommands = {"invalid", "attack", "move", "xyz"};
        for (String command : invalidCommands) {
            assertFalse(issueOrderPhase.validateCommand(command));
        }
    }
}

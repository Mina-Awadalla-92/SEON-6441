package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MapEditorPhase class.
 */
class MapEditorPhaseTest {
    private MapEditorPhase phase;
    private GameController gameControllerMock;
    private CommandPromptView commandPromptViewMock;
    private Map gameMapMock;

    @BeforeEach
    void setUp() {
        phase = new MapEditorPhase();
        gameControllerMock = Mockito.mock(GameController.class);
        commandPromptViewMock = Mockito.mock(CommandPromptView.class);
        gameMapMock = Mockito.mock(Map.class);
    }

    @Test
    void testGetNextPhase() {
        assertEquals(PhaseType.STARTUP, phase.getNextPhase());
    }

    @Test
    void testValidateCommand_Valid() {
        assertTrue(phase.validateCommand("editcontinent"));
        assertTrue(phase.validateCommand("loadmap"));
        assertTrue(phase.validateCommand("gameplayer"));
    }

    @Test
    void testValidateCommand_Invalid() {
        assertFalse(phase.validateCommand("invalidcommand"));
        assertFalse(phase.validateCommand("deploy"));
    }

    @Test
    void testStartPhase_GamePlayerCommand_TransitionsToStartupPhase() {
        String[] commandParts = {"gameplayer"};
        phase.StartPhase(gameControllerMock, Collections.emptyList(), commandPromptViewMock, commandParts, gameMapMock);

        Mockito.verify(gameControllerMock).setCurrentPhase(GameController.STARTUP_PHASE);
    }
}

package com.Game.Phases;

import static org.junit.Assert.*;

import com.Game.controller.GameController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.Game.model.Map;
import com.Game.view.CommandPromptView;

import java.util.Collections;

/**
 * Unit tests for the MapEditorPhase class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapEditorPhaseTest {
    private MapEditorPhase phase;
    private GameController gameControllerMock;
    private CommandPromptView commandPromptViewMock;
    private Map gameMapMock;

    @Before
    public void setUp() {
        phase = new MapEditorPhase();
        gameControllerMock = Mockito.mock(GameController.class);
        commandPromptViewMock = Mockito.mock(CommandPromptView.class);
        gameMapMock = Mockito.mock(Map.class);
    }

    @Test
    public void testGetNextPhase() {
        assertEquals(PhaseType.STARTUP, phase.getNextPhase());
    }

    @Test
    public void testValidateCommand_Valid() {
        assertTrue(phase.validateCommand("editcontinent"));
        assertTrue(phase.validateCommand("loadmap"));
        assertTrue(phase.validateCommand("gameplayer"));
    }

    @Test
    public void testValidateCommand_Invalid() {
        assertFalse(phase.validateCommand("invalidcommand"));
        assertFalse(phase.validateCommand("deploy"));
    }

    @Test
    public void testStartPhase_GamePlayerCommand_TransitionsToStartupPhase() {
        String[] commandParts = {"gameplayer"};
        phase.StartPhase(gameControllerMock, Collections.emptyList(), commandPromptViewMock, commandParts, gameMapMock);

        Mockito.verify(gameControllerMock).setCurrentPhase(GameController.STARTUP_PHASE);
    }
}

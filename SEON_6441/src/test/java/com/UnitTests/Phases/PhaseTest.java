package com.UnitTests.Phases;

import com.Game.Phases.*;
import com.Game.controller.GameController;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PhaseTest {

    private static class TestPhase extends Phase {
        @Override
        public void StartPhase(GameController p_gameController, List<Player> p_players,
                               CommandPromptView p_commandPromptView, String[] p_commandParts, Map p_gameMap) {
        }
    }

    @Test
    void testSetPhase_ValidPhases() {
        Phase phase = new TestPhase();

        // Test IssueOrderPhase transition
        Phase issueOrderPhase = phase.setPhase(PhaseType.ISSUE_ORDER);
        assertTrue(issueOrderPhase instanceof IssueOrderPhase);

        // Test OrderExecutionPhase transition
        Phase orderExecutionPhase = phase.setPhase(PhaseType.ORDER_EXECUTION);
        assertTrue(orderExecutionPhase instanceof OrderExecutionPhase);

        // Test StartupPhase transition
        Phase startupPhase = phase.setPhase(PhaseType.STARTUP);
        assertTrue(startupPhase instanceof StartupPhase);
    }
}


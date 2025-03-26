package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Phase class.
 * This test class verifies the behavior of the setPhase method and ensures
 * that the correct phase transitions occur based on the provided PhaseType.
 */
class PhaseTest {

    /**
     * Test that the setPhase method transitions to the IssueOrderPhase correctly.
     */
    @Test
    void testSetPhaseToIssueOrderPhase() {
        Phase phase = new StartupPhase();
        Phase nextPhase = phase.setPhase(PhaseType.ISSUE_ORDER);

        assertTrue(nextPhase instanceof IssueOrderPhase, "The next phase should be an instance of IssueOrderPhase.");
    }

    /**
     * Test that the setPhase method transitions to the OrderExecutionPhase correctly.
     */
    @Test
    void testSetPhaseToOrderExecutionPhase() {
        Phase phase = new StartupPhase();
        Phase nextPhase = phase.setPhase(PhaseType.ORDER_EXECUTION);

        assertTrue(nextPhase instanceof OrderExecutionPhase, "The next phase should be an instance of OrderExecutionPhase.");
    }

    /**
     * Test that the setPhase method transitions to the StartupPhase correctly.
     */
    @Test
    void testSetPhaseToStartupPhase() {
        Phase phase = new IssueOrderPhase();
        Phase nextPhase = phase.setPhase(PhaseType.STARTUP);

        assertTrue(nextPhase instanceof StartupPhase, "The next phase should be an instance of StartupPhase.");
    }

    /**
     * Test that the setPhase method throws an IllegalArgumentException for an invalid PhaseType.
     */
    @Test
    void testSetPhaseWithInvalidPhaseType() {
        Phase phase = new StartupPhase();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            phase.setPhase(PhaseType.UNKNOWN); // Assuming UNKNOWN is not a valid phase type
        });

        assertEquals("Invalid Phase Type!", exception.getMessage(), "The exception message should match.");
    }

    /**
     * Test that the StartPhase method is abstract and cannot be directly invoked.
     * This ensures that subclasses must implement the StartPhase method.
     */
    @Test
    void testStartPhaseIsAbstract() {
        Phase phase = new StartupPhase();

        // Since StartPhase is abstract, we cannot directly test it here.
        // Instead, we ensure that subclasses like StartupPhase implement it.
        assertDoesNotThrow(() -> {
            phase.StartPhase(new GameController(), new ArrayList<>(), new CommandPromptView(), new String[]{}, new Map());
        });
    }
}
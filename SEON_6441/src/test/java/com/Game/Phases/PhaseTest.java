package com.Game.Phases;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhaseTest {

    @Test
    void testSetPhase_WhenPhaseTypeIsIssueOrder() {
        Phase phase = new IssueOrderPhase();
        Phase nextPhase = phase.setPhase(PhaseType.ISSUE_ORDER);

        assertTrue(nextPhase instanceof IssueOrderPhase, "The next phase should be an instance of IssueOrderPhase.");
        assertNotNull(nextPhase, "The next phase should not be null.");
    }

    @Test
    void testSetPhase_WhenPhaseTypeIsOrderExecution() {
        Phase phase = new IssueOrderPhase();
        Phase nextPhase = phase.setPhase(PhaseType.ORDER_EXECUTION);

        assertTrue(nextPhase instanceof OrderExecutionPhase, "The next phase should be an instance of OrderExecutionPhase.");
        assertNotNull(nextPhase, "The next phase should not be null.");
    }

    @Test
    void testSetPhase_WhenPhaseTypeIsStartup() {
        Phase phase = new OrderExecutionPhase();
        Phase nextPhase = phase.setPhase(PhaseType.STARTUP);

        assertTrue(nextPhase instanceof StartupPhase, "The next phase should be an instance of StartupPhase.");
        assertNotNull(nextPhase, "The next phase should not be null.");
    }

    @Test
    void testSetPhase_WhenPhaseTypeIsInvalid() {
        Phase phase = new StartupPhase();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            phase.setPhase(null);
        });

        assertEquals("Invalid Phase Type!", exception.getMessage(), "The exception message should match.");
    }

    @Test
    void testSetPhaseToSamePhase() {
        Phase phase = new IssueOrderPhase();
        Phase nextPhase = phase.setPhase(PhaseType.ISSUE_ORDER);

        assertTrue(nextPhase instanceof IssueOrderPhase, "The next phase should still be an instance of IssueOrderPhase.");
        assertNotNull(nextPhase, "The next phase should not be null.");
    }

    @Test
    void testSetPhaseWithUnsupportedPhaseType() {
        Phase phase = new StartupPhase();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            phase.setPhase(PhaseType.UNKNOWN); // Assuming UNKNOWN is not a valid phase type
        });

        assertEquals("Invalid Phase Type!", exception.getMessage(), "The exception message should match.");
    }
}
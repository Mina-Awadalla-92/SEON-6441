package com.Game.Phases;

import com.Game.observer.GameLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhaseTest {

    private Phase phase;
    private GameLogger gameLoggerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock instance of GameLogger
        gameLoggerMock = mock(GameLogger.class);

        // Use reflection to inject the mock instance into the GameLogger singleton
        Field instanceField = GameLogger.class.getDeclaredField("d_instance");
        instanceField.setAccessible(true);
        instanceField.set(null, gameLoggerMock); // Set our mock as the singleton instance

        // Initialize a concrete phase (assuming IssueOrderPhase extends Phase)
        phase = new IssueOrderPhase();
    }

    @Test
    void testSetPhase_ValidPhases() {
        Phase newPhase = phase.setPhase(PhaseType.ORDER_EXECUTION);
        assertTrue(newPhase instanceof OrderExecutionPhase);

        newPhase = phase.setPhase(PhaseType.STARTUP);
        assertTrue(newPhase instanceof StartupPhase);

        newPhase = phase.setPhase(PhaseType.MAP_EDITOR);
        assertTrue(newPhase instanceof MapEditorPhase);
    }

    @Test
    void testSetPhase_NullPhase() {
        assertThrows(NullPointerException.class, () -> {
            phase.setPhase(null);
        });
    }

    @Test
    void testSetPhase_LogsPhaseChange() {
        phase.setPhase(PhaseType.ORDER_EXECUTION);
        verify(gameLoggerMock, times(1)).logPhaseChange("ORDER_EXECUTION");
    }
}
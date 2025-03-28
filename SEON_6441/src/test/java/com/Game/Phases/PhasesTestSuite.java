package com.Game.Phases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for the com.Game.command package.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    IssueOrderPhaseTest.class,
    MapEditorPhaseTest.class,
    OrderExecutionPhaseTest.class,
    PhaseTest.class,
    StartupPhaseTest.class
    // ... add other test classes in com.Game.command
})
public class PhasesTestSuite {
    // Empty class: used only as a holder for the above annotations.
}


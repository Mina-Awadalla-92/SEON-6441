package com.Game.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for the com.Game.command package.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    GameControllerTest.class,
    GamePlayController.class,
    MapEditorControllerTest.class
    // ... add other test classes in com.Game.command
})
public class ControllerTestSuite {
    // Empty class: used only as a holder for the above annotations.
}

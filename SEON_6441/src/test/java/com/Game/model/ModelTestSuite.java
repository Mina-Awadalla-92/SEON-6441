package com.Game.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite for the com.Game.command package.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    CardSystemTest.class,
    MapTest.class,
    TerritoryTest.class
    // ... add other test classes in com.Game.command
})
public class ModelTestSuite {
    // Empty class: used only as a holder for the above annotations.
}


package com.Game.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

// Import all package-level suites
import com.Game.command.CommandTestSuite;
import com.Game.controller.ControllerTestSuite;
import com.Game.integration.IntegrationTestSuite;
import com.Game.model.ModelTestSuite;
import com.Game.model.order.OrderTestSuite;
import com.Game.observer.ObserverTestSuite;
import com.Game.Phases.PhasesTestSuite;
import com.Game.utils.UtilsTestSuite;

/**
 * A single master test suite that runs all package-level suites in the project.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    CommandTestSuite.class,
    ControllerTestSuite.class,
    IntegrationTestSuite.class,
    ModelTestSuite.class,
    OrderTestSuite.class,
    ObserverTestSuite.class,
    PhasesTestSuite.class,
    UtilsTestSuite.class
})
public class AllTests {
    // This class remains empty; it is used only as a holder for the above annotations.
}

package com.UnitTests.TestSuites;

import com.UnitTests.Phases.IssueOrderPhaseTest;
import com.UnitTests.Phases.OrderExecutionPhaseTest;
import com.UnitTests.Phases.PhaseTest;
import com.UnitTests.Phases.StartupPhaseTest;
import com.UnitTests.controller.GamePlayControllerTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;


@Suite
@SelectClasses({
        IssueOrderPhaseTest.class,
//        OrderExecutionPhaseTest.class,
//        PhaseTest.class,
//        StartupPhaseTest.class,
//        GamePlayControllerTest.class
})
@SuiteDisplayName("All Tests Suite")
public class AllTestsSuite {
}

package com.Game.model.order;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    DeployOrderTest.class,
    AdvanceOrderTest.class,
    AirliftOrderTest.class,
    BlockadeOrderTest.class,
    BombOrderTest.class,
    NegotiateOrderTest.class
    // ... add other order test classes here
})
public class OrderTestSuite {
    // This class remains empty. It is used only as a holder for the annotations.
}

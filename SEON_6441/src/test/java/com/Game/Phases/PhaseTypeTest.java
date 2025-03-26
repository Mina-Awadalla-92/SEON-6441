package com.Game.Phases; 
@Test
void testPhaseTypeValues() {
    PhaseType[] expectedValues = {PhaseType.STARTUP, PhaseType.ISSUE_ORDER, PhaseType.ORDER_EXECUTION};
    PhaseType[] actualValues = PhaseType.values();

    assertArrayEquals(expectedValues, actualValues, "The PhaseType enum values should match the expected values.");
}

@Test
void testPhaseTypeValueOf() {
    assertEquals(PhaseType.STARTUP, PhaseType.valueOf("STARTUP"), "PhaseType.STARTUP should be retrievable using valueOf.");
    assertEquals(PhaseType.ISSUE_ORDER, PhaseType.valueOf("ISSUE_ORDER"), "PhaseType.ISSUE_ORDER should be retrievable using valueOf.");
    assertEquals(PhaseType.ORDER_EXECUTION, PhaseType.valueOf("ORDER_EXECUTION"), "PhaseType.ORDER_EXECUTION should be retrievable using valueOf.");
}

@Test
void testPhaseTypeToString() {
    assertEquals("STARTUP", PhaseType.STARTUP.toString(), "PhaseType.STARTUP should return 'STARTUP' as its string representation.");
    assertEquals("ISSUE_ORDER", PhaseType.ISSUE_ORDER.toString(), "PhaseType.ISSUE_ORDER should return 'ISSUE_ORDER' as its string representation.");
    assertEquals("ORDER_EXECUTION", PhaseType.ORDER_EXECUTION.toString(), "PhaseType.ORDER_EXECUTION should return 'ORDER_EXECUTION' as its string representation.");
}
@Test
void testPhaseTypeOrdinal() {
    assertEquals(0, PhaseType.STARTUP.ordinal(), "STARTUP should have ordinal value 0.");
    assertEquals(1, PhaseType.ISSUE_ORDER.ordinal(), "ISSUE_ORDER should have ordinal value 1.");
    assertEquals(2, PhaseType.ORDER_EXECUTION.ordinal(), "ORDER_EXECUTION should have ordinal value 2.");
}

@Test
void testPhaseTypeName() {
    assertEquals("STARTUP", PhaseType.STARTUP.name(), "STARTUP should return its name as 'STARTUP'.");
    assertEquals("ISSUE_ORDER", PhaseType.ISSUE_ORDER.name(), "ISSUE_ORDER should return its name as 'ISSUE_ORDER'.");
    assertEquals("ORDER_EXECUTION", PhaseType.ORDER_EXECUTION.name(), "ORDER_EXECUTION should return its name as 'ORDER_EXECUTION'.");
}
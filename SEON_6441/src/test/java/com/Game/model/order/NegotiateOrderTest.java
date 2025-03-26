package com.Game.model.order; 
@Test
void testConstructorWithParameters() {
	Player issuer = new Player("Issuer");
	Player target = new Player("Target");

	NegotiateOrder negotiateOrder = new NegotiateOrder(issuer, target);

	assertEquals(issuer, negotiateOrder.getIssuer(), "The issuer should match the provided player.");
	assertEquals(target, negotiateOrder.getPlayerTo(), "The target player should match the provided player.");
}

@Test
void testCopyConstructor() {
	Player issuer = new Player("Issuer");
	Player target = new Player("Target");

	NegotiateOrder original = new NegotiateOrder(issuer, target);
	NegotiateOrder copy = new NegotiateOrder(original);

	assertEquals(original.getIssuer(), copy.getIssuer(), "The issuer should match the original order.");
	assertEquals(original.getPlayerTo(), copy.getPlayerTo(), "The target player should match the original order.");
}

@Test
void testSetAndGetPlayerTo() {
	Player target1 = new Player("Target1");
	Player target2 = new Player("Target2");

	NegotiateOrder negotiateOrder = new NegotiateOrder();
	negotiateOrder.setPlayerTo(target1);

	assertEquals(target1, negotiateOrder.getPlayerTo(), "The target player should match the set value.");

	negotiateOrder.setPlayerTo(target2);
	assertEquals(target2, negotiateOrder.getPlayerTo(), "The target player should match the updated value.");
}

@Test
void testExecute() {
	Player issuer = new Player("Issuer");
	Player target = new Player("Target");

	NegotiateOrder negotiateOrder = new NegotiateOrder(issuer, target);
	negotiateOrder.execute();

	assertTrue(issuer.getNegociatedPlayersPerTurn().contains(target), "The target player should be in the issuer's negotiated players list.");
	assertTrue(target.getNegociatedPlayersPerTurn().contains(issuer), "The issuer should be in the target player's negotiated players list.");
}
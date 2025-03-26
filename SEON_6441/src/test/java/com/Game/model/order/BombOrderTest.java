package com.Game.model.order; 
@Test
void testExecute_BombSuccess() {
	Player attacker = new Player("Attacker");
	Player defender = new Player("Defender");

	Territory target = new Territory("Target", "Continent1", 5);
	target.setOwner(defender);
	target.setNumOfArmies(20);

	BombOrder bombOrder = new BombOrder(attacker, target);
	bombOrder.execute();

	assertEquals(10, target.getNumOfArmies(), "The number of armies in the target territory should be halved.");
}

@Test
void testExecute_BombPreventedByDiplomacy() {
	Player attacker = new Player("Attacker");
	Player defender = new Player("Defender");

	Territory target = new Territory("Target", "Continent1", 5);
	target.setOwner(defender);
	target.setNumOfArmies(20);

	attacker.getNegociatedPlayersPerTurn().add(defender);

	BombOrder bombOrder = new BombOrder(attacker, target);
	bombOrder.execute();

	assertEquals(20, target.getNumOfArmies(), "The number of armies in the target territory should remain unchanged due to diplomacy.");
}

@Test
void testConstructorWithParameters() {
	Player attacker = new Player("Attacker");
	Territory target = new Territory("Target", "Continent1", 5);

	BombOrder bombOrder = new BombOrder(attacker, target);

	assertEquals(attacker, bombOrder.getIssuer(), "The issuer should match the provided player.");
	assertEquals(target, bombOrder.getTerritoryTo(), "The target territory should match the provided territory.");
}

@Test
void testCopyConstructor() {
	Player attacker = new Player("Attacker");
	Territory target = new Territory("Target", "Continent1", 5);

	BombOrder original = new BombOrder(attacker, target);
	BombOrder copy = new BombOrder(original);

	assertEquals(original.getIssuer(), copy.getIssuer(), "The issuer should match the original order.");
	assertEquals(original.getTerritoryTo(), copy.getTerritoryTo(), "The target territory should match the original order.");
}

@Test
void testSetAndGetTerritoryTo() {
	Territory target1 = new Territory("Target1", "Continent1", 5);
	Territory target2 = new Territory("Target2", "Continent2", 10);

	BombOrder bombOrder = new BombOrder();
	bombOrder.setTerritoryTo(target1);

	assertEquals(target1, bombOrder.getTerritoryTo(), "The target territory should match the set value.");

	bombOrder.setTerritoryTo(target2);
	assertEquals(target2, bombOrder.getTerritoryTo(), "The target territory should match the updated value.");
}
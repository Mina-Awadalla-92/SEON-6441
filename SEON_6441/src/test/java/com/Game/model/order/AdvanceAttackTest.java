package com.Game.model.order; 
@Test
void testExecute_AttackSuccess() {
	Player attacker = new Player("Attacker");
	Player defender = new Player("Defender");

	Territory source = new Territory("Source", "Continent1", 5);
	Territory target = new Territory("Target", "Continent1", 5);

	source.setOwner(attacker);
	target.setOwner(defender);

	source.setNumOfArmies(10);
	target.setNumOfArmies(5);

	attacker.addTerritory(source);
	defender.addTerritory(target);

	AdvanceAttack advanceAttack = new AdvanceAttack(attacker, source, target, 8);
	advanceAttack.execute();

	if (target.getOwner() == attacker) {
		assertEquals(attacker, target.getOwner());
		assertTrue(target.getNumOfArmies() > 0);
		assertTrue(source.getNumOfArmies() >= 0);
	} else {
		assertEquals(defender, target.getOwner());
		assertTrue(target.getNumOfArmies() > 0);
		assertTrue(source.getNumOfArmies() >= 0);
	}
}

@Test
void testExecute_AttackFails() {
	Player attacker = new Player("Attacker");
	Player defender = new Player("Defender");

	Territory source = new Territory("Source", "Continent1", 5);
	Territory target = new Territory("Target", "Continent1", 5);

	source.setOwner(attacker);
	target.setOwner(defender);

	source.setNumOfArmies(10);
	target.setNumOfArmies(15);

	attacker.addTerritory(source);
	defender.addTerritory(target);

	AdvanceAttack advanceAttack = new AdvanceAttack(attacker, source, target, 8);
	advanceAttack.execute();

	assertEquals(defender, target.getOwner());
	assertTrue(target.getNumOfArmies() > 0);
	assertTrue(source.getNumOfArmies() >= 0);
}

@Test
void testExecute_DiplomacyActive() {
	Player attacker = new Player("Attacker");
	Player defender = new Player("Defender");

	Territory source = new Territory("Source", "Continent1", 5);
	Territory target = new Territory("Target", "Continent1", 5);

	source.setOwner(attacker);
	target.setOwner(defender);

	source.setNumOfArmies(10);
	target.setNumOfArmies(5);

	attacker.addTerritory(source);
	defender.addTerritory(target);

	attacker.getNegociatedPlayersPerTurn().add(defender);

	AdvanceAttack advanceAttack = new AdvanceAttack(attacker, source, target, 8);
	advanceAttack.execute();

	assertEquals(defender, target.getOwner());
	assertEquals(10, source.getNumOfArmies());
	assertEquals(5, target.getNumOfArmies());
}

@Test
void testConstructor() {
	Player attacker = new Player("Attacker");
	Territory source = new Territory("Source", "Continent1", 5);
	Territory target = new Territory("Target", "Continent1", 5);

	AdvanceAttack advanceAttack = new AdvanceAttack(attacker, source, target, 10);

	assertEquals(attacker, advanceAttack.getIssuer());
	assertEquals(source, advanceAttack.getD_territoryFrom());
	assertEquals(target, advanceAttack.getD_territoryTo());
	assertEquals(10, advanceAttack.getD_numberOfArmies());
}

@Test
void testCopyConstructor() {
	Player attacker = new Player("Attacker");
	Territory source = new Territory("Source", "Continent1", 5);
	Territory target = new Territory("Target", "Continent1", 5);

	AdvanceAttack original = new AdvanceAttack(attacker, source, target, 10);
	AdvanceAttack copy = new AdvanceAttack(original);

	assertEquals(original.getIssuer(), copy.getIssuer());
	assertEquals(original.getD_territoryFrom(), copy.getD_territoryFrom());
	assertEquals(original.getD_territoryTo(), copy.getD_territoryTo());
	assertEquals(original.getD_numberOfArmies(), copy.getD_numberOfArmies());
}
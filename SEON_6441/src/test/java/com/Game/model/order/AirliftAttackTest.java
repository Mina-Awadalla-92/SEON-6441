package com.Game.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.Game.model.Player;
import com.Game.model.Territory;

public class AirliftAttackTest {

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

        AirliftAttack airliftAttack = new AirliftAttack(attacker, source, target, 8);
        airliftAttack.execute();

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

        AirliftAttack airliftAttack = new AirliftAttack(attacker, source, target, 8);
        airliftAttack.execute();

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

        AirliftAttack airliftAttack = new AirliftAttack(attacker, source, target, 8);
        airliftAttack.execute();

        assertEquals(defender, target.getOwner());
        assertEquals(10, source.getNumOfArmies());
        assertEquals(5, target.getNumOfArmies());
    }

    @Test
    void testConstructor() {
        Player attacker = new Player("Attacker");
        Territory source = new Territory("Source", "Continent1", 5);
        Territory target = new Territory("Target", "Continent1", 5);

        AirliftAttack airliftAttack = new AirliftAttack(attacker, source, target, 10);

        assertEquals(attacker, airliftAttack.getIssuer());
        assertEquals(source, airliftAttack.getD_territoryFrom());
        assertEquals(target, airliftAttack.getD_territoryTo());
        assertEquals(10, airliftAttack.getD_numberOfArmies());
    }

    @Test
    void testCopyConstructor() {
        Player attacker = new Player("Attacker");
        Territory source = new Territory("Source", "Continent1", 5);
        Territory target = new Territory("Target", "Continent1", 5);

        AirliftAttack original = new AirliftAttack(attacker, source, target, 10);
        AirliftAttack copy = new AirliftAttack(original);

        assertEquals(original.getIssuer(), copy.getIssuer());
        assertEquals(original.getD_territoryFrom(), copy.getD_territoryFrom());
        assertEquals(original.getD_territoryTo(), copy.getD_territoryTo());
        assertEquals(original.getD_numberOfArmies(), copy.getD_numberOfArmies());
    }
}
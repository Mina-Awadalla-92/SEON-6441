package com.Game.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.Game.model.Player;
import com.Game.model.Territory;

public class DeployOrderTest {

    @Test
    void testConstructorWithParameters() {
        Player player = new Player("Player1");
        Territory territory = new Territory("Territory1", "Continent1", 5);

        DeployOrder deployOrder = new DeployOrder(player, territory, 10);

        assertEquals(player, deployOrder.getIssuer(), "The issuer should match the provided player.");
        assertEquals(territory, deployOrder.getTargetTerritory(), "The target territory should match the provided territory.");
        assertEquals(10, deployOrder.getNumberOfArmies(), "The number of armies should match the provided value.");
    }

    @Test
    void testCopyConstructor() {
        Player player = new Player("Player1");
        Territory territory = new Territory("Territory1", "Continent1", 5);

        DeployOrder original = new DeployOrder(player, territory, 10);
        DeployOrder copy = new DeployOrder(original);

        assertEquals(original.getIssuer(), copy.getIssuer(), "The issuer should match the original order.");
        assertEquals(original.getTargetTerritory(), copy.getTargetTerritory(), "The target territory should match the original order.");
        assertEquals(original.getNumberOfArmies(), copy.getNumberOfArmies(), "The number of armies should match the original order.");
    }

    @Test
    void testSetAndGetTargetTerritory() {
        Territory territory1 = new Territory("Territory1", "Continent1", 5);
        Territory territory2 = new Territory("Territory2", "Continent2", 10);

        DeployOrder deployOrder = new DeployOrder();
        deployOrder.setTargetTerritory(territory1);

        assertEquals(territory1, deployOrder.getTargetTerritory(), "The target territory should match the set value.");

        deployOrder.setTargetTerritory(territory2);
        assertEquals(territory2, deployOrder.getTargetTerritory(), "The target territory should match the updated value.");
    }

    @Test
    void testSetAndGetNumberOfArmies() {
        DeployOrder deployOrder = new DeployOrder();

        deployOrder.setNumberOfArmies(10);
        assertEquals(10, deployOrder.getNumberOfArmies(), "The number of armies should match the set value.");

        deployOrder.setNumberOfArmies(20);
        assertEquals(20, deployOrder.getNumberOfArmies(), "The number of armies should match the updated value.");
    }

    @Test
    void testExecute() {
        Player player = new Player("Player1");
        Territory territory = new Territory("Territory1", "Continent1", 5);

        territory.setNumOfArmies(10);

        DeployOrder deployOrder = new DeployOrder(player, territory, 15);
        deployOrder.execute();

        assertEquals(25, territory.getNumOfArmies(), "The number of armies in the territory should be updated after execution.");
    }
}
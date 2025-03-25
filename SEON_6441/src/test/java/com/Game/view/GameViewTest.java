package com.Game.view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Territory;

/**
 * We redirect System.out and System.err to ByteArrayOutputStreams,
 * call GameView methods, then compare the captured output with what we expect.
 */
public class GameViewTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private GameView gameView;

    @BeforeEach
    void setUp() {
        // Redirect System.out and System.err to our ByteArrayOutputStream
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        gameView = new GameView();
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out and System.err
        System.setOut(originalOut);
        System.setErr(originalErr);

        // Reset streams
        outContent.reset();
        errContent.reset();
    }

    @Test
    void testDisplayWelcomeMessage() {
        gameView.displayWelcomeMessage();
        String printed = outContent.toString().trim();

        assertEquals("Welcome to Warzone Game!", printed);
    }

    @Test
    void testDisplayMapEditingMenu() {
        gameView.displayMapEditingMenu();
        String printed = outContent.toString();

        // Just check for a few key lines
        assertTrue(printed.contains("Map Editing Phase Menu"), 
            "Expected the map editing menu heading.");
        assertTrue(printed.contains("editcontinent <args>"), 
            "Expected 'editcontinent' usage in output.");
        assertTrue(printed.contains("gameplayer <args>"), 
            "Expected 'gameplayer' usage in output.");
    }

    @Test
    void testDisplayStartupMenu() {
        gameView.displayStartupMenu();
        String printed = outContent.toString();

        assertTrue(printed.contains("Startup Phase Menu"));
        assertTrue(printed.contains("assigncountries"));
        assertTrue(printed.contains("startgame"));
    }

    @Test
    void testDisplayMainGameMenu() {
        gameView.displayMainGameMenu();
        String printed = outContent.toString();

        assertTrue(printed.contains("Main Game Phase Menu"));
        assertTrue(printed.contains("issueorder"));
        assertTrue(printed.contains("executeorders"));
    }

    @Test
    void testDisplayMap() {
        // Setup a mock map with territories
        Map mockMap = new Map();
        Territory t1 = new Territory("T1", "ContA", 2);
        t1.setNumOfArmies(3);
        Territory t2 = new Territory("T2", "ContA", 2);
        t2.setNumOfArmies(5);

        // T1 neighbors T2, T2 neighbors T1
        t1.addNeighbor(t2);
        t2.addNeighbor(t1);

        mockMap.addTerritory(t1);
        mockMap.addTerritory(t2);

        // Setup players
        Player p1 = new Player("Alice");
        p1.addTerritory(t1);
        p1.setNbrOfReinforcementArmies(10);

        Player p2 = new Player("Bob");
        p2.addTerritory(t2);
        p2.setNbrOfReinforcementArmies(5);

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        // Act
        gameView.displayMap(mockMap, players);

        // Check the output
        String printed = outContent.toString();
        // We expect "Continent: ContA", the territory details, neighbors, players info, etc.
        assertTrue(printed.contains("Continent: ContA"), "Should show the continent name 'ContA'.");
        assertTrue(printed.contains("T1 | Armies: 3"), "Should show territory T1 with 3 armies.");
        assertTrue(printed.contains("Neighbors: T2 ||"), "T1 neighbors T2.");
        assertTrue(printed.contains("T2 | Armies: 5"), "Should show territory T2 with 5 armies.");
        assertTrue(printed.contains("Neighbors: T1 ||"), "T2 neighbors T1.");

        // Player statuses
        assertTrue(printed.contains("Player Name: Alice"), "Should list player Alice.");
        assertTrue(printed.contains("Total Territories Owned: 1"), 
            "Alice owns exactly 1 territory.");
        assertTrue(printed.contains("Available Reinforcement Armies: 10"), 
            "Alice has 10 reinforcements.");

        assertTrue(printed.contains("Player Name: Bob"), "Should list player Bob.");
        assertTrue(printed.contains("Total Territories Owned: 1"), 
            "Bob owns 1 territory.");
        assertTrue(printed.contains("Available Reinforcement Armies: 5"), 
            "Bob has 5 reinforcements.");
    }

    @Test
    void testDisplayMessage() {
        gameView.displayMessage("Hello World");
        String printed = outContent.toString().trim();

        assertEquals("Hello World", printed);
    }

    @Test
    void testDisplayError() {
        gameView.displayError("Something went wrong");
        String errPrinted = errContent.toString().trim();

        // displayError prints to System.err
        assertEquals("Error: Something went wrong", errPrinted);
    }

    @Test
    void testDisplayReinforcementPhase() {
        gameView.displayReinforcementPhase();
        String printed = outContent.toString().trim();

        assertEquals("===== REINFORCEMENT PHASE =====", printed);
    }

    @Test
    void testDisplayReinforcementAllocation() {
        gameView.displayReinforcementAllocation("Alice", 5);
        String printed = outContent.toString().trim();

        assertEquals("Alice receives 5 reinforcement armies.", printed);
    }

    @Test
    void testDisplayReinforcementComplete() {
        gameView.displayReinforcementComplete();
        String printed = outContent.toString().trim();

        // We expect some spacing, so let's just check the text ignoring newlines:
        assertTrue(printed.contains("Players have been assigned their armies!"));
    }

    @Test
    void testDisplayIssueOrdersPhase() {
        gameView.displayIssueOrdersPhase();
        String printed = outContent.toString().trim();

        assertEquals("===== ISSUE ORDERS PHASE =====", printed);
    }

    @Test
    void testDisplayPlayerTurn() {
        gameView.displayPlayerTurn("Bob", 7);
        String printed = outContent.toString().trim();

        // Something like: "Bob's turn (7 reinforcements left)"
        assertEquals("Bob's turn (7 reinforcements left)", printed);
    }

    @Test
    void testDisplayPlayerTerritories() {
        List<Territory> territories = new ArrayList<>();
        Territory t1 = new Territory("Africa", "ContA", 5);
        t1.setNumOfArmies(3);
        territories.add(t1);

        Territory t2 = new Territory("Asia", "ContB", 5);
        t2.setNumOfArmies(10);
        territories.add(t2);

        gameView.displayPlayerTerritories(territories);
        String printed = outContent.toString();

        assertTrue(printed.contains("Your territories:"));
        assertTrue(printed.contains("1. Africa (3 armies)"));
        assertTrue(printed.contains("2. Asia (10 armies)"));
    }

    @Test
    void testDisplayIssueOrdersComplete() {
        gameView.displayIssueOrdersComplete();
        String printed = outContent.toString().trim();

        assertTrue(printed.contains("All players have issued their orders."));
    }

    @Test
    void testDisplayExecuteOrdersPhase() {
        gameView.displayExecuteOrdersPhase();
        String printed = outContent.toString().trim();

        assertTrue(printed.contains("===== EXECUTE ORDERS PHASE ====="));
        assertTrue(printed.contains("Executing all orders..."));
    }

    @Test
    void testDisplayExecutingOrder() {
        gameView.displayExecutingOrder("Bob");
        String printed = outContent.toString().trim();

        // Expected: "Executing order from Bob:"
        assertEquals("Executing order from Bob:", printed);
    }

    @Test
    void testDisplayExecuteOrdersComplete() {
        gameView.displayExecuteOrdersComplete();
        String printed = outContent.toString().trim();

        // "All orders executed. Use 'endturn'..."
        assertTrue(printed.contains("All orders executed."));
        assertTrue(printed.contains("Use 'endturn' to end the turn or 'showmap' to see the current state."));
    }

    @Test
    void testDisplayWinner() {
        gameView.displayWinner("Alice");
        String printed = outContent.toString();

        assertTrue(printed.contains("Game Over! Alice wins!"));
        assertTrue(printed.contains("*******************************"));
    }

    @Test
    void testDisplayEndTurn() {
        gameView.displayEndTurn();
        String printed = outContent.toString().trim();

        assertEquals("Turn ended. Starting new turn.", printed);
    }
}

